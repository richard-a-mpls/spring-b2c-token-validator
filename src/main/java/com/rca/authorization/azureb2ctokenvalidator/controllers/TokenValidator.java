package com.rca.authorization.azureb2ctokenvalidator.controllers;

import com.rca.authorization.azureb2ctokenvalidator.authorization.AuthorizeJwk;
import com.rca.authorization.azureb2ctokenvalidator.authorization.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenValidator {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AuthorizeJwk authorizeToken;

    @RequestMapping("/validate")
    public ResponseEntity<TokenModel> validate(@RequestHeader String tokenJwt) {

        TokenModel tokenModel = null;
        try {
            tokenModel = authorizeToken.authorizeToken(tokenJwt);
        } catch (Exception e) {
            logger.info("Error authorizing token: {}", e.getMessage());
            logger.trace("Error Details: ", e);
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(tokenModel, HttpStatus.OK);
    }
}