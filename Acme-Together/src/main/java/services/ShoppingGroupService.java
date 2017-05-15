
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ShoppingGroupRepository;
import domain.ShoppingGroup;

@Service
@Transactional
public class ShoppingGroupService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ShoppingGroupRepository	shoppingGroupRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ShoppingGroupService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public ShoppingGroup create() {
		ShoppingGroup res;
		res = new ShoppingGroup();
		return res;
	}

	public Collection<ShoppingGroup> findAll() {
		Collection<ShoppingGroup> res;
		res = this.shoppingGroupRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public ShoppingGroup findOne(final int shoppingGroupId) {
		ShoppingGroup res;
		res = this.shoppingGroupRepository.findOne(shoppingGroupId);
		Assert.notNull(res);
		return res;
	}

	public ShoppingGroup save(final ShoppingGroup s) {
		Assert.notNull(s);
		return this.shoppingGroupRepository.save(s);

	}

	public void delete(final ShoppingGroup s) {
		Assert.notNull(s);
		this.shoppingGroupRepository.delete(s);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.shoppingGroupRepository.flush();
	}
}
