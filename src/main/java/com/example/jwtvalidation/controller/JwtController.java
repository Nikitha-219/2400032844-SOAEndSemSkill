
package com.example.jwtvalidation.controller;

import com.example.jwtvalidation.dto.TokenRequest;
import com.example.jwtvalidation.dto.TokenResponse;
import com.example.jwtvalidation.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/jwt")
@Tag(name = "JWT Controller",
     description = "JWT generation, validation and claims extraction")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // Step 1: Generate JWT Token
    @PostMapping("/generate")
    @Operation(summary = "Generate JWT Token")
    public ResponseEntity<Map<String, String>> generateToken(
            @RequestParam(defaultValue = "101") String subject,
            @RequestParam(defaultValue = "student@example.com") String email,
            @RequestParam(defaultValue = "USER") String role,
            @RequestParam(defaultValue = "300") long validitySeconds) {

        String token = jwtService.generateDemoToken(
                subject, email, role, validitySeconds
        );

        return ResponseEntity.ok(Map.of("token", token));
    }

    // Step 2: Validate JWT and Extract Claims
    @PostMapping("/validate")
    @Operation(summary = "Validate JWT and Extract Claims")
    public ResponseEntity<TokenResponse> validateToken(
            @Valid @RequestBody TokenRequest request) {

        try {
            Map<String, Object> claims =
                    jwtService.validateAndExtractClaims(request.getToken());

            return ResponseEntity.ok(
                    new TokenResponse(
                            true,
                            "Token valid",
                            claims
                    )
            );

        } catch (ExpiredJwtException ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new TokenResponse(
                            false,
                            "Token expired",
                            null
                    )
            );

        } catch (JwtException | IllegalArgumentException ex) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new TokenResponse(
                            false,
                            "Invalid token or signature",
                            null
                    )
            );
        }
    }
}
