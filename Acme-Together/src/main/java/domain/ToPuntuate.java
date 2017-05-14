
package domain;

import java.util.Collection;

import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class ToPuntuate extends DomainEntity {

	//Constructor --------------------------------------------------------

	public ToPuntuate() {
		super();
	}

	private Collection<ToPuntuate> toPuntuate;

	@ManyToMany
	@Valid
	@NotNull
	public Collection<ToPuntuate> getToPuntuate() {
		return toPuntuate;
	}

	public void setToPuntuate(Collection<ToPuntuate> toPuntuate) {
		this.toPuntuate = toPuntuate;
	}
	
	
}
