package com.jameswu.demo;

import com.jameswu.demo.annotation.PasswordValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class WebApplicationTests {

	@Test
	public void passwordValidation() {
		ConstraintValidatorContext.ConstraintViolationBuilder violationBuilderMock =
				Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
		ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);
		Mockito.when(context.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(violationBuilderMock);
		String[] validPassword = new String[] {"StrongPassword123", "SecureP@ssw0rd!", "MyP@ssw0rd123"};
		String[] invalidPassword = new String[] {
			// (too short)
			"weak",
			// (no uppercase, no digit)
			"password",
			// (no uppercase, no lowercase)
			"1234567890",
		};
		PasswordValidator validator = new PasswordValidator();
		for (String invalid : invalidPassword) {
			Assertions.assertFalse(validator.isValid(invalid, context));
		}

		for (String valid : validPassword) {
			Assertions.assertTrue(validator.isValid(valid, context));
		}
	}
}
