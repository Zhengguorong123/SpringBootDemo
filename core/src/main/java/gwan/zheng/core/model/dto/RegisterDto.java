package gwan.zheng.core.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author: 郑国荣
 * @Date: 2025-10-02-17:08
 * @Description:
 */

@Setter
@Getter
@NoArgsConstructor
public class RegisterDto {

    @NotNull
    @Size(min = 1, max = 50, message = "Account length must be between 1 and 50 characters")
    private String account;

    @NotNull
    private String username;

    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String inviteCode;
}
