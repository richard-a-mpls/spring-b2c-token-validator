package com.rca.authorization.azureb2ctokenvalidator.controllers;

import com.rca.authorization.azureb2ctokenvalidator.authorization.AuthorizeJwk;
import com.rca.authorization.azureb2ctokenvalidator.authorization.TokenModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenValidatorTest {

    @Mock
    AuthorizeJwk authorizeB2CToken;

    @InjectMocks
    TokenValidator tokenValidator;

    @Test
    public void validateTestFail() {
        try {
            when(authorizeB2CToken.authorizeToken(anyString())).thenThrow(new Exception());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEntity<TokenModel> responseEntity = tokenValidator.validate("DOESNT MATTER WHAT I SEND");
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void validateTestSuccess() {
        try {
            when(authorizeB2CToken.authorizeToken(anyString())).thenReturn(new TokenModel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResponseEntity<TokenModel> responseEntity = tokenValidator.validate("DOESNT MATTER WHAT I SEND");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }


}