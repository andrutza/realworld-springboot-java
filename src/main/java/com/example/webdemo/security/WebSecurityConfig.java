package com.example.webdemo.security;

import com.example.webdemo.service.impl.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private FilterChainExceptionHandler filterChainExceptionHandler;

    public WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, FilterChainExceptionHandler filterChainExceptionHandler) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.filterChainExceptionHandler = filterChainExceptionHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SecurityConstants.USERS_URL,
                        SecurityConstants.USERS_URL + SecurityConstants.LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.TAGS_URL,
                        SecurityConstants.PROFILES_URL + "/{\\username+}",
                        SecurityConstants.ARTICLES_URL,
                        SecurityConstants.ARTICLES_URL + "/{\\slug+}",
                        SecurityConstants.ARTICLES_URL + "/{\\slug+}" + SecurityConstants.COMMENTS_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JWTAuthorizationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(filterChainExceptionHandler, JWTAuthorizationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> configurer = auth.userDetailsService(email -> userService.loadUserByUsername(email));
        configurer.passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("POST");
        config.addAllowedOrigin("http://localhost:8080");
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        source.registerCorsConfiguration("/api/**", corsConfiguration);

        return source;
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
