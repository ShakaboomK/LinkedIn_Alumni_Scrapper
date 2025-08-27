package com.KKStands.LinkedInAlumniProfile.Utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MyUtilClass {
    public  String buildSearchUrl(String university, String designation, Integer passoutYear) {
        if (university == null || university.isBlank())
            throw new IllegalArgumentException("University is required");
        if (designation == null || designation.isBlank())
            throw new IllegalArgumentException("Designation is required");

        StringBuilder sb = new StringBuilder();
        sb.append(university.trim()).append(" ").append(designation.trim());
        if (passoutYear != null) {
            sb.append(" ").append(passoutYear);
        }
        String keywords = java.net.URLEncoder.encode(sb.toString(), java.nio.charset.StandardCharsets.UTF_8);
        return "https://www.linkedin.com/search/results/people/?keywords=" + keywords + "&origin=GLOBAL_SEARCH_HEADER";
    }
}
