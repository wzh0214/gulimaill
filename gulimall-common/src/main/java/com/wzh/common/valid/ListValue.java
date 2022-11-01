package com.wzh.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author wzh
 * @data 2022/11/1 -16:02
 * 自定义校验注解
 */
@Documented
//约束。同一个注解可以指定多个不同的校验器，适配不同类型的校验
@Constraint(validatedBy = {ListValueConstraintValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {
    //    校验出错后，错误信息去哪取
    String message() default "{com.wzh.common.valid.ListValue.message}";

    //    支持分组校验的功能
    Class<?>[] groups() default { };

    //    自定义负载信息
    Class<? extends Payload>[] payload() default { };

    //    自定义注解里的属性
    int[] vals() default {};
}
