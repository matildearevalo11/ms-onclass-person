package com.pragma.powerup;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import com.pragma.powerup.application.dto.response.TokenResponseDto;
import com.pragma.powerup.domain.exception.ExceptionMessages;
import com.pragma.powerup.application.handler.IAuthenticationHandler;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class AuthenticationIntegrationTest {
    @Autowired
    private DatabaseClient database;
    @Autowired
    private PasswordEncoder passwords;
    @Value("${jwt.secret}")
    private String secret;
    @MockitoSpyBean
    private IAuthenticationHandler handler;
    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        String hash = passwords.encode("test-password");
        StepVerifier.create(database.sql("DELETE FROM users").fetch().rowsUpdated()
                .then(database.sql("DELETE FROM roles").fetch().rowsUpdated())
                .then(database.sql("INSERT INTO roles (id, name) VALUES (1, 'ADMIN')").fetch().rowsUpdated())
                .then(database.sql("INSERT INTO users (id, email, password_hash, role_id) VALUES (1, 'admin@example.com', :hash, 1)")
                        .bind("hash", hash).fetch().rowsUpdated()).then()).verifyComplete();
    }

    @Test
    void servesStaticOpenApiThroughSwaggerUi() throws IOException {
        String contract = new ClassPathResource("openapi.yaml")
                .getContentAsString(StandardCharsets.UTF_8);
        client.get().uri("/openapi.yaml").exchange().expectStatus().isOk()
                .expectBody(String.class).isEqualTo(contract);
        client.get().uri("/swagger-ui.html").exchange().expectStatus().is3xxRedirection();
        client.get().uri("/webjars/swagger-ui/index.html").exchange().expectStatus().isOk();
        client.get().uri("/v3/api-docs/swagger-config").exchange().expectStatus().isOk()
                .expectBody().jsonPath("$.url").isEqualTo("/api/v1/openapi.yaml");
    }

    @Test
    void issuesSignedTokenUsingTheRoleStoredInTheDatabase() {
        var response = login(" ADMIN@example.com ", "test-password").expectStatus().isOk()
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectBody(TokenResponseDto.class).returnResult().getResponseBody();
        assertThat(response).isNotNull();
        var decoder = NimbusJwtDecoder.withSecretKey(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"))
                .macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("http://localhost:8081"));
        var jwt = decoder.decode(response.getAccessToken());
        assertThat(jwt.getSubject()).isEqualTo("1");
        assertThat(jwt.getClaimAsString("role")).isEqualTo("ADMIN");
        assertThat(jwt.getAudience()).containsExactly("on-class-technology");
        assertThat(response.getExpiresIn()).isEqualTo(900);
        assertThat(response.getTokenType()).isEqualTo("Bearer");

    }

    @Test
    void rejectsWrongPasswordWithoutIssuingAToken() {
        login("admin@example.com", "wrong").expectStatus().isUnauthorized().expectBody()
                .jsonPath("$.errors.message").isEqualTo(ExceptionMessages.INVALID_CREDENTIALS.getMessage())
                .jsonPath("$.accessToken").doesNotExist();
    }

    @Test
    void rejectsUnknownAccountWithoutDisclosingItsExistence() {
        login("unknown@example.com", "test-password").expectStatus().isUnauthorized().expectBody()
                .jsonPath("$.errors.message").isEqualTo(ExceptionMessages.INVALID_CREDENTIALS.getMessage());
    }

    @Test
    void rejectsMissingCredentialsBeforeCallingTheHandler() {
        login("", "").expectStatus().isBadRequest().expectBody()
                .jsonPath("$.errors.message").isEqualTo(ExceptionMessages.CREDENTIALS_REQUIRED.getMessage());
        verifyNoInteractions(handler);
    }

    private WebTestClient.ResponseSpec login(String email, String password) {
        return client.post().uri("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("email", email, "password", password)).exchange();
    }

}
