
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public abstract class PuntuableEntity extends DomainEntity {

	//Constructor --------------------------------------------------------

	public PuntuableEntity() {
		super();
	}


	private Collection<PuntuableEntity>	toPuntuate;


	@ManyToMany
	@Valid
	@NotNull
	public Collection<PuntuableEntity> getToPuntuate() {
		return this.toPuntuate;
	}

	public void setToPuntuate(final Collection<PuntuableEntity> toPuntuate) {
		this.toPuntuate = toPuntuate;
	}

}
