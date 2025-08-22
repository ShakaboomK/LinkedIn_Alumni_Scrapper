package com.FreightFox.LinkedInAluminiProfile.Service;

import com.FreightFox.LinkedInAluminiProfile.Entity.AlumniProfile;
import com.FreightFox.LinkedInAluminiProfile.Repository.AlumniProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumniProfileService {
    private final AlumniProfileRepository alumniProfileRepository;
    private final PhantomBusterService phantomBusterService;

    public AlumniProfileService(AlumniProfileRepository alumniProfileRepository, PhantomBusterService phantomBusterService) {
        this.alumniProfileRepository = alumniProfileRepository;
        this.phantomBusterService = phantomBusterService;
    }
    public List<AlumniProfile> getAlumniProfilesInDB(){
        return alumniProfileRepository.findAll();
    }

//    public List<AlumniProfile> searchAndSaveAlumniProfiles(String university, String designation, String passoutYear){
//        List<AlumniProfile> results = phantomBusterService.searchAlumniProfiles(university, designation, passoutYear);
//        return alumniProfileRepository.saveAll(results);
//    }

}
