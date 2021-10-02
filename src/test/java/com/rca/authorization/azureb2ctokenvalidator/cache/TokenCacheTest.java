package com.rca.authorization.azureb2ctokenvalidator.cache;

import com.rca.authorization.azureb2ctokenvalidator.authorization.TokenModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.env.Environment;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TokenCacheTest {

    @InjectMocks
    TokenCache tokenCache;

    @Mock
    Environment environmentMock;

    TokenModel tokenModel = new TokenModel();
    long currentTime = 0;

    @Before
    public void setupTokenModel() {
        currentTime = System.currentTimeMillis()/1000;
        tokenModel.setAudience("1234");
        tokenModel.setExpires(currentTime + 10);
        tokenModel.setNotBefore(currentTime - 10);
    }

    @Test
    public void containsToken() {
        assertFalse(tokenCache.containsToken("not in the cache"));
        when(environmentMock.getProperty(anyString())).thenReturn("1234");
        try {
            tokenCache.verifyAndPut("aaaa", tokenModel);
            assertTrue(tokenCache.containsToken("aaaa"));
        } catch (Exception e) {
            fail("fail to insert legitimate token into token cache: " + e.getMessage());
        }
    }

    @Test
    public void get() {
        when(environmentMock.getProperty(anyString())).thenReturn("1234");
        try {
            tokenCache.verifyAndPut("aaaa", tokenModel);
        } catch (Exception e) {
            fail("unable to add token to cache");
        }

        try {
            assertNotNull(tokenCache.get("aaaa"));
            tokenCache.get("not there");
            fail("Token is not there so we should get exception");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void verifyAndPut() {
        when(environmentMock.getProperty(anyString())).thenReturn("1234");
        try {
            tokenCache.verifyAndPut("aaaa", tokenModel);
        } catch (Exception e) {
            fail("this should verify success");
        }
        try {
            tokenModel.setAudience("wrong audience"); // makes not before be too far in future
            tokenCache.verifyAndPut("aaaa", tokenModel);
        } catch (Exception e) {
            assertEquals("Audience validation failed", e.getMessage());
        }
        try {
            tokenModel.setExpires(currentTime-10); // makes not before be too far in future
            tokenCache.verifyAndPut("aaaa", tokenModel);
        } catch (Exception e) {
            assertEquals("Token is expired", e.getMessage());
        }
        try {
            tokenModel.setNotBefore(currentTime+10); // makes not before be too far in future
            tokenCache.verifyAndPut("aaaa", tokenModel);
        } catch (Exception e) {
            assertEquals("Not Before validation failed", e.getMessage());
        }
    }

    @Test
    public void cleanupTokenCache() {
        when(environmentMock.getProperty(anyString())).thenReturn("1234");
        try {
            tokenCache.verifyAndPut("aaaa", tokenModel);
            TokenModel tokenModelExpires = new TokenModel();
            tokenModelExpires.setNotBefore(tokenModel.getNotBefore());
            tokenModelExpires.setAudience(tokenModel.getAudience());
            tokenModelExpires.setExpires(currentTime+1);
            tokenCache.verifyAndPut("bbbb", tokenModelExpires);
            Thread.sleep(2000);
            tokenCache.cleanupTokenCache();
            assertTrue(tokenCache.containsToken("aaaa"));
            assertFalse(tokenCache.containsToken("bbbb"));
        } catch (Exception e) {
            fail("Should not except here");
        }
    }
}