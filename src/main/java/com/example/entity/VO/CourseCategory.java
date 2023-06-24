package com.example.entity.VO;

import lombok.Data;

import java.util.List;

@Data
public class CourseCategory {
    Long value;
    String label;
    List<CourseCate> children;
}
