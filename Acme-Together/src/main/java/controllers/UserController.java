/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CreditCardService;
import services.UserService;
import domain.CreditCard;
import domain.User;
import forms.RegistrationForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public UserController() {
		super();
	}


	@Autowired
	private UserService	userService;

	@Autowired
	private CreditCardService	creditCardService;

	
	// Creation ---------------------------------------------------------------
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		RegistrationForm user;

		user = new RegistrationForm();
		result = this.createEditModelAndView(user);

		return result;

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("user") @Valid final RegistrationForm form, final BindingResult binding) {

		ModelAndView result;
		User user;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(form, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(form);
		} else
			try {
				user = this.userService.reconstruct(form);
				this.userService.save(user);

				result = new ModelAndView("redirect:../security/login.do");
			} catch (final DataIntegrityViolationException exc) {
				result = this.createEditModelAndView(form, "user.duplicated.user");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "user.commit.error");
			}
		return result;
	}

	//Edit profile

		@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int userId) {
			ModelAndView res;
			User user;
			int principal;
			user = this.userService.findOne(userId);
			principal = this.userService.findByPrincipal().getId();
			Assert.isTrue(principal == userId);

			try {
				Assert.isTrue(principal == userId);
			} catch (final Throwable th) {
				res = this.createEditModelAndViewError(user);
				return res;
			}
			Assert.notNull(user);
			res = this.createEditModelAndView(user);
			return res;
		}

		@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
		public ModelAndView save(final User user, final BindingResult binding) {
			ModelAndView result;
			if (binding.hasErrors()) {
				if (binding.getGlobalError() != null)
					result = this.createEditModelAndView(user, binding.getGlobalError().getCode());
				else
					result = this.createEditModelAndView(user);
			} else
				try {
					final User user1 = this.userService.reconstruct(user, binding);
					this.userService.save(user1);
					result = new ModelAndView("redirect:../user/profile.do?userId=" + user.getId());
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(user, "user.commit.error");
				}
			return result;
		}

	
	// Terms of Use -----------------------------------------------------------
	@RequestMapping("/dataProtection")
	public ModelAndView dataProtection() {
		ModelAndView result;
		result = new ModelAndView("user/dataProtection");
		return result;

	}

	// Profile
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		ModelAndView result;
		User user;
		User principal;

		user = this.userService.findOne(userId);
		principal = this.userService.findByPrincipal();

		boolean toCreditCard = false;

		final CreditCard creditCard = user.getCreditCard();
		if (creditCard != null)
			toCreditCard = true;

		result = new ModelAndView("user/profile");
		result.addObject("user", user);
		result.addObject("principal", principal);
		result.addObject("creditCard", creditCard);
		result.addObject("toCreditCard", toCreditCard);
		result.addObject("requestURI", "user/profile.do?userId=" + userId);

		return result;
	}

	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile() {
		return this.display(this.userService.findByPrincipal().getId());

	}

	//CreditCard
	
	@RequestMapping(value = "/createCreditCard", method = RequestMethod.GET)
	public ModelAndView createCreditCard() {
		ModelAndView result;
		CreditCard creditCard;

		creditCard = this.creditCardService.create();

		result = this.editCreditCardModelAndView(creditCard);

		return result;
	}

	@RequestMapping(value = "/createCreditCard", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final CreditCard creditCard, final BindingResult binding) {

		ModelAndView result;
		CreditCard res;
		final User user = this.userService.findByPrincipal();
		res = this.creditCardService.reconstruct(creditCard, binding);
		if (binding.hasErrors())
			result = this.editCreditCardModelAndView(res);
		else
			try {
				this.userService.saveAndFlush2(user, creditCard);
				result = new ModelAndView("redirect:../user/profile.do?userId=" + user.getId());

			} catch (final Throwable th) {
				result = this.editCreditCardModelAndView(res, "creditCard.commit.error");

			}

		return result;
	}

	// Edit CreditCard ----------------------------------

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.GET)
	public ModelAndView editCreditCard(@RequestParam final int userId) {
		ModelAndView res;
		CreditCard creditCard;
		User principal;
		principal = this.userService.findByPrincipal();
		creditCard = this.userService.findOne(userId).getCreditCard();
		try {
			Assert.isTrue(principal.getId() == userId);
		} catch (final Throwable th) {
			res = this.createEditCreditCardModelAndViewError(creditCard);
			return res;
		}
		Assert.notNull(creditCard);
		res = this.createEditCreditCardModelAndView(creditCard);
		return res;
	}

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreditCard(@Valid final CreditCard creditCard, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("user/editCreditCard");
			result.addObject("creditCard", creditCard);
			result.addObject("forbiddenOperation", false);
		} else
			try {
				final User t = this.userService.findByPrincipal();
				CreditCard creditCard1 = this.creditCardService.reconstruct(creditCard, binding);
				if(binding.hasErrors()==false){
				this.userService.saveAndFlush2(t, creditCard1);	
				}
				result = new ModelAndView("redirect:../user/profile.do?userId=" + t.getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("user/editCreditCard");
				result.addObject("creditCard", creditCard);
				result.addObject("message", "user.commit.error");
			}
		return result;

	}

	
	
	// Other methods

	protected ModelAndView createEditModelAndView(final RegistrationForm user) {
		ModelAndView result;
		result = this.createEditModelAndView(user, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RegistrationForm user, final String message) {
		ModelAndView result;
		result = new ModelAndView("user/register");
		result.addObject("user", user);
		result.addObject("message", message);
		return result;

	}

	protected ModelAndView editCreditCardModelAndView(final CreditCard creditCard) {
		ModelAndView res;
		res = this.editCreditCardModelAndView(creditCard, null);
		return res;
	}

	protected ModelAndView editCreditCardModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;

		result = new ModelAndView("user/createCreditCard");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	protected ModelAndView createEditCreditCardModelAndView(final CreditCard creditCard) {
		ModelAndView result;
		result = this.createEditCreditCardModelAndView(creditCard, null);
		return result;

	}

	protected ModelAndView createEditCreditCardModelAndViewError(final CreditCard creditCard) {
		ModelAndView res;
		res = this.createEditCreditCardModelAndViewError(creditCard, null);
		return res;
	}

	protected ModelAndView createEditCreditCardModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;

		result = new ModelAndView("user/editCreditCard");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	protected ModelAndView createEditCreditCardModelAndViewError(final CreditCard creditCard, final String message) {
		ModelAndView res;
		res = new ModelAndView("user/editCreditCard");
		res.addObject("creditCard", creditCard);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

	protected ModelAndView createEditModelAndView(final User user) {
		ModelAndView result;
		result = this.createEditModelAndView(user, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final User user) {
		ModelAndView res;
		res = this.createEditModelAndViewError(user, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(final User user, final String message) {
		ModelAndView result;
		result = new ModelAndView("user/editProfile");
		result.addObject("user", user);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final User user, final String message) {
		ModelAndView res;
		res = new ModelAndView("user/editProfile");
		res.addObject("user", user);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}


	
}
