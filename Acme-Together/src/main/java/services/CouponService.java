
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CouponRepository;
import domain.Coupon;

@Service
@Transactional
public class CouponService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CouponRepository	couponRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public CouponService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Coupon create() {
		Coupon res;
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

	public void delete(final Coupon c) {
		Assert.notNull(c);
		this.couponRepository.delete(c);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.couponRepository.flush();
	}
}
