package online.bookstore.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String string1;
    private String string2;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        string1 = constraintAnnotation.str1();
        string2 = constraintAnnotation.str2();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object firstObject = new BeanWrapperImpl(o)
                .getPropertyValue(string1);

        Object secondObject = new BeanWrapperImpl(o)
                .getPropertyValue(string2);

        return firstObject.equals(secondObject);
    }

}
