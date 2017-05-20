
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Punctuation extends DomainEntity {

	//Constructors -------------------------------------

	public Punctuation() {
		super();
	}


	// Attributes

	private int	value;


	public int getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		this.value = value;
	}


	// Relationships ------------------------------------

	private User	userPunctuation;
	private User	shoppingGroup;


	@Valid
	@ManyToOne(optional = false)
	public User getUserPunctuation() {
		return this.userPunctuation;
	}

	public void setUserPunctuation(final User userPunctuation) {
		this.userPunctuation = userPunctuation;
	}

	@Valid
	@ManyToOne(optional = false)
	public User getShoppingGroup() {
		return this.shoppingGroup;
	}

	public void setShoppingGroup(final User shoppingGroup) {
		this.shoppingGroup = shoppingGroup;
	}

}
