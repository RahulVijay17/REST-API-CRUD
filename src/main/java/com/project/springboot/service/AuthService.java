package com.project.springboot.service;

import com.project.springboot.dto.LoginDto;
import com.project.springboot.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(RegisterDto registerDto);

}
