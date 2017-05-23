
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProductRepository;
import domain.Product;
import domain.User;

@Service
@Transactional
public class ProductService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ProductRepository	productRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService			userService;


	// Constructors -----------------------------------------------------------

	public ProductService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Product create() {
		Product res;
		User principal;

		principal = this.userService.findByPrincipal();

		res = new Product();
		res.setUserProduct(principal);
		return res;
	}

	public Collection<Product> findAll() {
		Collection<Product> res;
		res = this.productRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Product findOne(final int productId) {
		Product res;
		res = this.productRepository.findOne(productId);
		Assert.notNull(res);
		return res;
	}

	public Product save(final Product p) {
		Assert.isTrue(this.checkUserPrincipal());
		Assert.notNull(p);
		return this.productRepository.save(p);

	}

	public Product saveAndFlush(final Product p) {
		Assert.isTrue(this.checkUserPrincipal());
		Assert.notNull(p);
		return this.productRepository.saveAndFlush(p);

	}

	public void delete(final Product p) {
		Assert.isTrue(this.checkUserPrincipal());
		Assert.notNull(p);
		this.productRepository.delete(p);
	}

	//Other business methods --------------------------------------

	public boolean checkUserPrincipal() {
		final boolean res;
		User principal;

		principal = this.userService.findByPrincipal();

		res = principal != null;

		return res;
	}

	public void flush() {
		this.productRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public Product reconstruct(final Product product, final BindingResult binding) {
		Product result;

		if (product.getId() == 0)
			result = product;
		else {
			result = this.productRepository.findOne(product.getId());

			result.setName(product.getName());
			result.setUrl(product.getUrl());
			result.setReferenceNumber(product.getReferenceNumber());
			result.setPrice(product.getPrice());

		}

		this.validator.validate(result, binding);

		return result;
	}
}
