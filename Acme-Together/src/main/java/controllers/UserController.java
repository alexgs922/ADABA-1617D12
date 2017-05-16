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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.UserService;
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

	
	

}
