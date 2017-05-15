
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PuntuableEntityRepository;
import domain.PuntuableEntity;

@Service
@Transactional
public class PuntuableEntityService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PuntuableEntityRepository	puntuableEntityRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public PuntuableEntityService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Collection<PuntuableEntity> findAll() {
		Collection<PuntuableEntity> res;
		res = this.puntuableEntityRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PuntuableEntity findOne(final int puntuableEntityId) {
		PuntuableEntity res;
		res = this.puntuableEntityRepository.findOne(puntuableEntityId);
		Assert.notNull(res);
		return res;
	}

	public PuntuableEntity save(final PuntuableEntity p) {
		Assert.notNull(p);
		return this.puntuableEntityRepository.save(p);

	}

	public void delete(final PuntuableEntity p) {
		Assert.notNull(p);
		this.puntuableEntityRepository.delete(p);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.puntuableEntityRepository.flush();
	}
}
