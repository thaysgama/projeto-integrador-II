package com.br.digitalmoneyhouse.token.service;

import com.br.digitalmoneyhouse.core.exceptions.NotFoundException;
import com.br.digitalmoneyhouse.token.model.RefreshTokenEntity;
import com.br.digitalmoneyhouse.token.model.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Override
    public RefreshTokenEntity getRefreshToken(String token) {
        Optional<RefreshTokenEntity> refreshToken = repository.findByValue(token);
        if (refreshToken.isEmpty()) {
            log.error("Token not found [" + token + "].");
            throw new NotFoundException("Token [" + token + "] not found.");
        } else {
            log.info("Loading token [" + token + "].");
            return refreshToken.get();
        }
    }

    @Override
    public RefreshTokenEntity saveRefreshToken(RefreshTokenEntity refreshToken) {
        return repository.save(refreshToken);
    }

    @Override
    public void deleteToken(String token) {
        Optional<RefreshTokenEntity> refreshToken = repository.findByValue(token);
        if (!token.isEmpty()) {
            log.error("Token revoked for user [" + refreshToken.get().getUserId() + "].");
            repository.deleteById(refreshToken.get().getUserId());
        }
    }
}
