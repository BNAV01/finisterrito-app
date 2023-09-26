package com.ingsoftware.api.security;


import com.ingsoftware.api.exceptions.JwtResponseException;
import com.ingsoftware.api.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

//Clase que se encarga de generar el token y que sea inyectable en cualquier parte del proyecto
@Service
public class TokenUtils {
    @Value("${app.jwt.secret_token}")
    public String SECRET_KEY;
    @Value("${app.jwt.access_token_validity_in_minutes}")
    public int ACCESS_TOKEN_VALIDITY_SECOND = 0;
    @Value("${app.jwt.issuedBy}")
    public String ISSUED_BY;

    public TokenUtils() {}

    public String createToken(User user){

        long expirationTime = (long) ACCESS_TOKEN_VALIDITY_SECOND * 60 * 1000;

        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        Map<String, Object> extraClaims = new HashMap<String, Object>();
        extraClaims.put("firstName", user.getFirstname());
        extraClaims.put("lastName", user.getLastname());
        extraClaims.put("middleName", user.getMiddlename());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("userId", user.getId());
        extraClaims.put("isActive", user.isActive());
        extraClaims.put("isLockout", user.isLockout());
        extraClaims.put("phoneNumber", user.getPhoneNumber());
        extraClaims.put("authorities", user.getAuthorities());



        var token = Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(expirationDate)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(ISSUED_BY)
                .addClaims(extraClaims)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
        return token;
    }


    public String createRefreshToken() {
        UUID token = UUID.randomUUID();
        return Base64.encodeBase64String(token.toString().getBytes());
    }

    public String extractEmail(String token) {
        try {
            var decryptedToken = Jwts.parserBuilder().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)).build().parseClaimsJws(token.trim());
            return decryptedToken.getBody().getSubject();
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
        }
        catch (JwtException e){
            return null;
        }
    }

    public Boolean validateJwtToken(String authToken) throws JwtResponseException {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(authToken);
        } catch (SignatureException e) {
            throw new JwtResponseException("Invalid JWT signature");
        } catch (MalformedJwtException e) {
            throw new JwtResponseException("Invalid JWT token");
        } catch (ExpiredJwtException e) {
            return false;
        } catch (UnsupportedJwtException e) {
            throw new JwtResponseException("JWT token is unsupported");
        } catch (IllegalArgumentException e) {
            throw new JwtResponseException("JWT claims string is empty");
        }
        return true;
    }

    public String refreshToken(Map<String, Object> claims, String sub) {
        long expirationTime = ACCESS_TOKEN_VALIDITY_SECOND * 60 * 1000;
        //long expirationTime = 60 * 1000;

        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        var token = Jwts.builder()
                .setSubject(sub)
                .setExpiration(expirationDate)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setIssuer(ISSUED_BY)
                .addClaims(claims)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
        return token;
    }

}
