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

import java.util.Collection;

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

import services.DistributorService;
import domain.Distributor;
import forms.DistributorForm;

@Controller
@RequestMapping("/distributor")
public class DistributorController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public DistributorController() {
		super();
	}


	@Autowired
	private DistributorService	distributorService;


	// Creation ---------------------------------------------------------------
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		DistributorForm distributor;

		distributor = new DistributorForm();
		result = this.createEditModelAndView(distributor);

		return result;

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("distributor") @Valid final DistributorForm form, final BindingResult binding) {

		ModelAndView result;
		Distributor distributor;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(form, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(form);
		} else
			try {
				distributor = this.distributorService.reconstruct(form);
				this.distributorService.save(distributor);

				result = new ModelAndView("redirect:../security/login.do");
			} catch (final DataIntegrityViolationException exc) {
				result = this.createEditModelAndView(form, "distributor.duplicated.distributor");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "distributor.commit.error");
			}
		return result;
	}

	// Terms of Use -----------------------------------------------------------
	@RequestMapping("/dataProtection")
	public ModelAndView dataProtection() {
		ModelAndView result;
		result = new ModelAndView("distributor/dataProtection");
		return result;

	}

	//Edit profile

	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int distributorId) {
		ModelAndView res;
		Distributor distributor;
		int principal;
		distributor = this.distributorService.findOne(distributorId);
		principal = this.distributorService.findByPrincipal().getId();
		Assert.isTrue(principal == distributorId);

		try {
			Assert.isTrue(principal == distributorId);
		} catch (final Throwable th) {
			res = this.createEditModelAndViewError(distributor);
			return res;
		}
		Assert.notNull(distributor);
		res = this.createEditModelAndView(distributor);
		return res;
	}

	@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Distributor distributor, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(distributor, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(distributor);
		} else
			try {
				final Distributor distributor1 = this.distributorService.reconstruct(distributor, binding);
				this.distributorService.save(distributor1);
				result = new ModelAndView("redirect:../distributor/profile.do?distributorId=" + distributor.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(distributor, "distributor.commit.error");
			}
		return result;
	}

	// Profile

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int distributorId) {
		ModelAndView result;
		Distributor distributor;
		Distributor principal;

		distributor = this.distributorService.findOne(distributorId);
		principal = this.distributorService.findByPrincipal();

		result = new ModelAndView("distributor/profile");
		result.addObject("distributor", distributor);
		result.addObject("principal", principal);
		result.addObject("requestURI", "distributor/profile.do?distributorId=" + distributorId);

		return result;
	}

	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile() {
		return this.display(this.distributorService.findByPrincipal().getId());

	}

	// List Distributors

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<Distributor> distributors;

		distributors = this.distributorService.findAll();

		res = new ModelAndView("distributor/list");
		res.addObject("distributors", distributors);
		res.addObject("requestURI", "/distributor/list.do");

		return res;
	}

	// Other methods

	protected ModelAndView createEditModelAndView(final DistributorForm distributor) {
		ModelAndView result;
		result = this.createEditModelAndView(distributor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final DistributorForm distributor, final String message) {
		ModelAndView result;
		result = new ModelAndView("distributor/register");
		result.addObject("distributor", distributor);
		result.addObject("message", message);
		return result;

	}

	protected ModelAndView createEditModelAndView(final Distributor distributor) {
		ModelAndView result;
		result = this.createEditModelAndView(distributor, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Distributor distributor) {
		ModelAndView res;
		res = this.createEditModelAndViewError(distributor, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(final Distributor distributor, final String message) {
		ModelAndView result;
		result = new ModelAndView("distributor/editProfile");
		result.addObject("distributor", distributor);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Distributor distributor, final String message) {
		ModelAndView res;
		res = new ModelAndView("distributor/editProfile");
		res.addObject("distributor", distributor);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

}
