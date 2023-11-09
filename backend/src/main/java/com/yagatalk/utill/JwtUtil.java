package com.yagatalk.utill;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JwtUtil {

    public static boolean hasRoleAuthor(Jwt jwt) {
        return hasRole(jwt, "author");
    }

    public static boolean hasRoleAdmin(Jwt jwt) {
        return hasRole(jwt, "admin");
    }

    private static boolean hasRole(Jwt jwt, String role) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> realmRoles = (List<String>) realmAccess.get("roles");
        return realmRoles.contains(role);
    }

    public static UUID getAuthorId(Jwt jwt) {
        return UUID.fromString(jwt.getClaim("sub"));
    }

}
