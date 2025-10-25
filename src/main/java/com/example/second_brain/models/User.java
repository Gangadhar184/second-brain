package com.example.second_brain.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class User extends BaseModel implements UserDetails {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    // one user can have many contents
    @OneToMany(mappedBy = "user", cascade =  CascadeType.ALL, orphanRemoval = true)
    private Set<Content> contents;

    //one user can have many links
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Link> links;

    /*
    User details interface methods for spring security to work with User
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //acc status methods

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Password never expires
    }

    @Override
    public boolean isEnabled() {
        return true;  // Account is always enabled
    }

}
