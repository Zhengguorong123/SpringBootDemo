package gwan.zheng.core.model.entity;

import gwan.zheng.springbootcommondemo.annotation.EnumValue;
import gwan.zheng.springbootcommondemo.enums.JobStatus;
import gwan.zheng.springbootcommondemo.enums.JobType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-15:10
 * @Description:
 */
@Data
@Entity
@Getter
@Setter
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private Date updateTime;

    private String maker;

    @NotNull
    private String name;

    @NotNull
    @EnumValue(enumClass = JobType.class, message = "Invalid job type")
    private JobType type;

    @NotNull
    @EnumValue(enumClass = JobStatus.class, message = "Invalid job status")
    private JobStatus status;

    private String description;
    private String remark;

}
