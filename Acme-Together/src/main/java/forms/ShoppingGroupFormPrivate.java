
package forms;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.Category;
import domain.User;

public class ShoppingGroupFormPrivate {

	// Constructors -----------------------------------------------------------
	public ShoppingGroupFormPrivate() {
		super();

	}


	// Attributes -------------------------------------------------------------

	private boolean				private_group;
	private String				name;
	private String				description;
	private String				site;
	private Category			category;
	private Collection<User>	users;


	@NotNull
	@NotEmpty
	public Collection<User> getUsers() {
		return this.users;
	}

	public void setUsers(final Collection<User> users) {
		this.users = users;
	}


	private boolean	termsOfUse;


	public boolean isPrivate_group() {
		return this.private_group;
	}

	public void setPrivate_group(final boolean private_group) {
		this.private_group = private_group;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSite() {
		return this.site;
	}

	public void setSite(final String site) {
		this.site = site;
	}

	@NotNull
	@Valid
	public Category getCategory() {
		return this.category;
	}

	public void setCategory(final Category category) {
		this.category = category;
	}

	@AssertTrue
	public boolean isTermsOfUse() {
		return this.termsOfUse;
	}

	public void setTermsOfUse(final boolean termsOfUse) {
		this.termsOfUse = termsOfUse;
	}

}
