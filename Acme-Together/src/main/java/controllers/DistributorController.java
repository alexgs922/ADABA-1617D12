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
	private DistributorService distributorService;


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

}
