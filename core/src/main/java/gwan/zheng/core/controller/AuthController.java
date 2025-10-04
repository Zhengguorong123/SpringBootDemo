package gwan.zheng.core.controller;

import gwan.zheng.core.model.dto.RegisterDto;
import gwan.zheng.core.model.entity.Coin;
import gwan.zheng.core.model.entity.InviteCode;
import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.CoinRepository;
import gwan.zheng.core.model.repository.UserRepository;
import gwan.zheng.core.service.AuthService;
import gwan.zheng.core.service.InviteCodeService;
import gwan.zheng.core.validators.LoginDto;
import gwan.zheng.service.TokenManager;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import gwan.zheng.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-16:10
 * @Description:
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CoinRepository coinRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InviteCodeService inviteCodeService;

    public AuthController(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResultDto<String> login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @GetMapping("/verify")
    public boolean verify(@RequestParam String token) {
        return tokenManager.validateToken(token);
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String token) {
        tokenManager.deleteToken(token);
    }

    @PostMapping("/register")
    public ResultDto<RegisterDto> createUser(@RequestBody RegisterDto registerDto) {
        String inviteCode = registerDto.getInviteCode();
        return authService.register(registerDto, inviteCode);
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
}
