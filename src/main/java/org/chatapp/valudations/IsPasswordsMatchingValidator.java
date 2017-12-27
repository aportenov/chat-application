package org.chatapp.valudations;

import org.chatapp.models.UserBindingModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsPasswordsMatchingValidator implements ConstraintValidator<IsPasswordsMatching, Object> {
   @Override
   public void initialize(IsPasswordsMatching isPasswordsMatching) {

   }

   @Override
   public boolean isValid(Object userClass, ConstraintValidatorContext constraintValidatorContext) {
      if(userClass instanceof UserBindingModel){
         return ((UserBindingModel) userClass).getPassword().equals(((UserBindingModel) userClass).getConfirmPassword());
      }

      return false;
   }
}