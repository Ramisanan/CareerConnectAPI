package org.example.grad_gateway2.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(HttpMethod.GET,"/api/company/**","/api/review/**","/api/jobPost/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/company/**","/api/jobPost/**").hasAnyAuthority("EMPLOYER","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/company/**","/api/jobPost/**").hasAnyAuthority("EMPLOYER","ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/company/**","/api/jobPost/**").hasAnyAuthority("EMPLOYER","ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/review/**").hasAnyAuthority("STUDENT","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/review/**").hasAnyAuthority("STUDENT","ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/review/**").hasAnyAuthority("STUDENT","ADMIN")
                                .requestMatchers("/api/auth/register").permitAll()
                                .requestMatchers("/api/auth/all","/api/auth/delete/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/auth/user/**","/api/userdata/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE,"/api/userdata/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/application/**").hasAnyAuthority("EMPLOYER","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/application/**").hasAnyAuthority("EMPLOYER","ADMIN")
                                .requestMatchers(HttpMethod.PUT,"/api/application/**").hasAnyAuthority("EMPLOYER","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/application/**").hasAnyAuthority("EMPLOYER","ADMIN","STUDENT")
                                .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
