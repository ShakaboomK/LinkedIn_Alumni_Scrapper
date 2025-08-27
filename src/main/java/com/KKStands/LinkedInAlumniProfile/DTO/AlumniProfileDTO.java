package com.KKStands.LinkedInAlumniProfile.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfileDTO {
    private String profileUrl;
    private String fullName;
    private String headline;
    private String location;
    private String company;
    private String jobTitle; //equivalent to current role
    private String school;
    private String schoolDegree;
    private String schoolDateRange;
    private Integer passoutYear; //equivalent to date-range
}
