package com.example.springboot.validator;

import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private final UserService userService;
    private final MessageSource messageSource;

    @Autowired
    public UserValidator(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        User userEmail = userService.getUserByEmail(user.getEmail());
        if (userEmail != null && user.getId() != userEmail.getId())
            errors.rejectValue("email", "", messageSource.getMessage("userValidator.email.notEmpty", null, LocaleContextHolder.getLocale()));

        if (user.getName().isEmpty())
            errors.rejectValue("name", "",messageSource.getMessage("userValidator.name.required", null, LocaleContextHolder.getLocale()));

        if (!user.getEmail().matches("^\\w+@\\w+\\.\\w+$"))
            errors.rejectValue("email", "", messageSource.getMessage("userValidator.email.incorrect", null, LocaleContextHolder.getLocale()));

        if (user.getPassword().length() < 5 || user.getPassword().length() > 30)
            errors.rejectValue("password", "",messageSource.getMessage("userValidator.password.incorrect", null, LocaleContextHolder.getLocale()));
    }
}
