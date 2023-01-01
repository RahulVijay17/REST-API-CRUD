package com.project.springboot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenprovider jwtTokenprovider;

    private UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenprovider jwtTokenprovider, UserDetailsService userDetailsService) {
        this.jwtTokenprovider = jwtTokenprovider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException
                                                                    , IOException {
        //get jwt token from http request
        String token = getTokenFromRequest(request);

        //validate token
        if (StringUtils.hasText(token) && jwtTokenprovider.validateToken(token)){
            //get username from token
            String username= jwtTokenprovider.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,null,userDetails.getAuthorities()
            );
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request,response);
    }

    private String getTokenFromRequest(HttpServletRequest httpServletRequest){
        String bearerToken = httpServletRequest.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken)&& bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7,bearerToken.length());
        }
        return null;
    }
}
