package com.example.sweater2.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    private String email;
    private String activationCode;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    //@CollectionTable -  this annotation will create new table (user_role) in our db
    //and will connect with our table (usr) by user_id column
    @CollectionTable(name="user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    public boolean isAdmin() {
        return roles.contains(Role.ADMIN);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }
}
