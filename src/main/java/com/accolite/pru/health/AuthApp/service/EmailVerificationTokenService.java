package com.accolite.pru.health.AuthApp.service;

import com.accolite.pru.health.AuthApp.exception.InvalidTokenRequestException;
import com.accolite.pru.health.AuthApp.model.TokenStatus;
import com.accolite.pru.health.AuthApp.model.User;
import com.accolite.pru.health.AuthApp.model.token.EmailVerificationToken;
import com.accolite.pru.health.AuthApp.repository.EmailVerificationTokenRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationTokenService {

    private static final Logger logger = Logger.getLogger(EmailVerificationTokenService.class);
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Value("${app.token.email.verification.duration}")
    private Long emailVerificationTokenExpiryDuration;

    @Autowired
    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }


    public void createVerificationToken(User user, String token) {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setToken(token);
        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        logger.info("Generated Email verification token [" + emailVerificationToken + "]");
        emailVerificationTokenRepository.save(emailVerificationToken);
    }


    public EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken) {
        existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
        existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
        logger.info("Updated Email verification token [" + existingToken + "]");
        return save(existingToken);
    }

    public Optional<EmailVerificationToken> findByToken(String token) {
        return emailVerificationTokenRepository.findByToken(token);
    }

    public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
        return emailVerificationTokenRepository.save(emailVerificationToken);
    }


    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public void verifyExpiration(EmailVerificationToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new InvalidTokenRequestException("Email Verification Token", token.getToken(), "Expired token. Please issue a new request");
        }
    }

}
