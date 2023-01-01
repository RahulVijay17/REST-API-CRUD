package com.project.springboot.serviceImpl;

import com.project.springboot.dto.LoginDto;
import com.project.springboot.dto.RegisterDto;
import com.project.springboot.entity.Roles;
import com.project.springboot.entity.User;
import com.project.springboot.exception.BlogAPIException;
import com.project.springboot.repository.RolesRepository;
import com.project.springboot.repository.UserRepository;
import com.project.springboot.security.JwtTokenprovider;
import com.project.springboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RolesRepository rolesRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenprovider jwtTokenprovider;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RolesRepository rolesRepository,
                           PasswordEncoder passwordEncoder,
                            JwtTokenprovider jwtTokenprovider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenprovider = jwtTokenprovider;
    }

    @Override
    public String login(LoginDto loginDto) {
       Authentication authenticate=
               authenticationManager.authenticate
                       (new UsernamePasswordAuthenticationToken
                               (loginDto.getUsernameOrEmail()
                                       ,loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtTokenprovider.generateToken(authenticate);
        return token;
    }


    @Override
    public String register(RegisterDto registerDto) {

        // add check for username exists in database
        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        // add check for email exists in database
        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Roles> roles = new HashSet<>();
        Roles userRole = rolesRepository.findByName("ROLE_USER").get();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!.";
    }
}
