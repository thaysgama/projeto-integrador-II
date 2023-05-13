package com.br.digitalmoneyhouse.token.service;


import com.br.digitalmoneyhouse.token.model.RefreshTokenEntity;

public interface RefreshTokenService {
    RefreshTokenEntity getRefreshToken(String token);
    RefreshTokenEntity saveRefreshToken(RefreshTokenEntity refreshToken);
    void deleteToken(String token);
}
