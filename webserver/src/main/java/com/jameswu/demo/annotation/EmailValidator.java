package com.jameswu.demo.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<Email, String> {
	private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

	@Override
	public boolean isValid(String email, ConstraintValidatorContext context) {
		if (email == null || !email.matches(EMAIL_PATTERN)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("This email is not valid")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
