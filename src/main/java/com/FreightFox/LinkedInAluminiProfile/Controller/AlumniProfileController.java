package com.FreightFox.LinkedInAluminiProfile.Controller;

import com.FreightFox.LinkedInAluminiProfile.DTO.AlumniProfileSearchRequest;
import com.FreightFox.LinkedInAluminiProfile.DTO.PhantomWebhookPayload;
import com.FreightFox.LinkedInAluminiProfile.Entity.AlumniProfile;
import com.FreightFox.LinkedInAluminiProfile.Service.AlumniProfileService;
import com.FreightFox.LinkedInAluminiProfile.Service.PhantomBusterService;
import com.FreightFox.LinkedInAluminiProfile.Utils.MyUtilClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/alumni")
public class AlumniProfileController {

    private final PhantomBusterService phantomBusterService;
    private final AlumniProfileService alumniProfileService;

    @Autowired
    public AlumniProfileController(PhantomBusterService phantomBusterService, AlumniProfileService alumniProfileService) {
        this.phantomBusterService = phantomBusterService;
        this.alumniProfileService = alumniProfileService;
    }

    @PostMapping("/search")
    public Mono<ResponseEntity<String>> launchPhantom(@RequestBody AlumniProfileSearchRequest request) {
        String searchUrl = MyUtilClass.buildSearchUrl(request.getUniversity(), request.getDesignation(), request.getPassoutYear());
        log.info("Sending launch request to Phantom Linkedin Search Export Agent with URL: {}", searchUrl);

        return phantomBusterService.launchSearchPhantom(searchUrl)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error launching PhantomBuster: {}", e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}"));
                });
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> handlePhantomWebhook(@RequestBody PhantomWebhookPayload payload) {
        try {
            log.info("Webhook received for agentId: {}", payload.getAgentId());
            alumniProfileService.saveAlumniFromWebhookResult(payload.getResultObject());
            log.info("saved the alumni details successfully");
            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            log.error("Error processing webhook payload", e);
            return ResponseEntity.status(500).body("Error processing data");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAlumni() {
        log.info("fetching alumni profiles");
        try {
            List<AlumniProfile> alumni = alumniProfileService.getAllAlumniProfiles();
            log.info("fetched alumni profiles");
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", alumni
            ));
        }catch(Exception e) {
            log.error("Error fetching alumni profiles \n",e);
            return ResponseEntity.notFound().build();
        }


    }



}
