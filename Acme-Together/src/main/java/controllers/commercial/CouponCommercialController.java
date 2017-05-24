package controllers.commercial;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CommercialService;
import services.CouponService;
import controllers.AbstractController;
import domain.Commercial;
import domain.Coupon;

@Controller
@RequestMapping("/coupon/commercial")
public class CouponCommercialController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private CommercialService	commercialService;

	@Autowired
	private CouponService		couponService;


	// Constructor ------------------------------------------------------------

	public CouponCommercialController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;
		Commercial commercial;
		Collection<Coupon> coupons;

		commercial = this.commercialService.findByPrincipal();
		coupons = commercial.getCoupons();

		result = new ModelAndView("coupon/commercial/list");
		result.addObject("coupons", coupons);
		result.addObject("principal", commercial);
		result.addObject("requestURI", "coupon/commercial/list.do");

		return result;
	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Coupon coupon;

		coupon = new Coupon();

		result = new ModelAndView("coupon/commercial/edit");
		result.addObject("coupon", coupon);
		result.addObject("requestURI", "coupon/commercial/create.do");

		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Coupon coupon, final BindingResult bindingResult) {
		ModelAndView result;

		final Commercial c = this.commercialService.findByPrincipal();

		coupon = this.couponService.reconstruct(coupon, bindingResult);

		if (bindingResult.hasErrors())

			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(coupon, bindingResult.getGlobalError().getCode());

			else
				result = this.createEditModelAndView(coupon);

		else
			try {
				this.couponService.save2(coupon);
				result = new ModelAndView("redirect:/coupon/commercial/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(coupon, "coupon.commit.error");
			}

		return result;

	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int couponId) {
		ModelAndView result;
		Coupon coupon;

		coupon = this.couponService.findOne(couponId);
		Assert.notNull(coupon);

		final Commercial principal = this.commercialService.findByPrincipal();
		Assert.isTrue(principal.getId() == coupon.getCommercial().getId());

		result = this.createEditModelAndView(coupon);
		result.addObject("requestURI", "coupon/commercial/edit.do?couponId=" + couponId);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(Coupon coupon, final BindingResult binding) {
		ModelAndView result;

		try {
			coupon = this.couponService.reconstruct(coupon, binding);
		} catch (final Exception e) {
			result = this.createEditModelAndView(coupon);
			result.addObject("requestURI", "coupon/commercial/edit.do?couponId=" + coupon.getId());
		}

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(coupon);

			if (binding.getGlobalError() != null) {
				result = this.createEditModelAndView(coupon, binding.getGlobalError().getCode());
				result.addObject("requestURI", "coupon/commercial/edit.do?couponId=" + coupon.getId());
			} else {
				result = this.createEditModelAndView(coupon);
				result.addObject("requestURI", "coupon/commercial/edit.do?couponId=" + coupon.getId());
			}
		} else
			try {
				this.couponService.saveEdit(coupon);
				result = new ModelAndView("redirect:/coupon/commercial/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(coupon, "coupon.commit.error");
				result.addObject("requestURI", "coupon/commercial/edit.do?couponId=" + coupon.getId());
			}

		return result;
	}

	// Delete ----------------------------------------------------------------

	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int couponId) {
		ModelAndView result;

		Coupon coupon;

		coupon = this.couponService.findOne(couponId);
		Assert.notNull(coupon);

		try {
			final Commercial principal = this.commercialService.findByPrincipal();
			Assert.isTrue(principal.getId() == coupon.getCommercial().getId());
		} catch (final Exception e) {
			result = new ModelAndView("coupon/forboperation");
			result.addObject("forbiddenOperation", "coupon.not.your.coupon.delete");
			result.addObject("cancelURL", "coupon/commercial/list.do");
			return result;
		}

		try {

			this.couponService.delete(coupon);
			result = new ModelAndView("redirect:/coupon/commercial/list.do");

		} catch (final Throwable th) {

			result = new ModelAndView("coupon/forboperation");
			result.addObject("forbiddenOperation", "coupon.commit.error");
			result.addObject("cancelURL", "coupon/commercial/list.do");
			return result;
		}

		return result;

	}

	//-------------------------------------------------------------------------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Coupon coupon) {
		ModelAndView result;

		result = this.createEditModelAndView(coupon, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Coupon coupon, final String message) {
		ModelAndView result;

		result = new ModelAndView("coupon/commercial/edit");
		result.addObject("coupon", coupon);
		result.addObject("message", message);
		result.addObject("requestURI", "coupon/commercial/create.do");

		return result;
	}
}