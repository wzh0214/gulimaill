package com.wzh.gulimall.product.exception;

import com.wzh.common.exception.BizCodeEnum;
import com.wzh.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

/**
 * @author wzh
 * @data 2022/11/1 -13:34
 * 统一处理异常
 */
@Slf4j
//@ResponseBody
//@ControllerAdvice
@RestControllerAdvice(basePackages = "com.wzh.gulimall.product.controller") //扫描controler包下的类
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class) // 出现对应异常被调用
    public R handValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题：{}，异常类型：{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        HashMap<String, String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError) -> {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return R.error(BizCodeEnum.VAILD_EXCEPTION.getCode(), BizCodeEnum.VAILD_EXCEPTION.getMsg()).put("data", errorMap);
    }

    // 处理全局异常，如果上面精确捕捉没触发
    @ExceptionHandler(value = Throwable.class)    //Exception和Error继承自Throwable
    public R handleException(Throwable throwable){
        log.error("错误：", throwable);
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(), BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }


}
