package uz.davrbank.educontrolservice.aggregate.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * DTO для ответа с информацией о пользователе от Keycloak.
 * Содержит данные из userinfo endpoint OpenID Connect.
 *
 * @version 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("name")
    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("realm_access")
    private RealmAccess realmAccess;

    @JsonProperty("is_department")
    boolean isDepartment;

    @JsonProperty("departmentId")
    private Long departmentId;

    @JsonProperty("resource_access")
    private Map<String, ResourceAccess> resourceAccess;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RealmAccess {
        @JsonProperty("roles")
        private List<String> roles;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResourceAccess {
        @JsonProperty("roles")
        private List<String> roles;
    }
}

