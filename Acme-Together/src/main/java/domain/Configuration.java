
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Min;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Constructor ---------------------

	public Configuration() {
		super();
	}


	// Attributes ------------------------

	private double	fee;


	@Min(0)
	public double getFee() {
		return this.fee;
	}

	public void setFee(final double fee) {
		this.fee = fee;
	}

}
