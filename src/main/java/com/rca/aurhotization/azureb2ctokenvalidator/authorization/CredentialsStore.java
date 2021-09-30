package com.rca.aurhotization.azureb2ctokenvalidator.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rca.aurhotization.azureb2ctokenvalidator.utilities.Constants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class CredentialsStore {

    private Map<String, String> credentialsSet;

    @PostConstruct
    public void init() throws JsonProcessingException {
        if (System.getenv(Constants.ENV_BASIC_SECURITY) != null) {
            credentialsSet = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(System.getenv(Constants.ENV_BASIC_SECURITY));
            Iterator<String> iterator = node.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                credentialsSet.put(key, node.get(key).asText());
            }
        }
    }

    public boolean checkCredentials(String key, String value) {
        return credentialsSet.containsKey(key) && credentialsSet.get(key).equals(value);
    }
}
