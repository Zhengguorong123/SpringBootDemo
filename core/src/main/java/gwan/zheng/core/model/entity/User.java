package gwan.zheng.core.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gwan.zheng.springbootcommondemo.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
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
public class User extends BaseEntity {
    @NotNull
    @Column(nullable = false, unique = true)
    @Size(min = 1, max = 50, message = "Account length must be between 1 and 50 characters")
    private String account;

    @NotNull
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    private String passwordHash;

    private String salt;

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
