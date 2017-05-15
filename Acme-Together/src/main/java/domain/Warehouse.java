
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
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
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWarehouseAddress() {
		return this.warehouseAddress;
	}

	public void setWarehouseAddress(final String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}


	private Collection<OrderDomain>	orders;


	@OneToMany
	@Valid
	@NotNull
	public Collection<OrderDomain> getOrders() {
		return this.orders;
	}

	public void setOrders(final Collection<OrderDomain> orders) {
		this.orders = orders;
	}

}
