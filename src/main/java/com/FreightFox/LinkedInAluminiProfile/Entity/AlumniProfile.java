package com.FreightFox.LinkedInAluminiProfile.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfile {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String currentRole;
    private  String university;
    private String location;
    private String linkedinHeadline;
    private int passoutYear;

}
