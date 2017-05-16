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

import services.CommercialService;
import domain.Commercial;
import forms.CommercialForm;

@Controller
@RequestMapping("/commercial")
public class ComercialController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ComercialController() {
		super();
	}


	@Autowired
	private CommercialService	comercialService;


	// Creation ---------------------------------------------------------------
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		CommercialForm commercial;

		commercial = new CommercialForm();
		result = this.createEditModelAndView(commercial);

		return result;

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("commercial") @Valid final CommercialForm form, final BindingResult binding) {

		ModelAndView result;
		Commercial commercial;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(form, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(form);
		} else
			try {
				commercial = this.comercialService.reconstruct(form);
				this.comercialService.save(commercial);

				result = new ModelAndView("redirect:../security/login.do");
			} catch (final DataIntegrityViolationException exc) {
				result = this.createEditModelAndView(form, "commercial.duplicated.commercial");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "commercial.commit.error");
			}
		return result;
	}

	// Terms of Use -----------------------------------------------------------
	@RequestMapping("/dataProtection")
	public ModelAndView dataProtection() {
		ModelAndView result;
		result = new ModelAndView("commercial/dataProtection");
		return result;

	}
	
	// Other methods

	protected ModelAndView createEditModelAndView(final CommercialForm commercial) {
		ModelAndView result;
		result = this.createEditModelAndView(commercial, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final CommercialForm commercial, final String message) {
		ModelAndView result;
		result = new ModelAndView("commercial/register");
		result.addObject("commercial", commercial);
		result.addObject("message", message);
		return result;

	}

}
