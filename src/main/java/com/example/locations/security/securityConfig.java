package com.example.locations.security;


import com.example.locations.security.jwt.AuthEntryPointJwt;
import com.example.locations.security.jwt.AuthTokenFilter;
import com.example.locations.security.jwt.JwtUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class securityConfig {
    private final DataSource dataSource;
    private final AuthEntryPointJwt authEntryPointJwt;

    public securityConfig(DataSource dataSource, AuthEntryPointJwt authEntryPointJwt) {
        this.dataSource = dataSource;
        this.authEntryPointJwt = authEntryPointJwt;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthTokenFilter authTokenFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/sign-in").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPointJwt))
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CommandLineRunner initUsers(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;

        return args -> {
            if (!manager.userExists("admin")) {
                manager.createUser(User.withUsername("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .roles("ADMIN")
                        .build());
            }

            if (!manager.userExists("user")) {
                manager.createUser(User.withUsername("user")
                        .password(passwordEncoder.encode("user123"))
                        .roles("USER")
                        .build());
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }

    @Bean
    public AuthTokenFilter authTokenFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }
}
