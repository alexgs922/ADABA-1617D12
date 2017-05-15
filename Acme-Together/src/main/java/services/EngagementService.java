
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EngagementRepository;
import domain.Engagement;

@Service
@Transactional
public class EngagementService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private EngagementRepository	engagementRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public EngagementService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Engagement create() {
		Engagement result;

		result = new Engagement();

		return result;
	}

	public Collection<Engagement> findAll() {
		Collection<Engagement> res;
		res = this.engagementRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Engagement findOne(final int engagementId) {
		Engagement res;
		res = this.engagementRepository.findOne(engagementId);
		Assert.notNull(res);
		return res;
	}

	public Engagement save(final Engagement e) {
		Assert.notNull(e);
		return this.engagementRepository.save(e);

	}

	public void delete(final Engagement e) {
		Assert.notNull(e);
		this.engagementRepository.delete(e);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.engagementRepository.flush();
	}
}
