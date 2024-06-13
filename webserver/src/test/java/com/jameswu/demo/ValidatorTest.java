package com.jameswu.demo;

import com.jameswu.demo.annotation.EmailValidator;
import com.jameswu.demo.annotation.PasswordValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

class ValidatorTest {

	private static ConstraintValidatorContext context;

	@BeforeAll
	public static void setup() {
		ConstraintValidatorContext.ConstraintViolationBuilder violationBuilderMock =
				Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
		context = Mockito.mock(ConstraintValidatorContext.class);
		Mockito.when(context.buildConstraintViolationWithTemplate(ArgumentMatchers.anyString()))
				.thenReturn(violationBuilderMock);
	}

	@Test
	void passwordValidation() {
		PasswordValidator validator = new PasswordValidator();
		String[] validPassword = new String[] {"StrongPassword123", "SecureP@ssw0rd!", "MyP@ssw0rd123"};
		String[] invalidPassword = new String[] {
			// (too short)
			"weak",
			// (no uppercase, no digit)
			"password",
			// (no uppercase, no lowercase)
			"1234567890",
		};
		for (String invalid : invalidPassword) {
			Assertions.assertFalse(validator.isValid(invalid, context));
		}
		for (String valid : validPassword) {
			Assertions.assertTrue(validator.isValid(valid, context));
		}
	}

	@Test
	void emailValidation() {
		EmailValidator emailValidator = new EmailValidator();
		String[] validEmails = new String[] {
			"example.email+123@domain.com", "user.name@sub-domain.example.org", "valid_email@some-domain123.net"
		};
		String[] invalidEmails = new String[] {
			// (contains a comma)
			"invalid-email@domain,com",
			// (starts with a dot in the domain part)
			"user@.example.com",
			// (contains consecutive dots in the domain part)
			"user@domain..com"
		};
		for (String valid : validEmails) {
			Assertions.assertTrue(emailValidator.isValid(valid, context));
		}
		for (String invalid : invalidEmails) {
			Assertions.assertFalse(emailValidator.isValid(invalid, context));
		}
	}
}
