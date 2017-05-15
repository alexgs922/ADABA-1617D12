
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProductRepository;
import domain.Product;

@Service
@Transactional
public class ProductService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ProductRepository	productRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ProductService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Product create() {
		Product res;
		res = new Product();
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
		Assert.notNull(p);
		return this.productRepository.save(p);

	}

	public void delete(final Product p) {
		Assert.notNull(p);
		this.productRepository.delete(p);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.productRepository.flush();
	}
}
