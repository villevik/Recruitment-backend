package com.backend.recruitmentapp.controller;

import com.backend.recruitmentapp.security.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    /**
     * Shows current token holder
     */
    @RequestMapping("/resource")
    public String home(@AuthenticationPrincipal UserDetailsImpl user) {

        if (user == null) {
            return "Access denied";
        }

        return user.getUsername() + " " + user.getEmail() + " " + user.getAuthorities().stream().toList().get(0);
    }


    /**
     * Shows current user
     */
    @GetMapping("/show")
    public String showUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        Object details = auth.getDetails();
        String s = principal + "\n\r" + details + "\n\r" + authorities.stream().toString();
        return s;
    }

    //Not working yet
    @GetMapping("/user")
    @PreAuthorize("hasRole('applicant') or hasRole('recruiter')")
    public String userAccess() {
        return "User Content.";
    }

    //Not working yet
    @GetMapping("/admin")
    @PreAuthorize("hasRole('recruiter')")
    public String adminAccess() {
        return "Admin Board.";
    }

}