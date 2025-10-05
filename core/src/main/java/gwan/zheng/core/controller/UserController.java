package gwan.zheng.core.controller;

import gwan.zheng.core.model.entity.Coin;
import gwan.zheng.core.model.entity.InviteCode;
import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.CoinRepository;
import gwan.zheng.core.model.repository.UserRepository;
import gwan.zheng.core.service.InviteCodeService;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import gwan.zheng.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-11:09
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InviteCodeService inviteCodeService;
    @Autowired
    private CoinRepository coinRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/get_profile")
    public User getProfile() {
        Long userId = UserContext.getUserId();
        return userRepository.findById(userId).orElse(null);
    }

    @GetMapping("/get_owner_invite_codes")
    public List<InviteCode> getOwnerInviteCodes() {
        Long userId = UserContext.getUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return List.of();
        }
        return inviteCodeService.getOwnerInviteCodes(user);
    }

    @GetMapping("/get_owner_valid_invite_codes")
    public List<String> getOwnerValidInviteCodes() {
        Long userId = UserContext.getUserId();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return List.of();
        }
        List<InviteCode> andRefreshOwnerValidInviteCodes = inviteCodeService.getAndRefreshOwnerValidInviteCodes(user);
        return andRefreshOwnerValidInviteCodes.stream().map(InviteCode::getCode).toList();
    }

    @PutMapping("/update_count")
    public ResultDto<String> updateCount(@RequestParam Long count) {
        Long userId = UserContext.getUserId();
        Coin byUserId = coinRepository.findByUserId(userId);
        if (byUserId == null) {
            ResultDto<String> stringResultDto = new ResultDto<>();
            stringResultDto.addError("用户不存在");
            return stringResultDto;
        }
        byUserId.setNumber(count);
        coinRepository.save(byUserId);
        return new ResultDto<>();
    }

}
