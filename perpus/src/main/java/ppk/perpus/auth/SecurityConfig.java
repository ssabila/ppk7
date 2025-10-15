package ppk.perpus.auth;

import jakarta.servlet.http.HttpServletResponse;
import ppk.perpus.service.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtTokenFilter;

    private final CustomUserDetail CustomUserDetails;

    public SecurityConfig(CustomUserDetail CustomUserDetails) {
        this.CustomUserDetails = CustomUserDetails;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(CustomUserDetails).passwordEncoder(passwordEncoder);
        return authManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Nonaktifkan CSRF
                .csrf(csrf -> csrf.disable())

                // Atur session agar stateless
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Konfigurasi otorisasi request - IMPORTANT: Order matters!
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints untuk authentication
                        .requestMatchers("/register", "/login").permitAll()

                        // Public endpoints untuk dokumentasi API (OpenAPI & Swagger)
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // Endpoint error
                        .requestMatchers("/error").permitAll()

                        // Semua endpoint lain harus terautentikasi
                        .anyRequest().authenticated()
                )

                // Tambahkan JWT filter SETELAH konfigurasi authorization
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}