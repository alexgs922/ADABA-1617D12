
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CouponRepository;
import domain.Commercial;
import domain.Coupon;
import domain.OrderDomain;

@Service
@Transactional
public class CouponService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CouponRepository	couponRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private CommercialService	commercialService;


	// Constructors -----------------------------------------------------------

	public CouponService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Coupon create() {
		Coupon res;
		final Commercial principal = this.commercialService.findByPrincipal();
		Assert.notNull(principal);

		res = new Coupon();

		return res;
	}

	public Collection<Coupon> findAll() {
		Collection<Coupon> res;
		res = this.couponRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Coupon findOne(final int couponId) {
		Coupon res;
		res = this.couponRepository.findOne(couponId);
		Assert.notNull(res);
		return res;
	}

	public Coupon save(final Coupon c) {
		Assert.notNull(c);
		return this.couponRepository.save(c);
	}

	public Coupon save2(Coupon coupon) {
		Assert.notNull(coupon);
		final Commercial c = this.commercialService.findByPrincipal();
		coupon = this.couponRepository.save(coupon);
		c.getCoupons().add(coupon);
		this.commercialService.save(c);
		return coupon;
	}

	public Coupon saveEdit(Coupon coupon) {
		Assert.notNull(coupon);
		final Commercial c = this.commercialService.findByPrincipal();
		Assert.isTrue(c.getId() == coupon.getCommercial().getId());
		coupon = this.couponRepository.save(coupon);

		return coupon;
	}

	public void delete(final Coupon c) {
		Assert.notNull(c);
		this.couponRepository.delete(c);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.couponRepository.flush();
	}


	@Autowired
	private Validator	validator;


	public Coupon reconstruct(final Coupon coupon, final BindingResult binding) {
		Coupon result;

		if (coupon.getId() == 0) {
			result = coupon;
			final Commercial commercial = this.commercialService.findByPrincipal();
			Collection<OrderDomain> orders;
			orders = new ArrayList<OrderDomain>();
			result.setCommercial(commercial);
			result.setOrders(orders);

			this.validator.validate(result, binding);

		} else {

			result = this.couponRepository.findOne(coupon.getId());

			result.setCouponNumber(coupon.getCouponNumber());
			result.setDiscount(coupon.getDiscount());

			this.validator.validate(result, binding);

		}

		return result;

	}
}
