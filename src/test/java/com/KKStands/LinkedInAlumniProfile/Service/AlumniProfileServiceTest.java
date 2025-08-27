package com.KKStands.LinkedInAlumniProfile.Service;

import com.KKStands.LinkedInAlumniProfile.DTO.AlumniProfileDTO;
import com.KKStands.LinkedInAlumniProfile.Entity.AlumniProfile;
import com.KKStands.LinkedInAlumniProfile.Repository.AlumniProfileRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlumniProfileServiceTest {
    @Mock
    private AlumniProfileRepository  alumniProfileRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AlumniProfileService alumniProfileService;

    @Test
    public void testSaveAlumniFromValidJson() throws Exception {
        String json =" [{\"profileUrl\":\"http://linkedin.com/in/test\",\"fullName\":\"Test User\"}]";
        AlumniProfileDTO dto = new AlumniProfileDTO();
        dto.setProfileUrl("http://linkedin.com/in/test");
        dto.setFullName("Test User");

        List<AlumniProfileDTO> dtoList = List.of(dto);

        when(objectMapper.readValue(eq(json), any(TypeReference.class))).thenReturn(dtoList);
        when(alumniProfileRepository.existsByLinkedinProfileUrl("http://linkedin.com/in/test")).thenReturn(false);
        doNothing().when(alumniProfileRepository).save(any(AlumniProfile.class));

        alumniProfileService.saveAlumniFromWebhookResult(json);

        verify(alumniProfileRepository, times(1)).save(any(AlumniProfile.class));
    }
 }

