//package uz.davrbank.yeisvoservice.aggregate.config.security;
//
//import lombok.extern.slf4j.Slf4j;
//import uz.davrbank.yeisvoservice.aggregate.prop.AuthProp;
//import lombok.RequiredArgsConstructor;
////import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//@Slf4j
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final AuthProp authProp;
//
////    @Bean
////    public ModelMapper modelMapper() {
////        return new ModelMapper();
////    }
//
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        log.info("Request came to filterChain {}" , http);
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authz -> authz
////                        .antMatchers("/api/ping").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());
//
//        return http.build();
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth, PasswordEncoder passwordEncoder) throws Exception {
//        log.info("Configuring authentication of {}", auth);
//        for (AuthProp.User user : authProp.getUsers()) {
//            auth.inMemoryAuthentication()
//                    .withUser(user.getUsername())
//                    .password(passwordEncoder.encode(user.getPassword()))
//                    .roles(user.getRole());
//        }
//    }
//
//
//}