/* Copyright © 2020 Yuech and/or its affiliates. All rights reserved. */
package wiki.chenxun.apt.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 陈勋
 * @version 1.0
 * @date 2021-06-23 3:21 下午
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Validated {

    Class<?>[] value() default {};
}
