package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.exception.ResourceNotFoundException;
import com.accolite.pru.health.AuthApp.model.PasswordResetToken;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.payload.PasswordResetRequest;
import com.accolite.pru.health.AuthApp.repository.PasswordResetTokenRepository;
import com.accolite.pru.health.AuthApp.util.Util;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository repository;

    @Value("${app.token.password.reset.duration}")
    private Long expiration;

    @Autowired
    public PasswordResetTokenService(PasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    public PasswordResetToken getValidToken(PasswordResetRequest request) {
        String tokenID = request.getToken();
        PasswordResetToken token = repository.findByToken(tokenID)
                .orElseThrow(() -> new ResourceNotFoundException("Password Reset Token", "Token Id", tokenID));

        matchEmail(token, request.getEmail());
        verifyExpiration(token);
        return token;
    }

    public Optional<PasswordResetToken> createToken(User user) {
        PasswordResetToken token = createTokenWithUser(user);
        return Optional.of(repository.save(token));
    }

    public PasswordResetToken claimToken(PasswordResetToken token) {
        User user = token.getUser();
        token.setClaimed(true);

        CollectionUtils.emptyIfNull(repository.findActiveTokensForUser(user))
                .forEach(t -> t.setActive(false));

        return token;
    }


    void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("Password Reset Token", token.getToken(),
                    "Expired token. Please issue a new request");
        }
        if (!token.getActive()) {
            throw new InvalidTokenRequestException("Password Reset Token", token.getToken(),
                    "Token was marked inactive");
        }
    }

    void matchEmail(PasswordResetToken token, String requestEmail) {
        if (token.getUser().getEmail().compareToIgnoreCase(requestEmail) != 0) {
            throw new InvalidTokenRequestException("Password Reset Token", token.getToken(),
                    "Token is invalid for the given user " + requestEmail);
        }
    }

    PasswordResetToken createTokenWithUser(User user) {
        String tokenID = Util.generateRandomUuid();
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(tokenID);
        token.setExpiryDate(Instant.now().plusMillis(expiration));
        token.setClaimed(false);
        token.setActive(true);
        token.setUser(user);
        return token;
    }
}
