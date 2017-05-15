
package domain;

import java.util.Collection;

import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class Category extends DomainEntity {

	//Constructors -------------------------------------

	public Category() {
		super();
	}


	//Attributes ------------------------------------

	private String	name;
	private String	description;


	//Getters and Setters --------------------------

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


	//Relationships

	private Collection<ShoppingGroup>	shoppingGroups;


	@OneToMany(mappedBy = "category")
	@Valid
	@NotNull
	public Collection<ShoppingGroup> getShoppingGroups() {
		return this.shoppingGroups;
	}

	public void setShoppingGroups(final Collection<ShoppingGroup> shoppingGroups) {
		this.shoppingGroups = shoppingGroups;
	}

}
