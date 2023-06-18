package com.example.utils.factory.grade;
import java.util.Date;

import com.example.entity.domain.Grade;

public class GradeFactory {

    public Grade getGrade(String studentId, Long courseId){
        Grade grade = new Grade();
        grade.setId(0L);
        grade.setStudentId(studentId);
        grade.setCourseId(courseId);
        grade.setCreateUser("");
        grade.setCreateTime(new Date());
        grade.setModifyUser("");
        grade.setModifyTime(new Date());
        grade.setIsDelete(0);
        return grade;
    }
}
