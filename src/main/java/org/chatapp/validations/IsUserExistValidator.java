package org.chatapp.validations;

import org.chatapp.models.UserBindingModel;
import org.chatapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class IsUserExistValidator implements ConstraintValidator<IsUserExist, Object> {

   @Autowired
   private UserService userService;

   @Override
   public void initialize(IsUserExist constraint) {
   }

   public boolean isValid(Object userClass, ConstraintValidatorContext context) {
      UserBindingModel userBindingModel = null;
      if (userClass instanceof UserBindingModel) {
         userBindingModel = (UserBindingModel) userClass;
      }
      return this.userService.findUserByEmail(userBindingModel.getEmail());
   }
}
