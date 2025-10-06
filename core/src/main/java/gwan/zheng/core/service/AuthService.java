package gwan.zheng.core.service;

import gwan.zheng.core.model.dto.RegisterDto;
import gwan.zheng.core.model.entity.InviteCode;
import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.InviteCodeRepository;
import gwan.zheng.core.model.repository.UserRepository;
import gwan.zheng.core.utils.PasswordUtils;
import gwan.zheng.core.validators.LoginDto;
import gwan.zheng.core.validators.LoginValidator;
import gwan.zheng.core.validators.RegisterValidator;
import gwan.zheng.service.TokenManager;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private InviteCodeService inviteCodeService;
    @Autowired
    private InviteCodeRepository inviteCodeRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;


    public ResultDto<RegisterDto> register(RegisterDto registerDto, String inviteCode) {
        // 注册用户逻辑

        ResultDto<RegisterDto> validate = registerValidator.validate(registerDto);
        if (!validate.isValid()) {
            return validate;
        }
        String salt = PasswordUtils.generateSalt();
        String passwordHash = PasswordUtils.hashPassword(registerDto.getPassword(), salt);
        User newUser = new User();
        BeanUtils.copyProperties(registerDto, newUser);
        newUser.setSalt(salt);
        newUser.setPasswordHash(passwordHash);
        ResultDto<InviteCode> inviteCodeResultDto = inviteCodeService.registerWithInvite(inviteCode, newUser);
        if (!inviteCodeResultDto.isValid()) {
            validate.setErrors(inviteCodeResultDto.getErrors());
            validate.setSuccess(inviteCodeResultDto.isValid());
            return validate;
        }
        try {
            // 保存用户
            userRepository.save(newUser);

            validate.setSuccess(true);
        } catch (Exception e) {
            // 数据库保存失败，Redis 不会删除
            validate.setSuccess(false);
            validate.addError("注册失败: " + e.getMessage());
            return validate;
        }
        // 保存 InviteCode 更新
        InviteCode inviteCodeEntity = inviteCodeResultDto.getData();
        inviteCodeRepository.save(inviteCodeEntity);
        // 只有数据库保存成功，才删除 Redis
        String redisKey = "invite:code:" + inviteCodeEntity.getCode();
        redisTemplate.delete(redisKey);
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
        tokenResult = tokenManager.createToken(byUsername.getId());
        tokenResult.setExtra(String.valueOf(byUsername.getId()));
        return tokenResult;

    }

}
