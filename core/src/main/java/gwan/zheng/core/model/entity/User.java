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
import java.util.ArrayList;
import java.util.List;

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

//    2 TODO 头像/个性签名
//    private String profilePhotoPath;
//    private String bio;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-coin")
    private Coin coin;

    // 该用户生成的邀请码（最多 5 个）
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("user-owner")
    private List<InviteCode> inviteCodes = new ArrayList<>();

    // 该用户是被谁邀请的（如果有的话）
    @OneToOne(mappedBy = "invitee")
    @JsonManagedReference("user-invitee")
    private InviteCode invitedBy;

    @Override
    protected void prePersist() {
        super.prePersist();
        if (this.coin == null) {
            Coin coin = new Coin();
            coin.setUser(this);   // 保证双向绑定
            coin.setNumber(0L);
            this.coin = coin;
        }
    }
}
