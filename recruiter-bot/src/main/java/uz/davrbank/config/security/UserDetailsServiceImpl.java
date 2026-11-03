package uz.davrbank.config.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.*;
import lombok.RequiredArgsConstructor;
import uz.davrbank.entity.User;
import uz.davrbank.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (u.getRoles() != null && !u.getRoles().isBlank()) {
            for (String r : u.getRoles().split(",")) {
                authorities.add(new SimpleGrantedAuthority(r.trim()));
            }
        }
        return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), authorities);
    }
}
