package com.nokia.logtools.utils;

import org.hibernate.validator.HibernateValidator;

import com.nokia.logtools.exception.BizException;
import com.nokia.logtools.result.ErrorEnum;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * explain: 验证输入参数
 *
 * @author: LiMeng
 * CreateDate 2018/4/26.
 */
public class ValidationUtils {
    /**
     * 使用hibernate的注解来进行验证
     *
     */
    private static Validator validator = Validation
            .byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    /**
     * 功能描述: <br>
     * 〈注解验证参数〉
     *
     * @param obj
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static <T> void validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            throw new BizException(ErrorEnum.REQUEST_PARAMETER_ERROR.getCode(), constraintViolations.iterator().next().getMessage());
        }
    }
}
