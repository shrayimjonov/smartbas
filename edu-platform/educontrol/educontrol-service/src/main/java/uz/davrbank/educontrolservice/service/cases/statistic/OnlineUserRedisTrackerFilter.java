package uz.davrbank.educontrolservice.service.cases.statistic;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class OnlineUserRedisTrackerFilter extends OncePerRequestFilter {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public OnlineUserRedisTrackerFilter(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            Jwt jwt = jwtAuth.getToken();
            String userId = jwt.getSubject();
            String username = jwt.getClaim("preferred_username");

            String key = "online:" + userId;
            Instant now = Instant.now();

            Map<String, Object> data = new HashMap<>();

            // Если уже есть — прочитаем loginTime из Redis
            String existingJson = redisTemplate.opsForValue().get(key);
            if (existingJson != null) {
                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> existingData = objectMapper.readValue(existingJson, Map.class);
                    data.put("loginTime", existingData.getOrDefault("loginTime", now.toString()));
                } catch (Exception e) {
                    data.put("loginTime", now.toString());
                }
            } else {
                data.put("loginTime", now.toString());
            }

            data.put("username", username);
            data.put("lastActivity", now.toString());

            String json = objectMapper.writeValueAsString(data);
            redisTemplate.opsForValue().set(key, json, Duration.ofMinutes(10));
        }

        filterChain.doFilter(request, response);
    }

}
