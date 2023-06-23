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
     * 平时成绩
     */
    private BigDecimal nomalGrade;

    /**
     * 平时成绩占比
     */
    private BigDecimal nomalPercent;

    /**
     * 实验成绩
     */
    private BigDecimal testGrade;

    /**
     * 实验成绩占比
     */
    private BigDecimal testPercent;

    /**
     * 作业成绩
     */
    private BigDecimal workGrade;

    /**
     * 作业成绩占比
     */
    private BigDecimal workPercent;

    /**
     * 考试成绩
     */
    private BigDecimal examGrade;

    /**
     * 考试成绩占比
     */
    private BigDecimal examPercent;

    /**
     * 最终成绩
     */
    private BigDecimal finalGrade;

    /**
     * 最终成绩占比
     */
    private BigDecimal fianlPercent;

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

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Grade other = (Grade) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getNomalGrade() == null ? other.getNomalGrade() == null : this.getNomalGrade().equals(other.getNomalGrade()))
            && (this.getNomalPercent() == null ? other.getNomalPercent() == null : this.getNomalPercent().equals(other.getNomalPercent()))
            && (this.getTestGrade() == null ? other.getTestGrade() == null : this.getTestGrade().equals(other.getTestGrade()))
            && (this.getTestPercent() == null ? other.getTestPercent() == null : this.getTestPercent().equals(other.getTestPercent()))
            && (this.getWorkGrade() == null ? other.getWorkGrade() == null : this.getWorkGrade().equals(other.getWorkGrade()))
            && (this.getWorkPercent() == null ? other.getWorkPercent() == null : this.getWorkPercent().equals(other.getWorkPercent()))
            && (this.getExamGrade() == null ? other.getExamGrade() == null : this.getExamGrade().equals(other.getExamGrade()))
            && (this.getExamPercent() == null ? other.getExamPercent() == null : this.getExamPercent().equals(other.getExamPercent()))
            && (this.getFinalGrade() == null ? other.getFinalGrade() == null : this.getFinalGrade().equals(other.getFinalGrade()))
            && (this.getFianlPercent() == null ? other.getFianlPercent() == null : this.getFianlPercent().equals(other.getFianlPercent()))
            && (this.getCreateUser() == null ? other.getCreateUser() == null : this.getCreateUser().equals(other.getCreateUser()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getModifyUser() == null ? other.getModifyUser() == null : this.getModifyUser().equals(other.getModifyUser()))
            && (this.getModifyTime() == null ? other.getModifyTime() == null : this.getModifyTime().equals(other.getModifyTime()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getNomalGrade() == null) ? 0 : getNomalGrade().hashCode());
        result = prime * result + ((getNomalPercent() == null) ? 0 : getNomalPercent().hashCode());
        result = prime * result + ((getTestGrade() == null) ? 0 : getTestGrade().hashCode());
        result = prime * result + ((getTestPercent() == null) ? 0 : getTestPercent().hashCode());
        result = prime * result + ((getWorkGrade() == null) ? 0 : getWorkGrade().hashCode());
        result = prime * result + ((getWorkPercent() == null) ? 0 : getWorkPercent().hashCode());
        result = prime * result + ((getExamGrade() == null) ? 0 : getExamGrade().hashCode());
        result = prime * result + ((getExamPercent() == null) ? 0 : getExamPercent().hashCode());
        result = prime * result + ((getFinalGrade() == null) ? 0 : getFinalGrade().hashCode());
        result = prime * result + ((getFianlPercent() == null) ? 0 : getFianlPercent().hashCode());
        result = prime * result + ((getCreateUser() == null) ? 0 : getCreateUser().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getModifyUser() == null) ? 0 : getModifyUser().hashCode());
        result = prime * result + ((getModifyTime() == null) ? 0 : getModifyTime().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", nomalGrade=").append(nomalGrade);
        sb.append(", nomalPercent=").append(nomalPercent);
        sb.append(", testGrade=").append(testGrade);
        sb.append(", testPercent=").append(testPercent);
        sb.append(", workGrade=").append(workGrade);
        sb.append(", workPercent=").append(workPercent);
        sb.append(", examGrade=").append(examGrade);
        sb.append(", examPercent=").append(examPercent);
        sb.append(", finalGrade=").append(finalGrade);
        sb.append(", fianlPercent=").append(fianlPercent);
        sb.append(", createUser=").append(createUser);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyUser=").append(modifyUser);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}