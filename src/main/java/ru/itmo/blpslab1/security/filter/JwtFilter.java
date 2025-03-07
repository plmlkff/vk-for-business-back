package ru.itmo.blpslab1.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itmo.blpslab1.config.JwtProperties;
import ru.itmo.blpslab1.utils.security.JwtUtil;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String bearerToken = getBearer(request);

        if (bearerToken != null) {
            var userDetailsRes = JwtUtil.verifyToken(bearerToken, jwtProperties);

            if (userDetailsRes.isRight()) {
                var userDetails = userDetailsRes.get();
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);

    }

    private String getBearer(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");

        if (bearer == null || !bearer.startsWith("Bearer ")) return null;

        return bearer.substring(7);
    }
}
