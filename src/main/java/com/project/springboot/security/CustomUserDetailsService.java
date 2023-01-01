package com.project.springboot.security;

import com.project.springboot.entity.User;
import com.project.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {
      User user=  userRepository.findByUsernameOrEmail(userNameOrEmail,userNameOrEmail)
                .orElseThrow(()->new UsernameNotFoundException("user not found with username or email: " + userNameOrEmail));

        Set<GrantedAuthority> grantedAuthorities=user.getRoles()
                .stream()
                .map((role)->new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),grantedAuthorities);
    }
}
