package com.example.second_brain.services;

import com.example.second_brain.dtos.JwtResponse;
import com.example.second_brain.dtos.SignInRequest;
import com.example.second_brain.dtos.SignUpRequest;
import com.example.second_brain.models.User;
import com.example.second_brain.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    //register new user
    //check if username/email already exists
    //has the password using bcrypt
    //create user enitity
    //save to database

    public String registerUser(SignUpRequest signUpRequest) {
        if(userService.existsByUsername(signUpRequest.getUsername()))   {
            return "Error: Username is already taken!!";
        }
        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return "Error: Email is already in use!!";
        }

        //create a new user
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .build();

        userService.saveUser(user);

        return "User registered successfully";

    }

    //authenticate user and generate jwt token
    // authenticate username password -> if valid load user details -> generate jwt token -> return token with user info

    public JwtResponse authenticateUser(SignInRequest signInRequest) {
        //create authentication token with username and password
        //this is just the container not the jwt token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getUsername(),
                        signInRequest.getPassword()
                )
        );

        //set authentication in security context
        //this tells spring security that user is authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //load user details
        User user = (User) authentication.getPrincipal();

        //generate jwt token
        String jwt = jwtUtil.generateToken(user);

        //build and return response
        return JwtResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    //sign out user
    //jwt is stateless, so we cant invalidate tokens on server side, the client should delete the token from storage

    public String signOutUser() {
        SecurityContextHolder.clearContext();
        return "User signed out successfully";
    }

    public static class ContentService {
    }
}
