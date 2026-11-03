package uz.davrbank.payment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import uz.davrbank.payment.model.property.UserConfigProperty;
import uz.nodir.commondto.model.dto.core.response.AuthDTO;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class AuthProvider {
    private final UserConfigProperty userConfigProperty;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<String> users = userConfigProperty.getCredentials();

        Collection<UserDetails> userDetails = new LinkedList<>();

        users.forEach(u -> {
            AuthDTO auth = getAuth.apply(u);
            userDetails.add(User.builder()
                    .username(auth.getUsername())
                    .password(auth.getPassword())
                    .roles(auth.getRole())
                    .build());
        });
        return new InMemoryUserDetailsManager(userDetails);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    private final Function<String, AuthDTO> getAuth =
            (string) -> new AuthDTO(
                    string.split(":")[0],
                    string.split(":")[1],
                    string.split(":")[2]);
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }




}
