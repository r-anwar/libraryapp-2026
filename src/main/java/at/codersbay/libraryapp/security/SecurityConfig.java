package at.codersbay.libraryapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                // Swagger UI public
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs"
                ).permitAll()
                // Public: user registration
                .antMatchers(HttpMethod.POST, "/api/user").permitAll()
                // Public: read books
                .antMatchers(HttpMethod.GET, "/api/book", "/api/book/borrow").permitAll()
                // Public: read users
                .antMatchers(HttpMethod.GET, "/api/user").permitAll()
                // Authenticated: borrow / return
                .antMatchers(HttpMethod.POST, "/api/book/borrow").authenticated()
                .antMatchers(HttpMethod.PATCH, "/api/book/borrow").authenticated()
                // Admin only: manage books
                .antMatchers(HttpMethod.POST, "/api/book").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/api/book").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/book").hasRole("ADMIN")
                // Admin only: manage users
                .antMatchers(HttpMethod.PUT, "/api/user").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/user").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
