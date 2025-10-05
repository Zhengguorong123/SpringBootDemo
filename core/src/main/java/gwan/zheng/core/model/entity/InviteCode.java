package gwan.zheng.core.model.entity;

import com.fasterxml.jackson.annotation.*;
import gwan.zheng.springbootcommondemo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-04-13:39
 * @Description:
 */
@Entity
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class InviteCode extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnoreProperties({"inviteCodes", "coin", "updateTime","inviteCodes"})
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id")
    @JsonIgnoreProperties({"inviteCodes", "coin", "updateTime","inviteCodes"})
    private User invitee;

    // 临时字段：只给 JSON 用，不存数据库
    @Transient
    @JsonProperty("ownerId")
    private Long ownerId;

    public Long getInviteeId() {
        return invitee != null ? invitee.getId() : null;
    }

    public Long getOwnerId() {
        return owner != null ? owner.getId() : null;
    }

    private boolean used = false;
    private LocalDateTime expireAt;
    private LocalDateTime usedAt;
}


