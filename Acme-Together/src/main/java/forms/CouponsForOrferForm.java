
package forms;

import javax.validation.Valid;

import domain.Coupon;

public class CouponsForOrferForm {

	Coupon	coupon;


	@Valid
	public Coupon getCoupon() {
		return this.coupon;
	}

	public void setCoupon(final Coupon coupon) {
		this.coupon = coupon;
	}

}
