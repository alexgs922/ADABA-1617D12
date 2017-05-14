
package domain;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PuntuableEntity extends DomainEntity {

	//Constructor --------------------------------------------------------

	public PuntuableEntity() {
		super();
	}

	private Collection<PuntuableEntity> toPuntuate;

	@ManyToMany
	@Valid
	@NotNull
	public Collection<PuntuableEntity> getToPuntuate() {
		return toPuntuate;
	}

	public void setToPuntuate(Collection<PuntuableEntity> toPuntuate) {
		this.toPuntuate = toPuntuate;
	}
	
	
}
