package com.project.springboot.config;

import com.project.springboot.security.JWTAuthenticationEntryPoint;
import com.project.springboot.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests((authorize)->
                        //authorize.anyRequest().authenticated()
                        authorize.requestMatchers(HttpMethod.GET,"/api/v1/**").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .anyRequest().authenticated()
                ).exceptionHandling(exception-> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                ).sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

/*    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails rahul = User.builder()
                .username("rahul")
                .password(passwordEncoder().encode("rahulvj17"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(rahul,admin);
    }*/
}
