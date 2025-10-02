package gwan.zheng.core.controller;

import gwan.zheng.core.model.dto.RegisterDto;
import gwan.zheng.core.service.AuthService;
import gwan.zheng.core.validators.LoginDto;
import gwan.zheng.service.TokenManager;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private AuthService authService;

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
        return authService.register(registerDto);
    }
}
