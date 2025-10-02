package gwan.zheng.core.validators;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-17:56
 * @Description:
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotNull
    private String username;

    @NotNull
    private String password;

    private String token;
}
