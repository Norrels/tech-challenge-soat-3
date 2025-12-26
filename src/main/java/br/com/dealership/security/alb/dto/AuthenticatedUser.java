package br.com.dealership.security.alb.dto;

import java.util.List;

public record AuthenticatedUser(
    String id,
    String email,
    String username,
    List<String> roles
) {}