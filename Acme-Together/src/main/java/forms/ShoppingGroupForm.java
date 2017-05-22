
package forms;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import domain.Category;

public class ShoppingGroupForm {

	// Constructors -----------------------------------------------------------
	public ShoppingGroupForm() {
		super();

	}


	// Attributes -------------------------------------------------------------

	private boolean		private_group;
	private String		name;
	private String		description;
	private int			freePlaces;
	private String		site;
	private Category	category;
	private boolean		termsOfUse;


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

	@Min(0)
	public int getFreePlaces() {
		return this.freePlaces;
	}

	public void setFreePlaces(final int freePlaces) {
		this.freePlaces = freePlaces;
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
