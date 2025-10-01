package gwan.zheng.core.model.dto;

import gwan.zheng.springbootcommondemo.enums.JobStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-15:17
 * @Description:
 */
@Data
public class RecordDto {

    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String maker;

    private String name;

    private String description;

    private String type;

    private JobStatus status;

    private String remark;
}
