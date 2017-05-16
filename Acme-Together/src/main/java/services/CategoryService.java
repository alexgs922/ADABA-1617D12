
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

@Service
@Transactional
public class CategoryService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CategoryRepository		categoryRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;


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
		Assert.notNull(c);
		return this.categoryRepository.saveAndFlush(c);

	}

	public void delete(final Category c) {
		Assert.notNull(c);
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

		if (category.getId() == 0)
			result = category;
		else {

			result = new Category();

			result.setDescription(category.getDescription());
			result.setName(category.getName());

			this.validator.validate(result, bindingResult);
		}
		return result;

	}
}
