package com.FreightFox.LinkedInAluminiProfile.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfileSearchRequest {
    private String university;
    private String designation;
    private int passoutYear;

}
