package gwan.zheng.core.model.annotation;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-15:46
 * @Description:
 */
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValue {
    String message() default "Invalid enum type!";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    Class<? extends Enum<?>> enumClass();
}
