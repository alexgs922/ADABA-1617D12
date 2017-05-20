
package utilities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import forms.CommercialForm;

public class PasswordMatchesValidatorCommercial implements ConstraintValidator<PasswordMatchesCommercial, CommercialForm> {

	@Override
	public void initialize(final PasswordMatchesCommercial constraintAnnotation) {
	}
	@Override
	public boolean isValid(final CommercialForm form, final ConstraintValidatorContext context) {
		return form.getPassword().equals(form.getPasswordCheck());
	}
}
