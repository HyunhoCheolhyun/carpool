package com.criminals.plusExponential.infrastructure.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomLoginSuccessHandler customLoginSuccessHandler;


    public DefaultSecurityConfig(CustomUserDetailsService userDetailsService, CustomLoginSuccessHandler customLoginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.customLoginSuccessHandler = customLoginSuccessHandler;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/passenger").permitAll()
                        .requestMatchers("/unmatched-path").hasRole("PASSENGER")
                        .requestMatchers("/html/**").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/user/signup").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable());

        http.addFilterBefore(
                customAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))),
                UsernamePasswordAuthenticationFilter.class
        );


        return http.build();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager);
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        customAuthenticationFilter.setAuthenticationSuccessHandler(customLoginSuccessHandler);
        customAuthenticationFilter.afterPropertiesSet();
        return customAuthenticationFilter;
    }
}
