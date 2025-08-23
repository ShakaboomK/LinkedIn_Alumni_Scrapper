package com.FreightFox.LinkedInAluminiProfile.Service;

import com.FreightFox.LinkedInAluminiProfile.DTO.AlumniProfileDTO;
import com.FreightFox.LinkedInAluminiProfile.Entity.AlumniProfile;
import com.FreightFox.LinkedInAluminiProfile.Repository.AlumniProfileRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumniProfileService {
    private final AlumniProfileRepository repository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void saveAlumniFromWebhookResult(String resultObject) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        // resultObjectStr is a JSON string that is actually a JSON array
        List<AlumniProfileDTO> alumniList = objectMapper.readValue(resultObject,
                new TypeReference<List<AlumniProfileDTO>>() {});
        if (alumniList == null || alumniList.isEmpty()) {
            throw new IllegalArgumentException("Result object is empty or not a valid alumni list.");
        }
        for (AlumniProfileDTO dto : alumniList) {
            // Checking duplicates by LinkedIn profile URL or other unique key
            if (repository.existsByLinkedinProfileUrl(dto.getProfileUrl())) continue;

            AlumniProfile entity = new AlumniProfile();
            entity.setName(dto.getFullName());
            entity.setLinkedinProfileUrl(dto.getProfileUrl());
            entity.setLinkedinHeadline(dto.getHeadline());
            entity.setLocation(dto.getLocation());
            entity.setCurrentRole(dto.getJobTitle());  // map jobTitle to currentRole
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
