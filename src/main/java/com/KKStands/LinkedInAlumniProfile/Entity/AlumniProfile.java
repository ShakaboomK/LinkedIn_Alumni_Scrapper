package com.KKStands.LinkedInAlumniProfile.Entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlumniProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 512)
    private String linkedinProfileUrl;
    @Column(nullable = false)
    private String name;
    @Column(name = "current_role_name")
    private String currentRole;
    private  String university;
    private String location;
    private String linkedinHeadline;
    private Integer passoutYear;

}
