package com.sobrevidas.crud_pacientes.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/token")
public class TokenController {

    @Operation(summary = "Faz a requisição de token para o keycloak e retorna um token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solicitação de token feita com sucesso"),
            @ApiResponse(responseCode = "401", description = "Conexão com o keycloak foi recusada")
    })
    @PostMapping("/")
    public ResponseEntity<String> token(@RequestBody User user) {

        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", user.clientId);
        formData.add("username", user.username);
        formData.add("password", user.password);
        formData.add("grant_type", user.grantType);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        var result = rt.postForEntity("http://localhost:8081/realms/sobrevidas/protocol/openid-connect/token", entity, String.class);

        return result;
    }

    public record User(String clientId, String username, String password, String grantType) {}
}