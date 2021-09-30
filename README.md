# spring-b2c-token-validator
Quick spring api to validate b2c tokens.  Allows me to stop re-implement token validation across multiple languages but rather centralizes this function via a REST Microservices.


Following run time environment attributes are optional and required
* optional attributes:
  * BASIC_SECURITY={key1:value1, key2: value2} - JSON string with key value pairs for BASIC authentication security.  If not defined, additional security is bypassed.
NOTE: future version will allow for multiple issued keys and secrets and likely will look at a more dynamic way of loading and setting them.
* required attributes:
  * JWKS_URL - jwks url for the openid definitions, under the .well-known config.
  * B2C_AUDIENCE - client id of the b2c client in the app registration.
