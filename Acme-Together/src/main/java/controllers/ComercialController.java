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

import services.CommercialService;
import domain.Commercial;
import domain.Distributor;
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
	
	
	//Edit profile

			@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
			public ModelAndView edit(@RequestParam final int commercialId) {
				ModelAndView res;
				Commercial commercial;
				int principal;
				commercial = this.comercialService.findOne(commercialId);
				principal = this.comercialService.findByPrincipal().getId();
				Assert.isTrue(principal == commercialId);

				try {
					Assert.isTrue(principal == commercialId);
				} catch (final Throwable th) {
					res = this.createEditModelAndViewError(commercial);
					return res;
				}
				Assert.notNull(commercial);
				res = this.createEditModelAndView(commercial);
				return res;
			}

			@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
			public ModelAndView save(final Commercial commercial, final BindingResult binding) {
				ModelAndView result;
				if (binding.hasErrors()) {
					if (binding.getGlobalError() != null)
						result = this.createEditModelAndView(commercial, binding.getGlobalError().getCode());
					else
						result = this.createEditModelAndView(commercial);
				} else
					try {
						final Commercial commercial1 = this.comercialService.reconstruct(commercial, binding);
						this.comercialService.save(commercial1);
						result = new ModelAndView("redirect:../commercial/profile.do?commercialId=" + commercial.getId());
					} catch (final Throwable oops) {
						result = this.createEditModelAndView(commercial, "commercial.commit.error");
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

	
	// Profile
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int commercialId) {
		ModelAndView result;
		Commercial commercial;
		Commercial principal;

		commercial	= this.comercialService.findOne(commercialId);
		principal = this.comercialService.findByPrincipal();

		

		result = new ModelAndView("commercial/profile");
		result.addObject("commercial", commercial);
		result.addObject("principal", principal);
		result.addObject("requestURI", "commercial/profile.do?commercialId=" + commercialId);

		return result;
	}

	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile() {
		return this.display(this.comercialService.findByPrincipal().getId());

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

	protected ModelAndView createEditModelAndView(final Commercial commercial) {
		ModelAndView result;
		result = this.createEditModelAndView(commercial, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Commercial commercial) {
		ModelAndView res;
		res = this.createEditModelAndViewError(commercial, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(final Commercial commercial, final String message) {
		ModelAndView result;
		result = new ModelAndView("commercial/editProfile");
		result.addObject("commercial", commercial);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Commercial commercial, final String message) {
		ModelAndView res;
		res = new ModelAndView("commercial/editProfile");
		res.addObject("commercial", commercial);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}
	
	
}
