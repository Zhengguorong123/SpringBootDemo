package gwan.zheng.core.controller;

import gwan.zheng.core.model.entity.Coin;
import gwan.zheng.core.model.repository.CoinRepository;
import gwan.zheng.core.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-10:08
 * @Description:
 */
@RestController
@RequestMapping("/api/coins")
public class CoinController {

    @Autowired
    private CoinService coinService;

    @GetMapping
    public List<Coin> getAll(){
        return coinService.getAll();
    }

    @GetMapping("/{id}")
    public Coin getOne(@PathVariable Long id){
        return coinService.getCoinById(id);
    }

    @PostMapping
    public Coin create(@RequestBody Coin coin){
        return coinService.createCoin(coin);
    }

    @PutMapping
    public Coin update(@RequestBody Coin coin){
        return coinService.updateCoin(coin);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id){
        try {
            coinService.deleteCoinById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PutMapping("/{id}/{number}")
    public Coin updateCoinNumberById(@PathVariable Long id, @PathVariable Long number){
        return coinService.updateCoinById(id,number);
    }


}
