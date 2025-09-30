package gwan.zheng.core.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gwan.zheng.core.model.entity.Record;
import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-15:22
 * @Description:
 */

@Repository
public interface RecordRepository extends JpaRepository<Record,Long> {
//    find by name
    List<Record> findByName(String name);
//    fuzzyQuery
    List<Record> findByNameContaining(String name);
}
