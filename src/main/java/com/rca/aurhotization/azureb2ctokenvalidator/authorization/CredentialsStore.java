package com.rca.aurhotization.azureb2ctokenvalidator.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rca.aurhotization.azureb2ctokenvalidator.utilities.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class CredentialsStore {

    @Autowired
    Environment environment;

    private final Map<String, String> credentialsSet = new HashMap<>();

    @PostConstruct
    public void init() throws JsonProcessingException {
        init(environment.getProperty(Constants.ENV_BASIC_SECURITY, "{}"));
    }

    public void init(String allowedCredentials) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(allowedCredentials);
        Iterator<String> iterator = node.fieldNames();
        while (iterator.hasNext()) {
            String key = iterator.next();
            credentialsSet.put(key, node.get(key).asText());
        }
    }

    public boolean checkCredentials(String key, String value) {
        return credentialsSet.containsKey(key) && credentialsSet.get(key).equals(value);
    }
}
