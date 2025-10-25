package com.example.second_brain.services;


import com.example.second_brain.models.User;
import com.example.second_brain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    //load user by username - required by spring security, this is done during authentication
    //user -> login -> spring security -> calls this methods -> we fetch user from database -> spring security compare passwords

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }

    //check if username already exists
    //used during registration to prevent duplicates

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    //check if email already exists, to prevent duplicate emails

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    //save new user to database

    public User saveUser(User user){
        return userRepository.save(user);
    }


}
