package com.KKStands.LinkedInAlumniProfile.Repository;

import com.KKStands.LinkedInAlumniProfile.Entity.AlumniProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlumniProfileRepository extends JpaRepository<AlumniProfile, Long> {
    boolean existsByLinkedinProfileUrl(String linkedinProfileUrl);
}
