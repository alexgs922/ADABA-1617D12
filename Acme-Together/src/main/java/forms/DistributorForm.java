
package forms;

import javax.persistence.Column;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import utilities.validators.PasswordMatchesDistributor;

@PasswordMatchesDistributor
public class DistributorForm {

	// Constructors -----------------------------------------------------------
	public DistributorForm() {
		super();

	}


	// Attributes -------------------------------------------------------------

	private String	username;
	private String	password;
	private String	passwordCheck;

	private boolean	termsOfUse;
	private String	name;
	private String	surName;
	private String	email;
	private String	phone;

	private String	companyName;
	private String	vatNumber;
	private String	companyAddress;
	private String	webPage;


	//Getters and Setters --------------------------

	@NotBlank
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	@NotBlank
	@Pattern(regexp = "^ES[ABCDEFGHJNPQRSUVW]{1}[1-9]{8}$")
	public String getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final String vatNumber) {
		this.vatNumber = vatNumber;
	}

	@NotBlank
	public String getCompanyAddress() {
		return this.companyAddress;
	}

	public void setCompanyAddress(final String companyAddress) {
		this.companyAddress = companyAddress;
	}

	@NotBlank
	@URL
	public String getWebPage() {
		return this.webPage;
	}

	public void setWebPage(final String webPage) {
		this.webPage = webPage;
	}

	@NotNull
	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;

	}
	public void setUsername(final String username) {
		this.username = username;
	}

	@NotNull
	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	@NotNull
	@Size(min = 5, max = 32)
	public String getPasswordCheck() {
		return this.passwordCheck;

	}

	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	@AssertTrue
	public boolean getTermsOfUse() {
		return this.termsOfUse;

	}

	public void setTermsOfUse(final boolean termsOfUse) {
		this.termsOfUse = termsOfUse;

	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;

	}

	@NotBlank
	public String getSurName() {
		return this.surName;
	}

	public void setSurName(final String surName) {
		this.surName = surName;

	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?\\d{1,3}?[- .]?\\d+$")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

}
