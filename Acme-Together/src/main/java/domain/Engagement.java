
package domain;

import java.util.Collection;

public class Engagement extends DomainEntity {

	//Constructors -------------------------------------

	public Engagement() {
		super();
	}


	//Attributes ------------------------------------

	private Collection<String>	listOrdersByUser;


	//Getters and Setters --------------------------

	public Collection<String> getListOrdersByUser() {
		return this.listOrdersByUser;
	}

	public void setListOrdersByUser(final Collection<String> listOrdersByUser) {
		this.listOrdersByUser = listOrdersByUser;
	}

}
