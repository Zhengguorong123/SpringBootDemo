package gwan.zheng.core.controller;

import gwan.zheng.core.model.entity.Record;
import gwan.zheng.core.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-16:40
 * @Description:
 */
@RestController
@RequestMapping("/api/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping
    public List<Record> getAll() {
        return recordService.findAll();
    }

    @GetMapping("/{id}")
    public Record getById(@PathVariable Long id) {
        return recordService.findById(id);
    }

    @PostMapping
    public Record create(@RequestBody Record record) {
        return recordService.save(record);
    }

    @PutMapping("/{id}")
    public Record update(@PathVariable Long id, @RequestBody Record record) {
        record.setId(id);
        return recordService.save(record);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        recordService.deleteById(id);
    }

    @GetMapping("/search")
    public List<Record> searchByName(@RequestParam String name) {
        return recordService.findByNameContaining(name);
    }
}
