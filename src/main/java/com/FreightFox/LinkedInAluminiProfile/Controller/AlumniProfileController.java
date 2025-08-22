package com.FreightFox.LinkedInAluminiProfile.Controller;

import com.FreightFox.LinkedInAluminiProfile.DTO.AlumniProfileSearchRequest;
import com.FreightFox.LinkedInAluminiProfile.DTO.PhantomWebhookPayload;
import com.FreightFox.LinkedInAluminiProfile.Service.PhantomBusterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/api/alumni")
public class AlumniProfileController {

    private final PhantomBusterService phantomBusterService;

    @Autowired
    public AlumniProfileController(PhantomBusterService phantomBusterService) {
        this.phantomBusterService = phantomBusterService;
    }

    @PostMapping("/search")
    public Mono<ResponseEntity<String>> launchPhantom(@RequestBody AlumniProfileSearchRequest request) {
        String searchUrl = buildSearchUrl(request.getUniversity(), request.getDesignation(), request.getPassoutYear());
        log.info("Sending launch request to Phantom Linkedin Search Export Agent with URL: {}", searchUrl);
        // Launch Phantom, return immediate launch response (do NOT poll for results here)
        return phantomBusterService.launchSearchPhantom(searchUrl)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error launching PhantomBuster: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}"));
                });
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> handlePhantomWebhook(@RequestBody PhantomWebhookPayload payload) {
        // Log received webhook message
        log.info("Webhook received for agentId {} with exitMessage '{}'", payload.getAgentId(), payload.getExitMessage());
        Object resultObject = payload.getResultObject();
        log.info("ResultObject Received Structure: \n{}", resultObject);
        //Later add the parsing logic and save the results in db
        return ResponseEntity.ok("Webhook processed successfully");
    }

    //utility function for building the search string
    private String buildSearchUrl(String university, String designation, Integer passoutYear) {
        if (university == null || university.isBlank())
            throw new IllegalArgumentException("University is required");
        if (designation == null || designation.isBlank())
            throw new IllegalArgumentException("Designation is required");

        StringBuilder sb = new StringBuilder();
        sb.append(university.trim()).append(" ").append(designation.trim());
        if (passoutYear != null) {
            sb.append(" ").append(passoutYear);
        }
        String encodedKeywords = java.net.URLEncoder.encode(sb.toString(), java.nio.charset.StandardCharsets.UTF_8);
        return "https://www.linkedin.com/search/results/people/?keywords=" + encodedKeywords + "&origin=GLOBAL_SEARCH_HEADER";
    }


}
