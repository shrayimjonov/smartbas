package davr.uzcard.config;

import davr.uzcard.config.model.AuthDto;
import davr.uzcard.config.model.UserConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserConfigProperties userConfigProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests(
                        z ->
                                z.antMatchers("/adm/**")
                                        .authenticated()
//                                        .antMatchers("/adm/**")
//                                        .hasIpAddress("127.0.0.1")
                                        .anyRequest()
                                        .permitAll()


                )
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<String> users = userConfigProperties.getCredentials();

        Collection<UserDetails> userDetails = new LinkedList<>();

        users.forEach(u -> {
            AuthDto auth = getAuth.apply(u);
            userDetails.add(User.builder()
                    .username(auth.getUsername())
                    .password(auth.getPassword())
                    .roles("ADMIN")
                    .build());
        });
        return new InMemoryUserDetailsManager(userDetails);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }


    private Function<String, AuthDto> getAuth = (string) -> new AuthDto(string.split(":")[0], string.split(":")[1]);


}
