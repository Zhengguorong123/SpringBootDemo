package gwan.zheng.core.model.repository;

import gwan.zheng.core.model.entity.Coin;
import gwan.zheng.core.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-10:12
 * @Description:
 */
@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {

}
