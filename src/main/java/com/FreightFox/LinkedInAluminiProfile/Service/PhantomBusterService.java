package com.FreightFox.LinkedInAluminiProfile.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PhantomBusterService {
    @Value("${my.app.api_key}")
    private String apiKey;

    @Value("${my.app.agent_id}")
    private String agentId;

    @Value("${my.app.identity_id}")
    private String identityId;

    @Value("${my.app.session_cookie}")
    private String sessionCookie;

    @Value("${my.app.user_agent}")
    private String userAgent;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.phantombuster.com/api/v2")
            .build();

    public Mono<String> launchSearchPhantom(String searchUrl) {
        Map<String, Object> argument = new HashMap<>();
        argument.put("searchType", "linkedInSearchUrl");
        argument.put("connectionDegreesToScrape", List.of("2", "3+"));
        argument.put("category", "People");
        argument.put("numberOfResultsPerLaunch", 100);
        argument.put("numberOfResultsPerSearch", 10);
        argument.put("numberOfLinesPerLaunch", 10);
        argument.put("enrichLeadsWithAdditionalInformation", true);
        argument.put("linkedInSearchUrl", searchUrl);
        argument.put("removeDuplicateProfiles", true);
        argument.put("csvName", "alumni_results");

        Map<String, Object> identity = new HashMap<>();
        identity.put("identityId", identityId);
        identity.put("sessionCookie", sessionCookie);
        identity.put("userAgent", userAgent);

        argument.put("identities", List.of(identity));

        Map<String, Object> body = Map.of(
                "id", agentId,
                "argument", argument
        );

        return webClient.post()
                .uri("/agents/launch")
                .header("x-phantombuster-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(), response -> {
                    log.error("PhantomBuster API error: {}", response.statusCode());
                    return response.createException();
                })
                .bodyToMono(String.class)
                .doOnError(e -> log.error("Error calling PhantomBuster API", e));
    }

}
