package gwan.zheng.core.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.LocalDateTime;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-10:08
 * @Description:
 */

@Data
@Getter
@Setter
@Entity
public class Coin {

    @Id
    private Long id;  // 主键

    @OneToOne
    @MapsId
    @JsonBackReference
    @JoinColumn(name = "id")
    private User user;


    private Long number;
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        this.updateTime = LocalDateTime.now();
    }
}
