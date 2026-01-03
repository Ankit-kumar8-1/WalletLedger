package in.ankitsaahariya.WalletLedger.config;

import in.ankitsaahariya.WalletLedger.security.JwtAuthenticationFilter;
import in.ankitsaahariya.WalletLedger.services.AppUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AppUserDetailsService appUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

//    ye method btata  hai kon se request access kar sakte hai or kon se nahi
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity.cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/status","/health","/register","/activate","/login").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class
            );

    return httpSecurity.build();
}

// ye password ko encoder karta hai kyoki plan password db m save nahi karte hai
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * Yeh method application ke liye CORS rules define karta hai.
     *
     * Iska kaam browser wale frontend ko backend APIs access
     * karne ki permission dena hai jab dono alag origin par ho.
     *
     * Is configuration me:
     * - Koi bhi origin se request allow hai
     * - Common HTTP methods allowed hain (GET, POST, PUT, DELETE, OPTIONS)
     * - Important headers allow hain jaise Authorization aur Content-Type
     * - Tokens ya credentials bhejne ki permission hai
     * - Yeh rules saare endpoints par apply hote hain
     *
     * Iska use CORS error se bachne ke liye hota hai
     * jab frontend aur backend alag port ya domain par chal rahe ho.
     */
    @Bean
    public CorsConfigurationSource corsConfiguration(){
        CorsConfiguration configuration =  new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type","Accept"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }
}
