package uz.davrbank.educontrolservice.service.cases.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import uz.davrbank.educontrolservice.aggregate.dto.employee.EmployeeResponseDTO;
import uz.davrbank.educontrolservice.exception.condition.DepartmentIdNotFoundException;
import uz.davrbank.educontrolservice.feign.EmployeeClientFeign;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtTokenService {

    private final EmployeeClientFeign employeeClient;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public JwtTokenService(EmployeeClientFeign employeeClient, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.employeeClient = employeeClient;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @SuppressWarnings("unchecked")
    public  Map<String, Object> getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            log.error("Пользователь не аутентифицирован!");
            return Collections.emptyMap();
        }

        String username = jwt.getClaim("preferred_username");
        String email = jwt.getClaim("email");
        String id = jwt.getClaim("sub");

        // Extract roles from realm_access.roles
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");

        List<String> roles = new ArrayList<>();

        if (realmAccess != null && realmAccess.get("roles") instanceof List<?>) {
            roles = (List<String>) realmAccess.get("roles");
        }

        return Map.of(
                "username", username,
                "email", email,
                "id", id,
                "roles", roles
        );
    }


    public  String getCurrentUsername() {
        return (String) getCurrentUserInfo().get("username");
    }

    public String getCurrentAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            log.warn("Пользователь не аутентифицирован или токен недоступен.");
            return null;
        }

        return jwt.getTokenValue(); // Это и есть сам JWT токен
    }


    public  String getCurrentUserId() {
        return (String) getCurrentUserInfo().get("id");
    }

    @SuppressWarnings("unchecked")
    public  List<String> roles() {
        return (List<String>) getCurrentUserInfo().getOrDefault("roles", Collections.emptyList());
    }

    /**
     * Extracts the 'department_id' from redis.
     */

    public Long getDepartmentId() {
        // 1. Get User UUID from Token
        String userId = getCurrentUserId();

        // 2. Check Cache
        String cacheKey = "user_dept:" + userId;
        String cachedId = redisTemplate.opsForValue().get(cacheKey);

        if (cachedId != null) {
            return Long.valueOf(cachedId);
        }

        // 3. Fetch from Source (Employee Service)
        try {
            log.warn("Cache miss for user {}. Fetching from Employee Service.", userId);

            Object empObject = employeeClient.getOneEmployee(userId);
            objectMapper.writeValueAsString(empObject);

            EmployeeResponseDTO response =
                    objectMapper.readValue(
                            objectMapper.writeValueAsString(empObject),
                            EmployeeResponseDTO.class);
            if (Objects.nonNull(response)) {
                Long deptId = response.departmentId();

                // 4. Cache it (Valid for 1 hour)
                redisTemplate.opsForValue().set(cacheKey, String.valueOf(deptId), 1, TimeUnit.HOURS);
                return deptId;
            }
        } catch (Exception e) {
            log.error("Could not fetch department for user {}", userId);
            throw new DepartmentIdNotFoundException("Department id with %s user is not found".formatted(userId));
        }

        return null;
    }

}
