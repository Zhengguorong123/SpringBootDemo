package gwan.zheng.core.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-10:13
 * @Description:
 */

@Data
@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    private String name;

    private String email;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Coin coin;

    // 工具方法：绑定 coin
    public void createCoin() {
        Coin coin = new Coin();
        coin.setUser(this);
        coin.setNumber(0L);
        this.coin = coin;
    }
}
