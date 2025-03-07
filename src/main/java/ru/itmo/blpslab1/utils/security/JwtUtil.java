package ru.itmo.blpslab1.utils.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.blpslab1.config.JwtProperties;
import ru.itmo.blpslab1.domain.enums.UserAuthority;
import ru.itmo.blpslab1.security.entity.JwtUserDetails;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtUtil {

    public static String createToken(UserDetails userDetails, JwtProperties jwtProperties) {
        Claims claims = Jwts.claims();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", String.join(",", roles));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getLifeTime()))
                .signWith(getSigningKey(jwtProperties), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Either<JwtException, UserDetails> verifyToken(String token, JwtProperties jwtProperties) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(jwtProperties))
                    .build()
                    .parseClaimsJws(token);

            JwtUserDetails userDetails = new JwtUserDetails();
            String rolesString = claims.getBody().get("roles", String.class);
            Set<GrantedAuthority> roles = stringToRoles(rolesString);

            userDetails.setUserName(claims.getBody().getSubject());
            userDetails.setRoles(roles);

            return Either.right(userDetails);
        } catch (Exception e) {
            return Either.left(new JwtException(e.getMessage()));
        }
    }

    private static Key getSigningKey(JwtProperties jwtProperties) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private static Set<GrantedAuthority> stringToRoles(String roleString) {
        if (roleString == null) return new HashSet<>();
        return Arrays.stream(roleString.split(","))
                .map(UserAuthority::valueOf).collect(Collectors.toSet());
    }
}
