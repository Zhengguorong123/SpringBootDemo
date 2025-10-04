package gwan.zheng.core.model.repository;

import gwan.zheng.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-11:09
 * @Description:
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
