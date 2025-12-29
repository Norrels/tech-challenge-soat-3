package br.com.dealership.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserService {

    public AuthenticatedUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new IllegalStateException("Invalid authentication token");
        }

        String name = jwt.getClaimAsString("name");
        String cpf = jwt.getClaimAsString("custom:cpf");

        if (name == null || name.isBlank()) {
            throw new IllegalStateException("User name not found in token");
        }

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalStateException("User CPF not found in token");
        }

        return new AuthenticatedUser(name, cpf);
    }

    public record AuthenticatedUser(String name, String cpf) {}
}