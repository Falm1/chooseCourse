package com.example.entity.Request.Grade;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeAddRequest {
    /**
     * 主键db_course中主键
     */
    private Long couId;

    /**
     * 成绩
     */
    private BigDecimal grade;
}
