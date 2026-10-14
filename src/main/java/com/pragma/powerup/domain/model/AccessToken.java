package com.pragma.powerup.domain.model;

public record AccessToken(String accessToken, String tokenType, long expiresIn) {
}
