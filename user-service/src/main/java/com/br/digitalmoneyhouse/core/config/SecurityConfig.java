package com.br.digitalmoneyhouse.core.config;

import com.br.digitalmoneyhouse.core.security.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final String[] authWhitelist = new String[]{
            // -- Swagger
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/api/v1/user/refresh/token",
            "/api/v1/user/logout",
            "/api/v1/user/confirmEmail/**",
            "/api/v1/user/recoveruser/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
         http.csrf().disable()
                 .authorizeHttpRequests()
                 .requestMatchers(authWhitelist).permitAll()
                 .requestMatchers(HttpMethod.POST, "/api/v1/user/login", "/api/v1/user").permitAll()
                 .anyRequest().authenticated()
                 .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

         http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

         http.exceptionHandling().authenticationEntryPoint(
                (request, response, ex) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            ex.getMessage()
                    );
                }
         );
        return http.build();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
