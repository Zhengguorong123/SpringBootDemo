package gwan.zheng.core.model.annotation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * @Author: 郑国荣
 * @Date: 2025-09-30-15:47
 * @Description:
 */
// EnumValueValidator.java


public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {
    private EnumValue annotation;

    @Override
    public void initialize(EnumValue annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true; // 可配合@NotNull
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        for (Object enumValue : enumValues) {
            if (value.equals(enumValue.toString()) || value.equals(enumValue)) {
                return true;
            }
        }
        return false;
    }
}
