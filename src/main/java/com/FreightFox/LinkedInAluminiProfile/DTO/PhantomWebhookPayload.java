package com.FreightFox.LinkedInAluminiProfile.DTO;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhantomWebhookPayload {

    private String agentId;
    private String agentName;
    private String containerId;
    private String script;
    private String scriptOrg;
    private String branch;
    private long launchDuration;
    private long runDuration;
    private String exitMessage;
    private int exitCode;
    private JsonNode resultObject;

}
