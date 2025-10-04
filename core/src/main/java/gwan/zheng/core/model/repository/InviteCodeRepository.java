package gwan.zheng.core.model.repository;

import gwan.zheng.core.model.entity.InviteCode;
import gwan.zheng.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-04-13:46
 * @Description:
 */
public interface InviteCodeRepository extends JpaRepository<InviteCode, String> {
    InviteCode findByOwner(User owner);
    InviteCode findByCode(String code);
    List<InviteCode> findAllByOwner(User owner);
}
