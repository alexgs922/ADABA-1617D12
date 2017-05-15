
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.OrderDomainRepository;
import domain.OrderDomain;

@Service
@Transactional
public class OrderDomainService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private OrderDomainRepository	orderRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public OrderDomainService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public OrderDomain create() {
		OrderDomain res;
		res = new OrderDomain();
		return res;
	}

	public Collection<OrderDomain> findAll() {
		Collection<OrderDomain> res;
		res = this.orderRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public OrderDomain findOne(final int orderId) {
		OrderDomain res;
		res = this.orderRepository.findOne(orderId);
		Assert.notNull(res);
		return res;
	}

	public OrderDomain save(final OrderDomain o) {
		Assert.notNull(o);
		return this.orderRepository.save(o);

	}

	public void delete(final OrderDomain o) {
		Assert.notNull(o);
		this.orderRepository.delete(o);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.orderRepository.flush();
	}
}
