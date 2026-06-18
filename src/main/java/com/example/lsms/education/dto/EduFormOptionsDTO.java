package com.example.lsms.education.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EduFormOptionsDTO {

    private List<Option> categories;
    private List<Option> terms;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Option {
        private Long id;
        private String label;
    }
}
