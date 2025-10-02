package gwan.zheng.core.service;

import gwan.zheng.core.model.entity.Coin;
import gwan.zheng.core.model.repository.CoinRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-10:21
 * @Description:
 */

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepository;

    public List<Coin> getAll(){
        return coinRepository.findAll();
    }

    public Coin getCoinById(Long id){
        return coinRepository.findById(id).orElse(null);
    }

    public Coin createCoin(Coin coin){
        return coinRepository.save(coin);
    }

    public Coin updateCoin(Coin coin){
        return coinRepository.save(coin);
    }

    public void deleteCoinById(Long id){
        coinRepository.deleteById(id);
    }

    public Coin updateCoinById(Long id, Long number){
        Coin coin = coinRepository.findById(id).orElse(null);
        assert coin != null;
        coin.setNumber(number);
        return coinRepository.save(coin);
    }
}
