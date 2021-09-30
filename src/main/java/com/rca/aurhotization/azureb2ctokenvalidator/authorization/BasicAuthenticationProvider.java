package com.rca.aurhotization.azureb2ctokenvalidator.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.ArrayList;

public class BasicAuthenticationProvider implements AuthenticationProvider {

    private final CredentialsStore credentialsStore;

    public BasicAuthenticationProvider(CredentialsStore credentialsStore) {
        this.credentialsStore = credentialsStore;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String key = authentication.getPrincipal().toString();
        String value = authentication.getCredentials().toString();

        if (credentialsStore.checkCredentials(key, value)) {
            return new UsernamePasswordAuthenticationToken(
                    authentication.getName(), authentication.getCredentials(), new ArrayList<>());
        }

        return null;
    }
}
