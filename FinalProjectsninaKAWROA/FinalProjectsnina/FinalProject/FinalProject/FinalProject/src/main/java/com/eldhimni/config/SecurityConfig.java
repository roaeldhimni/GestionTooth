package com.eldhimni.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.eldhimni.entity.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests()

                .requestMatchers("/", "/register","/student/addUtilisateurr", "/signin", "/saveUser","/forgotPassword","/newpassword","/resetpassword","/student/adduser","/home","/add","/addhabitude","/delete/{id}","/edit/{id}","/update/{id}","/admin/editUser/{id}").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN") // Permit access to /admin only for users with the role ADMIN
                .requestMatchers("/user/**").authenticated()
                .requestMatchers("/defis/**").authenticated()  
                .requestMatchers("/defis").authenticated()
                .requestMatchers("/bagdes/**").authenticated() 
                .requestMatchers("/add").authenticated()
                .requestMatchers("/admin/editUser/{id}").authenticated()
                .requestMatchers("/groupe/**").authenticated()  
                .requestMatchers("/student/addUtilisateurr").authenticated()  
                .requestMatchers("/student/editUser/{id}").authenticated()  
                .requestMatchers("/user/home").authenticated()  
                .requestMatchers("/snina/**").authenticated()  
                .requestMatchers("/pw/**").authenticated()  
                .requestMatchers("/student/**").authenticated()  
                .requestMatchers("/groupe/pws/{id}").authenticated()  
                .requestMatchers("/pw/download/{id}").authenticated()  
                .requestMatchers("/profile/update").authenticated()  
                .requestMatchers("/prof/profile/{id}").authenticated()








                .requestMatchers("/student/adduser").authenticated()  

                .requestMatchers("/student/**").authenticated()  



                .requestMatchers("/utilisateur-badges/**").authenticated()






                .and()
            .formLogin()
                .loginPage("/signin").loginProcessingUrl("/userLogin")
                .defaultSuccessUrl("/user/profile")
                .successHandler((request, response, authentication) -> {
                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                        if (auth.getAuthority().equals("ROLE_ADMIN")) {
                            response.sendRedirect("/admin");
                        } else {
                            response.sendRedirect("/home");
                        }
                    }
                })
                .permitAll();

        return http.build();}
   }
