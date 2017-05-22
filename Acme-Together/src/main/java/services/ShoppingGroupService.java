
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ShoppingGroupRepository;
import domain.Category;
import domain.Comment;
import domain.Product;
import domain.Punctuation;
import domain.ShoppingGroup;
import domain.User;
import forms.ShoppingGroupForm;

@Service
@Transactional
public class ShoppingGroupService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ShoppingGroupRepository	shoppingGroupRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService				userService;


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
		final User principal = this.userService.findByPrincipal();
		principal.getMyShoppingGroups().add(s);
		principal.getShoppingGroup().add(s);
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

	public Collection<ShoppingGroup> findShoppingGroupByCategory(final Category category) {
		Assert.notNull(category);

		Collection<ShoppingGroup> shoppingGroups;

		shoppingGroups = this.shoppingGroupRepository.findShoppingGroupsByCategory(category.getId());

		return shoppingGroups;
	}

	public Collection<ShoppingGroup> listPublicForUsersOfSH() {
		Collection<ShoppingGroup> shoppingGroups;

		final User principal = this.userService.findByPrincipal();

		shoppingGroups = this.shoppingGroupRepository.listPublicForUsersOfSH(principal.getId());

		return shoppingGroups;
	}

	public Collection<ShoppingGroup> ShoppingGroupsToWichBelongsAndNotCreatedBy(final User u) {
		Collection<ShoppingGroup> shoppingGroups;

		shoppingGroups = this.shoppingGroupRepository.ShoppingGroupsToWichBelongsAndNotCreatedBy(u.getId());

		return shoppingGroups;

	}


	@Autowired
	private Validator	validator;


	public ShoppingGroup reconstruct(final ShoppingGroupForm form, final BindingResult bindingResult) {
		ShoppingGroup result;
		Collection<Comment> cs;
		User principal;
		Collection<Product> products;
		Collection<Punctuation> punctuations;
		Collection<User> users;

		result = new ShoppingGroup();
		cs = new ArrayList<Comment>();
		principal = this.userService.findByPrincipal();
		products = new ArrayList<Product>();
		punctuations = new ArrayList<Punctuation>();
		users = new ArrayList<User>();
		users.add(principal);

		result.setCategory(form.getCategory());
		result.setComments(cs);
		result.setCreator(principal);
		result.setDescription(form.getDescription());
		result.setFreePlaces(form.getFreePlaces());
		result.setLastOrderDate(null);
		result.setName(form.getName());
		result.setPrivate_group(form.isPrivate_group());
		result.setProducts(products);
		result.setPunctuations(punctuations);
		result.setPuntuation(0);
		result.setSite(form.getSite());
		result.setUsers(users);

		this.validator.validate(result, bindingResult);

		return result;
	}

}
