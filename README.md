# JWT Validation and Claims Extraction Practical

## Requirements
- JDK 17 or later
- Maven 3.8+
- Internet access on first build to download Maven dependencies

## Run in terminal
```bash
mvn spring-boot:run
```

Or import the folder into IntelliJ IDEA / Spring Tool Suite / Eclipse as an existing Maven project and run `JwtValidationApplication.java`.

## Swagger UI
Open: http://localhost:8080/swagger-ui/index.html

OpenAPI JSON: http://localhost:8080/v3/api-docs

## Practical steps

### 1. Generate a valid demo token
In Swagger, execute `POST /api/jwt/generate` with defaults. Copy the `token` from the response.

### 2. Validate the token
Execute `POST /api/jwt/validate` with this JSON:
```json
{
  "token": "PASTE_TOKEN_HERE"
}
```
Expected: HTTP 200 and extracted subject, email, role, issuedAt and expiration claims.

### 3. Test an altered token
Change one or more characters in the copied token and submit it to `/api/jwt/validate`.
Expected: HTTP 401, `Invalid token or signature`.

### 4. Test an expired token
Execute `POST /api/jwt/generate` with `validitySeconds=-10`, copy the returned token, and submit it to `/api/jwt/validate`.
Expected: HTTP 401, `Token expired`.

## How validation works
`JwtService.validateAndExtractClaims()` parses the signed JWT using the configured HMAC key. The parser verifies the signature and checks the `exp` claim. Only after parsing succeeds are the claims returned.

## Important
The `/api/jwt/generate` endpoint and the demo secret are included only to make the practical easy to demonstrate. Do not expose token generation publicly or use this demo secret in production. In a real application, issue tokens only after authenticating users and keep the signing key in a secure secret store or environment variable.
