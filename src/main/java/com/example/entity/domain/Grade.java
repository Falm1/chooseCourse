package com.example.entity.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName db_grade
 */
@Data
public class Grade implements Serializable {
    /**
     * 对应选课表的主键
     */
    private Long id;

    /**
     * 最终成绩
     */
    private BigDecimal grade;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String modifyUser;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 逻辑删除 0-未删除 1-已删除
     */
    private Integer isDelete;

}