package com.condigence.nsproductservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer {


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http.authorizeRequests().anyRequest().permitAll();
////        http.cors().and().csrf().disable();
//        http.cors()
//                .and().csrf().disable()
//                .authorizeRequests()
//                .anyRequest().access("isAuthenticated() or hasIpAddress('127.0.0.1/24') or hasIpAddress('::1')")
//                .and().httpBasic().and().headers().referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.ORIGIN);
//    }


    // THis also can be used
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // For now permit all requests (development convenience). Revert before production.
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // Keep httpBasic disabled for now since all is permitted
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
//        //configuration.setAllowedMethods(Arrays.asList("*"));
//       // configuration.setAllowedHeaders(Arrays.asList("*"));
//       // configuration.setAllowCredentials(true);
//
//
//
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
//       // configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
//       // configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
        configuration.addAllowedOriginPattern(CorsConfiguration.ALL);
        configuration.setAllowedMethods(List.of(CorsConfiguration.ALL));
       // configuration.addAllowedHeader();
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token","Access-Control-Allow-Origin"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/neerseva/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowCredentials(false).maxAge(3600);;
    }





}
