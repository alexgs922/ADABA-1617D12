
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Administrator;
import domain.Category;
import domain.ShoppingGroup;

@Service
@Transactional
public class CategoryService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CategoryRepository		categoryRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ShoppingGroupService	shoppingGroupService;


	// Constructors -----------------------------------------------------------

	public CategoryService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Category create() {
		Category res;
		res = new Category();
		return res;
	}

	public Collection<Category> findAll() {
		Assert.isTrue(this.checkAdminPrincipal());
		Collection<Category> res;
		res = this.categoryRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Collection<Category> findAll2() {
		Collection<Category> res;
		res = this.categoryRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Collection<Category> findAllLogout() {
		Collection<Category> res;
		res = this.categoryRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Category findOne(final int categoryId) {
		Assert.isTrue(this.checkAdminPrincipal());
		Category res;
		res = this.categoryRepository.findOne(categoryId);
		Assert.notNull(res);
		return res;
	}

	public Category save(final Category c) {
		Assert.isTrue(this.checkAdminPrincipal());
		Assert.notNull(c);
		return this.categoryRepository.save(c);
	}

	public Category saveAndFlush(final Category c) {
		Assert.isTrue(this.checkAdminPrincipal());
		Assert.notNull(c);
		return this.categoryRepository.saveAndFlush(c);

	}

	public void delete(final Category c) {
		Assert.isTrue(this.checkAdminPrincipal());
		Assert.notNull(c);

		Collection<ShoppingGroup> shoppingGroupsByCategory;
		shoppingGroupsByCategory = this.shoppingGroupService.findShoppingGroupByCategory(c);

		for (final ShoppingGroup s : shoppingGroupsByCategory) {
			s.setCategory(null);
			this.shoppingGroupService.save(s);
		}

		this.categoryRepository.delete(c);
	}

	//Other business methods --------------------------------------

	public boolean checkAdminPrincipal() {
		final boolean res;
		Administrator principal;

		principal = this.administratorService.findByPrincipal();

		res = principal != null;

		return res;
	}

	public void flush() {
		this.categoryRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public Category reconstruct(final Category category, final BindingResult bindingResult) {
		Category result;
		if (category.getId() == 0) {
			result = category;
			this.validator.validate(result, bindingResult);
		} else {

			result = this.categoryRepository.findOne(category.getId());

			result.setDescription(category.getDescription());
			result.setName(category.getName());

			this.validator.validate(result, bindingResult);

		}

		return result;

	}

	public Collection<ShoppingGroup> shFromCategory(final int categoryId) {

		Collection<ShoppingGroup> shs;

		shs = this.categoryRepository.shFromCategory(categoryId);

		return shs;
	}
}
