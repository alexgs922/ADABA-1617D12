
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Engagement extends DomainEntity {

	//Constructors -------------------------------------

	public Engagement() {
		super();
	}


	//Attributes ------------------------------------

	private Collection<String>	listOrdersByUser;


	//Getters and Setters --------------------------

	@ElementCollection
	public Collection<String> getListOrdersByUser() {
		return this.listOrdersByUser;
	}

	public void setListOrdersByUser(final Collection<String> listOrdersByUser) {
		this.listOrdersByUser = listOrdersByUser;
	}


	//Relationships

	private OrderDomain	order;


	@Valid
	
	@OneToOne(optional = false)
	public OrderDomain getOrder() {
		return this.order;
	}

	public void setOrder(final OrderDomain order) {
		this.order = order;
	}

}
