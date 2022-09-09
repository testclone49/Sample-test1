
package com.accolite.pru.health.AuthApp.security;

import com.accolite.pru.health.AuthApp.model.CustomUserDetails;
import com.accolite.pru.health.AuthApp.model.Role;
import com.accolite.pru.health.AuthApp.model.RoleName;
import com.accolite.pru.health.AuthApp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtTokenProviderTest {

    private static final String jwtSecret = "testSecret";
    private static final long jwtExpiryInMs = 25000;

    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.tokenProvider = new JwtTokenProvider(jwtSecret, jwtExpiryInMs);
    }

    @Test
    void testGetUserIdFromJWT() {
        String token = tokenProvider.generateToken(stubCustomUser());
        assertEquals(100, tokenProvider.getUserIdFromJWT(token).longValue());
    }

    @Test
    void testGetTokenExpiryFromJWT() {
        String token = tokenProvider.generateTokenFromUserId(120L);
        assertNotNull(tokenProvider.getTokenExpiryFromJWT(token));
    }

    @Test
    void testGetExpiryDuration() {
        assertEquals(jwtExpiryInMs, tokenProvider.getExpiryDuration());
    }

    @Test
    void testGetAuthoritiesFromJWT() {
        String token = tokenProvider.generateToken(stubCustomUser());
        assertNotNull(tokenProvider.getAuthoritiesFromJWT(token));
    }

    private CustomUserDetails stubCustomUser() {
        User user = new User();
        user.setId((long) 100);
        user.setRoles(Collections.singleton(new Role(RoleName.ROLE_ADMIN)));
        return new CustomUserDetails(user);
    }
}
