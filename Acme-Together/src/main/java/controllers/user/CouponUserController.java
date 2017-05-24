
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CouponService;
import services.UserService;
import controllers.AbstractController;
import domain.Coupon;
import domain.User;

@Controller
@RequestMapping("/coupon/user")
public class CouponUserController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService		userService;

	@Autowired
	private CouponService	couponService;


	// Constructor ------------------------------------------------------------

	public CouponUserController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		User user;
		Collection<Coupon> coupons;

		user = this.userService.findByPrincipal();
		coupons = this.couponService.findAll();

		result = new ModelAndView("coupon/user/list");
		result.addObject("coupons", coupons);
		result.addObject("principal", user);
		result.addObject("requestURI", "coupon/user/list.do");

		return result;
	}

}