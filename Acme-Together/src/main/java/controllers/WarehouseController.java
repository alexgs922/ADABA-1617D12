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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DistributorService;
import services.WarehouseService;
import domain.Distributor;
import domain.OrderDomain;
import domain.Warehouse;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public WarehouseController() {
		super();
	}

	@Autowired
	private WarehouseService	warehouseService;

	@Autowired
	private DistributorService	distributorService;
	
	//List categories -------------------------------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<Warehouse> warehouses;

		warehouses = this.warehouseService.findAll();

		res = new ModelAndView("warehouse/list");
		res.addObject("warehouses", warehouses);
		res.addObject("requestURI", "/warehouse/list.do");

		return res;
	}

	//List my warehouses -------------------------------------------------------------------------------------

		@RequestMapping(value = "/myWarehouses", method = RequestMethod.GET)
		public ModelAndView listMyWarehouses() {

			ModelAndView res;
			Collection<Warehouse> warehouses;
			Distributor d = this.distributorService.findByPrincipal();
			warehouses = d.getWarehouses();
			
			res = new ModelAndView("warehouse/myWarehouses");
			res.addObject("warehouses", warehouses);

			return res;
		}
	//List my orders in a warehouse -------------------------------------------------------------------------------------

			@RequestMapping(value = "/myOrders", method = RequestMethod.GET)
			public ModelAndView listOrders(@RequestParam final int warehouseId)  {
				
					ModelAndView res;
					Collection<OrderDomain> orders;
					Warehouse w = 	this.warehouseService.findOne(warehouseId);
					Distributor principal = this.distributorService.findByPrincipal();

					Assert.isTrue(w.getDistributor().getId() == principal.getId());
					
					orders = w.getOrders();
					
					res = new ModelAndView("warehouse/myOrders");
					res.addObject("orders", orders);

					return res;
				}
		
		
	//Create Warehouse --------------------------------------------------------------------------------------

		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {

			ModelAndView res;
			Warehouse warehouse;

			warehouse = this.warehouseService.create();
			res = this.createCreateModelAndView(warehouse);

			return res;
		}

		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
		public ModelAndView create(@ModelAttribute("warehouse") final Warehouse warehouse, final BindingResult binding) {

			ModelAndView res;
			Warehouse warehouseRes;

			if (binding.hasErrors())
				res = this.createCreateModelAndView(warehouse);
			else
				try {
					warehouseRes = this.warehouseService.reconstruct(warehouse, binding);
					Distributor d = this.distributorService.findByPrincipal();
					this.distributorService.saveAndFlush2(d, warehouseRes);					
					res = new ModelAndView("redirect:../warehouse/myWarehouses.do");

				} catch (final Throwable tw) {
					res = this.createCreateModelAndView(warehouse, "warehouse.commit.error");
				}

			return res;
		}		
	
	//Edit My warehouses -------------------------------------------------------------------------------------
	
		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam final int warehouseId) {
			ModelAndView res;
			Distributor distributor;
			int principal;
			Warehouse w = this.warehouseService.findOne(warehouseId);
			distributor = w.getDistributor();
			principal = this.distributorService.findByPrincipal().getId();
			Assert.isTrue(principal == distributor.getId());

			try {
				Assert.isTrue(principal == distributor.getId());
			} catch (final Throwable th) {
				res = this.createEditModelAndViewError(w);
				return res;
			}
			Assert.notNull(w);
			res = this.createEditModelAndView(w);
			return res;
		}

		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(final Warehouse warehouse, final BindingResult binding) {
			ModelAndView result;
			
			if (binding.hasErrors()) {
				if (binding.getGlobalError() != null)
					result = this.createEditModelAndView(warehouse, binding.getGlobalError().getCode());
				else
					result = this.createEditModelAndView(warehouse);
			} else
				try {
					final Warehouse warehouse1 = this.warehouseService.reconstruct(warehouse, binding);
					this.warehouseService.save(warehouse1);
					result = new ModelAndView("redirect:../warehouse/myWarehouses.do");
				} catch (final Throwable oops) {
					result = this.createEditModelAndView(warehouse, "warehouse.commit.error");
				}
			return result;
		}

		// Delete Category ---------------------------------------------------------------

		@RequestMapping(value = "/delete", method = RequestMethod.GET)
		public ModelAndView delete(@RequestParam final int warehouseId) {
			ModelAndView res;
			Warehouse w;

			w = this.warehouseService.findOne(warehouseId);

			try {
				Distributor d = this.distributorService.findByPrincipal();
				Assert.isTrue(d.getId()==w.getDistributor().getId());
				this.warehouseService.delete(w);
				res = new ModelAndView("redirect:../warehouse/myWarehouses.do");

			} catch (final Throwable tw) {
				res = new ModelAndView("forbiddenOperation");
			}

			return res;
		}

		
		
		// OTHER METHODS
		
		
		protected ModelAndView createEditModelAndView(final Warehouse warehouse) {
			ModelAndView result;
			result = this.createEditModelAndView(warehouse, null);
			return result;
		}

		protected ModelAndView createEditModelAndViewError(final Warehouse warehouse) {
			ModelAndView res;
			res = this.createEditModelAndViewError(warehouse, null);
			return res;

		}

		protected ModelAndView createEditModelAndView(final Warehouse warehouse, final String message) {
			ModelAndView result;
			result = new ModelAndView("warehouse/edit");
			result.addObject("warehouse", warehouse);
			result.addObject("message", message);
			result.addObject("forbiddenOperation", false);
			return result;
		}

		protected ModelAndView createEditModelAndViewError(final Warehouse warehouse, final String message) {
			ModelAndView res;
			res = new ModelAndView("warehouse/edit");
			res.addObject("warehouse", warehouse);
			res.addObject("message", message);
			res.addObject("forbiddenOperation", true);
			return res;

		}

		protected ModelAndView createCreateModelAndView(final Warehouse warehouse) {
			ModelAndView res;

			res = this.createCreateModelAndView(warehouse, null);

			return res;
		}

		protected ModelAndView createCreateModelAndView(final Warehouse warehouse, final String message) {
			ModelAndView result;

			result = new ModelAndView("warehouse/create");
			result.addObject("warehouse", warehouse);
			result.addObject("message", message);

			return result;
		}

		
	
	
}
