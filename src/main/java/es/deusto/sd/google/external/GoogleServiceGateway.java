package es.deusto.sd.google.external;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

@Component
public class GoogleServiceGateway implements ILoginServiceGateway {
    private final String GOOGLE_API_URL = "http://localhost:8081";

    public boolean login(String email, String password) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = Map.of("email", email, "password", password);

        HttpHeaders headers = new HttpHeaders();

        headers.set("Content-Type", "application/json");

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, String>> response = restTemplate.exchange(GOOGLE_API_URL, HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<Map<String, String>>() {
                });

        if (response.getStatusCode() == HttpStatus.OK) {
            return true;
        } else {
            return false;
        }

    }
}
