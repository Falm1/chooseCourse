package com.example.entity.enums;

import lombok.Data;

public enum CourseStatusEnum {

    /**
     * 类型 0-课程 1-实验 2-作业 3-期末考试
     */
    Course(0, "课程"),
    Test(1, "实验"),
    Work(2, "作业"),
    Exam(3, "考试");

    public static CourseStatusEnum getValue(Integer value){
        if(value == null){
            return null;
        }
        CourseStatusEnum[] values = CourseStatusEnum.values();
        for (CourseStatusEnum courseStatusEnum : values) {
            if(value.equals(courseStatusEnum.value)){
                return courseStatusEnum;
            }
        }
        return null;
    }

    CourseStatusEnum(int value, String text) {
        this.text = text;
        this.value = value;
    }
    private Integer value;
    private String text;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
