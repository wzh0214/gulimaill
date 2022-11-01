package com.wzh.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wzh
 * @data 2022/11/1 -16:10
 */

//第一个泛型是校验注解，第二个泛型是校验数据类型
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {
    private Set<Integer> set =  new HashSet<>();
    // 初始化方法
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] value = constraintAnnotation.vals();
        for (int i : value) {
            set.add(i);
        }
    }

    // 判断是否校验成功
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return  set.contains(value);
    }
}
