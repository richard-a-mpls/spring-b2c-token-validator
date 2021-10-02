package com.rca.authorization.azureb2ctokenvalidator.authorization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsStoreTest {

    String GOOD_JSON = "{\"app_key_1\":\"secret1\", \"app_key_2\": \"secret2\"}";

    @Test
    void testPostConstructInit() {
        CredentialsStore credentialsStore = new CredentialsStore();
        assertFalse(credentialsStore.checkCredentials("key", "value"));
    }

    @Test
    void testManualInitBadJson() {
        CredentialsStore credentialsStore = new CredentialsStore();
        try {
            credentialsStore.init("bad json text");
            fail("No exception caught on json parse");
        } catch (Exception e) {
            // success
        }
    }

    @Test
    void testManualInitNullJson() {
        CredentialsStore credentialsStore = new CredentialsStore();
        try {
            credentialsStore.init(null);
            fail("No exception caught on json parse");
        } catch (Exception e) {
            // success
        }
    }

    @Test
    void testManualInitGoodJson() {
        CredentialsStore credentialsStore = new CredentialsStore();
        try {
            credentialsStore.init(GOOD_JSON);
            // success
        } catch (Exception e) {
            fail("caught exception on good json");
        }
    }


    @Test
    void checkCredentialsNull() {
        CredentialsStore credentialsStore = new CredentialsStore();
        credentialsStore.checkCredentials("key", "value");
    }

    @Test
    void checkCredentialsTrue() {
        CredentialsStore credentialsStore = new CredentialsStore();
        try {
            credentialsStore.init(GOOD_JSON);
            assertTrue(credentialsStore.checkCredentials("app_key_1", "secret1"));
            assertTrue(credentialsStore.checkCredentials("app_key_1", "secret1"));
        } catch (Exception e) {
            fail("exception thrown checking good credentials");
        }
    }

    @Test
    void checkCredentialsFalse() {
        CredentialsStore credentialsStore = new CredentialsStore();
        try {
            credentialsStore.init(GOOD_JSON);
            assertFalse(credentialsStore.checkCredentials("app_key_1_BAD", "secret1_BAD"));
        } catch (Exception e) {
            fail("exception thrown checking bad credentials");
        }
    }


}