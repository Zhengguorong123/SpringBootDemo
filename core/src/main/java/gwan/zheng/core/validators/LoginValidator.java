package gwan.zheng.core.validators;

import gwan.zheng.core.model.entity.User;
import gwan.zheng.core.model.repository.UserRepository;
import gwan.zheng.core.utils.PasswordUtils;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import gwan.zheng.springbootcommondemo.validator.CustomBaseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-17:57
 * @Description:
 */

@Component
public class LoginValidator extends CustomBaseValidator<LoginDto> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResultDto<LoginDto> validate(LoginDto loginDto) {
        ResultDto<LoginDto> resultDto = new ResultDto<>();
        resultDto.setData(loginDto);
        super.baseValidate(resultDto);
        User byUsername = userRepository.findByUsername(loginDto.getUsername());

        if (byUsername == null) {
            resultDto.addError("Username does not exist");
            return resultDto;
        }
        if (!PasswordUtils.verifyPassword(loginDto.getPassword(),
                byUsername.getSalt(),
                byUsername.getPasswordHash())){
            resultDto.addError("Incorrect password");
            return resultDto;
        }

        return resultDto;
    }
}
