# spring-jwk-token-validator
Quick spring api to validate jwk tokens.  Allows me to stop re-implement token validation across multiple languages but rather centralizes this function via a REST Microservices.

I've implemented this to use across a larger set of microservices ranging from SpringBoot, Python, Node and others to come as a larger sample projects taking on technologies in React, as well as those mentioned prior.  This was tested specifically against Azure B2C delivered tokens and has worked well there.

Additional security beyond the JWT is not necessary however I've built in the option to set ENV vars to require Basic Auth.  When configured, not only is the JWT validated but the Basic Auth is also enforced.

This project deploys to Azure App Services via GitHub Actions whenever a pull request is merged to main.

This may also be run locally by running the SpringBoot App directly.

Following run time environment attributes are optional and required
* optional attributes:
  * BASIC_SECURITY={key1:value1, key2: value2} - JSON string with key value pairs for BASIC authentication security.  If not defined, additional security is bypassed.
NOTE: future version will allow for multiple issued keys and secrets and likely will look at a more dynamic way of loading and setting them.
* required attributes:
  * JWKS_URL - jwks url for the openid definitions, under the .well-known config.
  * B2C_AUDIENCE - client id of the b2c client in the app registration.
