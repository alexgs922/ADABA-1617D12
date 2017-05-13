
package domain;

import org.hibernate.validator.constraints.NotBlank;

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
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

}
