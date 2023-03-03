package com.backend.recruitmentapp.security;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.*;

@Component
public class JwtUtils
{
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${recruitment.app.jwtSecret}")
    private String jwtSecret;

    @Value("${recruitment.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication)
    {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return generateJwtTokenWithUsername(
                userPrincipal.getUsername(),
                new Date(),
                new Date((new Date()).getTime() + jwtExpirationMs));
    }

    public String getUserNameFromJwtToken(String token)
    {
        return Jwts.parserBuilder().setSigningKey(jwtSecret).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    public String generateJwtTokenWithUsername(String username,
                                               Date issuedAt,
                                               Date expiration)
    {
        Claims claim= Jwts.claims().setSubject(username);
        claim.put("issuedAt", String.valueOf(issuedAt));
        claim.put("expiration",String.valueOf(expiration) );
        return Jwts.builder().setClaims(claim).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateJwtToken(String jwt)
    {
        try
        {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException | IllegalArgumentException e)
        {
            System.err.println(e.getMessage());
        }
        return false;
    }

//    String getSecret()
//        {
//        String secret = jwtSecret;
//        byte[] bytesEncoded = Base64.getEncoder().encode(secret.getBytes());
//        return jwtSecret;
//
//        }



}