package com.example.second_brain.controllers;


import com.example.second_brain.dtos.JwtResponse;
import com.example.second_brain.dtos.MessageResponse;
import com.example.second_brain.dtos.SignInRequest;
import com.example.second_brain.dtos.SignUpRequest;
import com.example.second_brain.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try{
            String message = authService.registerUser(signUpRequest);
            if(message.startsWith("Error")) {
                return ResponseEntity.badRequest().body(new MessageResponse((message)));

            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(message));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {
        try{
            //authenticate and get jwt token
            JwtResponse jwtResponse = authService.authenticateUser(signInRequest);
            return ResponseEntity.ok(jwtResponse);
        }catch (Exception e) {
            //authentication failed
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Error: Invalid username or password"));
        }
    }

    //signout
    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser() {
        try {
            String message = authService.signOutUser();
            return ResponseEntity.ok(new MessageResponse(message));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error: " + e.getMessage()));
        }
    }
}
