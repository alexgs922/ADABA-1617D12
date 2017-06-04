
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PunctuationRepository;
import domain.Punctuation;
import domain.ShoppingGroup;
import domain.User;

@Service
@Transactional
public class PunctuationService {

	// Managed repository -------------------------------------------

	@Autowired
	private PunctuationRepository	punctuationRepository;

	@Autowired
	private UserService				userService;

	@Autowired
	private ShoppingGroupService	shoppingGroupService;


	// Supporting services ------------------------------------------

	// Constructor --------------------------------------------------

	public PunctuationService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Punctuation create() {
		Punctuation res;
		res = new Punctuation();
		return res;
	}

	public Collection<Punctuation> findAll() {
		Collection<Punctuation> res;
		res = this.punctuationRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Punctuation findOne(final int punctuationId) {
		Punctuation res;
		res = this.punctuationRepository.findOne(punctuationId);
		Assert.notNull(res);
		return res;
	}

	public Punctuation save(final Punctuation c) {
		Assert.notNull(c);
		return this.punctuationRepository.save(c);

	}

	public Punctuation saveAndFlush(final Punctuation c) {
		Assert.notNull(c);
		return this.punctuationRepository.saveAndFlush(c);

	}

	public void delete(final Punctuation c) {
		Assert.notNull(c);
		this.punctuationRepository.delete(c);
	}

	public void flush() {
		this.punctuationRepository.flush();
	}
	// Other business methods --------------------------------------------------------------

	public Punctuation getPunctuationByShoppingGroupAndUser(final ShoppingGroup shoppingGroup, final User user) {
		Assert.notNull(shoppingGroup);
		Assert.notNull(user);

		Punctuation res;

		res = this.punctuationRepository.getPunctuationByShoppingGroupAndUser(shoppingGroup.getId(), user.getId());

		return res;
	}


	@Autowired
	private Validator	validator;


	public Punctuation reconstruct(final Punctuation punctuation, final BindingResult binding) {

		Punctuation result;

		if (punctuation.getId() == 0)
			result = punctuation;
		else {

			result = this.punctuationRepository.findOne(punctuation.getId());

			result.setValue(punctuation.getValue());
		}

		this.validator.validate(punctuation, binding);

		this.punctuationRepository.flush();

		return result;
	}

	public void rate(final ShoppingGroup shoppingGroup, final Punctuation rate) {
		Assert.notNull(shoppingGroup);
		Assert.notNull(rate);

		final User principal = this.userService.findByPrincipal();

		Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
		Assert.isTrue(!principal.getPunctuations().contains(rate));

		this.userService.save(principal);
		this.shoppingGroupService.save(shoppingGroup);
		this.punctuationRepository.saveAndFlush(rate);
	}

	public void editRate(final ShoppingGroup shoppingGroup, final Punctuation rate) {
		Assert.notNull(shoppingGroup);
		Assert.notNull(rate);

		final User principal = this.userService.findByPrincipal();

		Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
		Assert.isTrue(principal.getPunctuations().contains(rate));

		this.shoppingGroupService.save(shoppingGroup);
		this.punctuationRepository.saveAndFlush(rate);
	}
}
