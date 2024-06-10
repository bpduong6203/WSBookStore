package com.bookstore.validator;

import com.bookstore.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUseridValidator implements ConstraintValidator<ValidUserId, User> {
    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if(user == null){
            return true;
        }
        return user.getId() != null;
    }
}
