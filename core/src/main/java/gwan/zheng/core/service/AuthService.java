package gwan.zheng.core.service;

import gwan.zheng.core.model.dto.RegisterDto;
import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.UserRepository;
import gwan.zheng.core.utils.PasswordUtils;
import gwan.zheng.core.validators.LoginDto;
import gwan.zheng.core.validators.LoginValidator;
import gwan.zheng.core.validators.RegisterValidator;
import gwan.zheng.service.TokenManager;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-16:13
 * @Description:
 */

@Service
public class AuthService {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegisterValidator registerValidator;

    @Autowired
    private LoginValidator loginValidator;

    public ResultDto<RegisterDto> register(RegisterDto registerDto) {
        // 注册用户逻辑

        ResultDto<RegisterDto> validate = registerValidator.validate(registerDto);
        if (!validate.isValid()) {
            return validate;
        }
        String salt = PasswordUtils.generateSalt();
        String passwordHash = PasswordUtils.hashPassword(registerDto.getPassword(), salt);
        User user = new User();
        BeanUtils.copyProperties(registerDto, user);
        user.setSalt(salt);
        user.setPasswordHash(passwordHash);
        userRepository.save(user);
        return validate;
    }

    public ResultDto<String> login(LoginDto loginDto) {
        ResultDto<LoginDto> validate = loginValidator.validate(loginDto);
        ResultDto<String> tokenResult = new ResultDto<>();
        if (!validate.isValid()) {
            tokenResult.setErrors(validate.getErrors());
            tokenResult.setSuccess(validate.isValid());
            return tokenResult;
        }
        User byUsername = userRepository.findByUsername(loginDto.getUsername());
        return  tokenManager.createToken(byUsername.getId());
    }

}
