package com.example.second_brain.security;


import com.example.second_brain.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter
 * Intercepts every HTTP request and validates JWT token

 * Extends OncePerRequestFilter to ensure filter is executed only once per request
 */

@Component
@RequiredArgsConstructor
public  class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    // main filter method - executed for every request
    // extract jwt token from authorization header -> validate token -> load user details -> set authentication in secutrity context -> continue filter chain

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            // 1. extract jwt token from request header
            String jwt = parseJwt(request);

            //2. if token exists and is valid
            if(jwt != null) {
                //extract username from token
                String username = jwtUtil.extractUsername(jwt);

                //if user is not already authenticated
                if(username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
                    //3. load user details from database
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    //4 validate token
                    if(jwtUtil.validateToken(jwt, userDetails)) {
                        //create authentication token
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null, // no needed after auth
                                        userDetails.getAuthorities() //user roles and permissions
                                );

                        //set additional details
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );

                        //5 set authentication in security context
                        // this tells spring security "this user is authenticate
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }catch (Exception e) {
            //log the error but dont stop the request
            logger.error("Cannot set user authentication {}",e);
        }

        //6. continue with next filter in chain
        filterChain.doFilter(request, response);
    }

    /**extract jwt token from header
     **
     * Extract JWT token from Authorization header
     *
     * Header format: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     * We need to extract the token part after "Bearer "
     **/

    private String parseJwt(HttpServletRequest request) {
        //get header
        String headerAuth = request.getHeader("Authorization");
        //check if header exists and starts with "Bearer"
        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            //extract token (remove Bearer prefix)
            return headerAuth.substring(7);
        }
        return null;
    }
}
