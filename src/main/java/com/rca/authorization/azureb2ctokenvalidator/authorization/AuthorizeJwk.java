package com.rca.authorization.azureb2ctokenvalidator.authorization;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rca.authorization.azureb2ctokenvalidator.cache.TokenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

@Service
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class AuthorizeJwk {

    String JWK_URL = "JWK_URL";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    List<JwkProvider> jwkProviderList = new ArrayList<>();

    @Autowired
    TokenCache tokenCache;

    public TokenModel authorizeToken(String token) throws Exception {
        TokenModel tokenModel = null;
        if (tokenCache.containsToken(token)) {
            tokenModel = tokenCache.get(token);
            return tokenModel;
        }

        DecodedJWT decodedJWT = verifyJwtSignature(token);
        String jwtPayload = new String(Base64.getDecoder().decode(decodedJWT.getPayload()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            tokenModel = mapper.readValue(jwtPayload, TokenModel.class);
        } catch (JsonProcessingException e) {
            throw new Exception("unable to parse jwt payload");
        }
        tokenCache.verifyAndPut(token, tokenModel);
        return tokenModel;
    }

    public DecodedJWT verifyJwtSignature(String sourceJwt) throws Exception {
        DecodedJWT decodedJWT = JWT.decode(sourceJwt);
        URL jwkUrl = null;
        try {
            jwkUrl = new URL(System.getenv(JWK_URL));
        } catch (MalformedURLException e) {
            throw new Exception("unable to load jwk_url from env: " + System.getenv(JWK_URL));
        }

        Jwk jwk = getJwk(decodedJWT.getKeyId());
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
        algorithm.verify(decodedJWT);
        return decodedJWT;
    }

    private Jwk getJwk(String kid) throws Exception {
        URL jwkUrl = new URL(System.getenv(JWK_URL));
        if (jwkProviderList == null) {
            jwkProviderList = new ArrayList<>();
            jwkProviderList.add(new UrlJwkProvider(jwkUrl));
        }

        for (JwkProvider provider: jwkProviderList) {
            try {
                return provider.get(kid);
            } catch (JwkException e) {
                // not found yet
            }
        }
        // didn't find, try getting refreshed view
        UrlJwkProvider newProvider = new UrlJwkProvider(jwkUrl);
        try {
            Jwk jwk = newProvider.get(kid);
            logger.info("Adding new Jwk Provider");
            jwkProviderList.add(newProvider);
            if (jwkProviderList.size() >= 5) {
                jwkProviderList.remove(0); // remove oldest instance.
            }
            return jwk;
        } catch (JwkException e) {
            throw new Exception("Could not determine Jwk");
        }


    }
}
