package com.backend.recruitmentapp.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.backend.recruitmentapp.model.Person;
import com.backend.recruitmentapp.model.Role;
import com.backend.recruitmentapp.security.payload.response.JwtResponse;
import com.backend.recruitmentapp.security.payload.request.LoginRequest;
import com.backend.recruitmentapp.security.payload.response.MessageResponse;
import com.backend.recruitmentapp.security.payload.request.SignupRequest;
import com.backend.recruitmentapp.repository.PersonRepository;
import com.backend.recruitmentapp.repository.RoleRepository;
import com.backend.recruitmentapp.security.JwtUtils;
import com.backend.recruitmentapp.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Authenticates a user.
 * Logs in with a token.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
//@CrossOrigin(name="localhost",origins = "*", maxAge = 3600)
//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Logs in a user.
     * Verifies credentials and sends a token.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, javax.servlet.http.HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/"); // set the cookie's path to "/"
        cookie.setHttpOnly(true); // set the HttpOnly flag to prevent client-side access
        cookie.setSecure(true); // set the Secure flag to ensure the cookie is only sent over HTTPS
        cookie.setComment("SameSite=None"); // set the SameSite attribute to "None"

        // set the cookie's max age to 1 hour
        Instant expirationTime = Instant.now().plus(Duration.ofHours(1));
        Date expirationDate = Date.from(expirationTime);
        cookie.setMaxAge((int) (expirationDate.getTime() / 1000L));
        response.addCookie(cookie);
        System.out.println("Cookie: " + cookie.getValue());
        System.out.println("jwt: " + jwt);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * Registers a new user.
     * @param userData username, email, password, role, name, surname, person number
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest userData) {
        //Verify that the username is unique.
        if (personRepository.existsByUsername(userData.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        //Verify that the same email has not already been registered.
        if (personRepository.existsByEmail(userData.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        //Verify that the same person number has not already been registered.
        if (personRepository.existsByPnr(userData.getPnr())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: This person number is already registered!"));
        }

        // Create a new user account
        Person user = new Person(userData.getUsername(),
                userData.getEmail(),
                encoder.encode(userData.getPassword()));

        String strRoles = userData.getRole();
        Optional<Role> role = roleRepository.findByName(strRoles);
        user.setRole(role.get());
        user.setPnr(userData.getPnr());
        user.setName(userData.getName());
        user.setSurname(userData.getSurname());
        personRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    /**
     * Gets the role of the user.
     */
    @RequestMapping("/getRole")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //check token
        String principalString = auth.getPrincipal().toString();
        System.out.println(principalString);
        if(principalString.equals("anonymousUser")) {
            return "{\"role\" : \"none\"}";
        }
        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String role = authorities.stream().toList().get(0).toString();
        String result = "{ \"role\" : \"" + role + "\" }";
        return result;
    }

    /**
     * Sign out.
     */
    @RequestMapping("/signout")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/json")
    public String signout() {
        System.out.println("signout");
        SecurityContextHolder.clearContext();
        return "{\"status\" : \"ok\"}";
    }

}
