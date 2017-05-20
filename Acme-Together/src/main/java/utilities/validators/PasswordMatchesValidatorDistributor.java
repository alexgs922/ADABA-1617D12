
package utilities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import forms.DistributorForm;

public class PasswordMatchesValidatorDistributor implements ConstraintValidator<PasswordMatchesDistributor, DistributorForm> {

	@Override
	public void initialize(final PasswordMatchesDistributor constraintAnnotation) {
	}
	@Override
	public boolean isValid(final DistributorForm form, final ConstraintValidatorContext context) {
		return form.getPassword().equals(form.getPasswordCheck());
	}
}
