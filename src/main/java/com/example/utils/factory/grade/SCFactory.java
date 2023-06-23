package com.example.utils.factory.grade;
import java.util.Date;

import com.example.entity.domain.SC;

public class SCFactory {

    public SC getSC(String studentId, String courseId, Integer isDelete){
        SC selectCourse = new SC();
        selectCourse.setStudentId(studentId);
        selectCourse.setCourseId(courseId);
        selectCourse.setCreateUser("");
        selectCourse.setCreateTime(new Date());
        selectCourse.setModifyUser("");
        selectCourse.setModifyTime(new Date());
        selectCourse.setIsDelete(isDelete);
        return selectCourse;
    }
}
