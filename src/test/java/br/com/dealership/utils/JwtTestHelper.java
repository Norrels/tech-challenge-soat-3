package br.com.dealership.utils;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

public class JwtTestHelper {

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor createAdminJwt(String name, String cpf) {
        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(jwt -> jwt
                        .claim("name", name)
                        .claim("custom:cpf", cpf)
                        .claim("cognito:groups", java.util.List.of("Admin"))
                )
                .authorities(new SimpleGrantedAuthority("ROLE_Admin"));
    }

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor createAdminJwt() {
        return createAdminJwt("Test Admin", "12345678909");
    }

    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor createRegularUserJwt(String name, String cpf) {
        return SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(jwt -> jwt
                        .claim("name", name)
                        .claim("custom:cpf", cpf)
                );
    }
    
    public static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor createRegularUserJwt() {
        return createRegularUserJwt("Regular User", "98765432100");
    }
}
