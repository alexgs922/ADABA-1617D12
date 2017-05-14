
package domain;

import java.util.Collection;

import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Warehouse extends DomainEntity {

	//Constructors -------------------------------------

	public Warehouse() {
		super();
	}


	//Attributes ------------------------------------

	private String	name;
	private String	warehouseAddress;


	//Getters and Setters --------------------------

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getWarehouseAddress() {
		return this.warehouseAddress;
	}

	public void setWarehouseAddress(final String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	
	
	private Collection<Order>orders;

	@OneToMany
	@Valid
	@NotNull
	public Collection<Order> getOrders() {
		return orders;
	}

	public void setOrders(Collection<Order> orders) {
		this.orders = orders;
	}
	
	

}
