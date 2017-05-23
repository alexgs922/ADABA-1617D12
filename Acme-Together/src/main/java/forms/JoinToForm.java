
package forms;

import javax.validation.constraints.AssertTrue;

public class JoinToForm {

	// Constructors -----------------------------------------------------------
	public JoinToForm() {
		super();

	}


	// Attributes -------------------------------------------------------------

	private boolean	termsOfUse;


	@AssertTrue
	public boolean isTermsOfUse() {
		return this.termsOfUse;
	}

	public void setTermsOfUse(final boolean termsOfUse) {
		this.termsOfUse = termsOfUse;
	}

}
