package com.rca.authorization.azureb2ctokenvalidator.cache;

import com.rca.authorization.azureb2ctokenvalidator.authorization.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class TokenCache {

    String AUDIENCE_ENV = "B2C_AUDIENCE";
    Map<String, TokenModel> verifiedTokens = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    Environment environment;

    public boolean containsToken(String token) {
        return verifiedTokens.containsKey(token);
    }

    public TokenModel get(String token) throws Exception {
        if (verifiedTokens.containsKey(token)) {
            TokenModel tokenModel = verifiedTokens.get(token);
            isExpired(System.currentTimeMillis(), tokenModel);
            return tokenModel;
        }
        throw new Exception("Token requested is not cached");
    }

    public void verifyAndPut(String token, TokenModel tokenModel) throws Exception {
        verifyJwtAttributes(tokenModel);
        verifiedTokens.put(token, tokenModel);
    }

    @Scheduled(fixedRate = 60000*1)
    public void cleanupTokenCache() {
        logger.info("Run Cleanup Job against tokenCount: " + verifiedTokens.size());
        Map<String, TokenModel> newTokenMap = new HashMap<>();
        Iterator<String> iterator = verifiedTokens.keySet().iterator();
        long currentTime = System.currentTimeMillis();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (!isExpired(currentTime/1000, verifiedTokens.get(key))) {
                newTokenMap.put(key, verifiedTokens.get(key));
            }
        }
        verifiedTokens = newTokenMap;
        logger.info("Post Cleanup Token Size: " + verifiedTokens.size());
    }

    private void verifyJwtAttributes(TokenModel tokenModel) throws Exception {
        long currentTime = System.currentTimeMillis() / 1000;
        if (tokenModel.getNotBefore() > currentTime) {
            throw new Exception("Not Before validation failed");
        }
        if (isExpired(currentTime, tokenModel)) {
            throw new Exception("Token is expired");
        }
        if (!environment.getProperty(AUDIENCE_ENV).equals(tokenModel.getAudience())) {
            throw new Exception("Audience validation failed");
        }
    }

    private boolean isExpired(long currentTimestamp, TokenModel tokenModel) {
        return tokenModel.getExpires() < currentTimestamp;
    }

}
