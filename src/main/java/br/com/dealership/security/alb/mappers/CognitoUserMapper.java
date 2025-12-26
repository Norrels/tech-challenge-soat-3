package br.com.dealership.security.alb.mappers;

import br.com.dealership.security.alb.dto.AuthenticatedUser;
import org.springframework.security.oauth2.jwt.Jwt;

public class CognitoUserMapper {
    public static AuthenticatedUser from(Jwt jwt) {
        return new AuthenticatedUser(
            jwt.getSubject(),
            jwt.getClaim("email"),
            jwt.getClaim("cognito:username"),
            jwt.getClaimAsStringList("cognito:groups")
        );
    }
}