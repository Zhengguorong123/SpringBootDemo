package gwan.zheng.core.validators;

import gwan.zheng.core.model.dto.RegisterDto;
import gwan.zheng.springbootcommondemo.dto.ResultDto;
import gwan.zheng.springbootcommondemo.validator.CustomBaseValidator;
import org.springframework.stereotype.Component;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-16:17
 * @Description:
 */

@Component
public class RegisterValidator extends CustomBaseValidator<RegisterDto> {


    @Override
    public ResultDto<RegisterDto> validate(RegisterDto registerDto) {
        ResultDto<RegisterDto> resultDto = new ResultDto<>();
        resultDto.setData(registerDto);
        super.baseValidate(resultDto);
        return resultDto;
    }
}
