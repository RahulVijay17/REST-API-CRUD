package com.project.springboot.controller;

import com.project.springboot.dto.JwtAuthResponseDto;
import com.project.springboot.dto.LoginDto;
import com.project.springboot.dto.RegisterDto;
import com.project.springboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"login","/signin"})
    public ResponseEntity<JwtAuthResponseDto> login (@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JwtAuthResponseDto jwtAuthResponseDto= new JwtAuthResponseDto();
        jwtAuthResponseDto.setAccessToken(token);
        return ResponseEntity.ok(jwtAuthResponseDto);
    }

    @PostMapping(value = {"/register","/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
       String  response = authService.register(registerDto);
       return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
