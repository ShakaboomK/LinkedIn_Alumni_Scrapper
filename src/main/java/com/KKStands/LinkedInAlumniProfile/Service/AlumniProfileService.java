package com.KKStands.LinkedInAlumniProfile.Service;

import com.KKStands.LinkedInAlumniProfile.DTO.AlumniProfileDTO;
import com.KKStands.LinkedInAlumniProfile.Entity.AlumniProfile;
import com.KKStands.LinkedInAlumniProfile.Repository.AlumniProfileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlumniProfileService {
    private final AlumniProfileRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveAlumniFromWebhookResult(Object resultObject) throws Exception {
        log.info("received result object: \n{}", resultObject);
        log.info("resultObject class = {}, resultObject = {}",
                resultObject == null ? "null" : resultObject.getClass().getName(),
                resultObject);

        List<AlumniProfileDTO> alumniList = null;

        if (resultObject == null ||resultObject instanceof com.fasterxml.jackson.databind.JsonNode node && node.isNull()) {

            log.warn("No alumni data found in resultObject. Skipping save operation.");
            return;
        }

        if (resultObject instanceof String str) {
            if (str.isBlank()) throw new IllegalArgumentException("resultObject string is blank");
            alumniList = objectMapper.readValue(str, new TypeReference<List<AlumniProfileDTO>>() {});
        } else if (resultObject instanceof List<?>) {
            alumniList = ((List<?>) resultObject).stream()
                    .map(o -> objectMapper.convertValue(o, AlumniProfileDTO.class))
                    .toList();
        } else if (resultObject instanceof JsonNode node) {
            if (node.isArray()) {
                alumniList = objectMapper.convertValue(node, new TypeReference<List<AlumniProfileDTO>>() {});
            } else if (node.isTextual()) {
                alumniList = objectMapper.readValue(node.asText(), new TypeReference<List<AlumniProfileDTO>>() {});
            } else {
                throw new IllegalArgumentException("Unknown JsonNode resultObject structure: " + node);
            }
        } else {
            // Generic fallback
            alumniList = objectMapper.convertValue(resultObject, new TypeReference<List<AlumniProfileDTO>>() {});
        }

        if (alumniList == null || alumniList.isEmpty()) {
            throw new IllegalArgumentException("Result object is empty or not a valid alumni list.");
        }


        for (AlumniProfileDTO dto : alumniList) {

            if (dto.getProfileUrl() == null || dto.getProfileUrl().isBlank()) {
                log.warn("Skipping alumni due to missing LinkedIn profile URL: {}", dto);
                continue;
            }

            if (repository.existsByLinkedinProfileUrl(dto.getProfileUrl())) continue;


            AlumniProfile entity = new AlumniProfile();
            entity.setName(dto.getFullName());
            entity.setLinkedinProfileUrl(dto.getProfileUrl());
            entity.setLinkedinHeadline(dto.getHeadline());
            entity.setLocation(dto.getLocation());
            entity.setCurrentRole(dto.getJobTitle());
            entity.setUniversity(dto.getSchool());
            entity.setPassoutYear(extractPassoutYear(dto.getSchoolDateRange()));
            repository.save(entity);
        }

    }
    private Integer extractPassoutYear(String dateRange) {

        if (dateRange == null || !dateRange.contains("-")) return null;
        String[] parts = dateRange.split("-");
        String endPart = parts[1].trim();
        try {
            return Integer.parseInt(endPart.substring(endPart.length() - 4));
        } catch (Exception e) {
            return null;
        }
    }


    public List<AlumniProfile> getAllAlumniProfiles(){
        return repository.findAll();
    }



}
