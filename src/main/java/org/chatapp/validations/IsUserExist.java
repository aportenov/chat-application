package org.chatapp.validations;

import org.springframework.messaging.handler.annotation.Payload;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = IsUserExistValidator.class)
public @interface IsUserExist {

    String message() default "User already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
