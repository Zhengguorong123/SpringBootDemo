package gwan.zheng.core.controller;

import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-11:09
 * @Description:
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.createCoin();
        return userRepository.save(user);
    }
}
