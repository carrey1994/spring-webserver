package com.jameswu.demo.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
	private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$";

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null || !password.matches(PASSWORD_PATTERN)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
							"This value must be between 8 and 20 characters long and contain at least one lowercase letter, one uppercase letter, and one digit")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
