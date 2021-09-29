# spring-b2c-token-validator
Quick spring api to validate b2c tokens.  Allows me to stop re-implement token validation across multiple languages but rather centralizes this function via a REST Microservices.


Following run time environment attributes are optional and required
* optional attributes:
  * AUTH_KEY_KEY={value} where value is expected api key to look for i.e. header key
  * AUTH_KEY_SECRET={value} the secret that must be passed as the header key value.
NOTE: future version will allow for multiple issued keys and secrets and likely will look at a more dynamic way of loading and setting them.
* required attributes:
  * JWKS_URL - jwks url for the openid definitions, under the .well-known config.
  * B2C_AUDIENCE - client id of the b2c client in the app registration.
