
package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Coupon;
import domain.Distributor;

public class CouponsForOrferForm {

	Coupon		coupon;
	Distributor	distributor;


	@NotNull
	@Valid
	public Distributor getDistributor() {
		return this.distributor;
	}

	public void setDistributor(final Distributor distributor) {
		this.distributor = distributor;
	}

	@Valid
	public Coupon getCoupon() {
		return this.coupon;
	}

	public void setCoupon(final Coupon coupon) {
		this.coupon = coupon;
	}

}
