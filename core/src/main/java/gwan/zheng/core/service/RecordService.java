package gwan.zheng.core.service;

import gwan.zheng.core.model.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import gwan.zheng.core.model.entity.Record;
/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-15:22
 * @Description:
 */

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    public List<Record> findAll(){
        return recordRepository.findAll();
    }

    public Page<Record> findWebPage(Pageable pageable){
        return recordRepository.findAll(pageable);
    }

    public Record save(Record record){
        return recordRepository.save(record);
    }

    public Record findById(Long id){
        return recordRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id){
        recordRepository.deleteById(id);
    }

    public List<Record> findByName(String name){
        return recordRepository.findByName(name);
    }

    public List<Record> findByNameContaining(String name){
        return recordRepository.findByNameContaining(name);
    }
}
