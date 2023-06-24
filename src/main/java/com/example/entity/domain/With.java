package com.example.entity.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class With {

    /**
     * 对应选课表的主键
     */
    private Long id;

    /**
     * 名称
     */
    private String courseName;

    /**
     * 占百分比
     */
    private BigDecimal percent;

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
     * 是否删除 0-未删除 1-已删除
     */
    private Integer isDelete;
}
