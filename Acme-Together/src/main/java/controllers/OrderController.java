/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CouponService;
import services.DistributorService;
import services.OrderDomainService;
import services.WarehouseService;
import domain.Distributor;
import domain.OrderDomain;
import domain.Status;
import domain.Warehouse;

@Controller
@RequestMapping("/order")
public class OrderController extends AbstractController {

	@Autowired
	private OrderDomainService	orderDomainService;

	@Autowired
	private DistributorService	distributorService;

	@Autowired
	private CouponService	couponService;

	@Autowired
	private WarehouseService	warehouseService;


	// Change Status of the Order ---------------------------------------------------------------		

	@RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
	public ModelAndView listOrders(@RequestParam final int orderId) {

		ModelAndView res;

		final OrderDomain order = this.orderDomainService.findOne(orderId);
		if (order.getStatus().equals(Status.INPROCESS))
		order.setStatus(Status.SENT);
		this.orderDomainService.save(order);

		res = new ModelAndView("redirect:../warehouse/myWarehouses.do");

		return res;
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public ModelAndView removeOrders(@RequestParam final int orderId) {

		ModelAndView res;
		res = new ModelAndView();
		
		final OrderDomain order = this.orderDomainService.findOne(orderId);
		if (order.getStatus().equals(Status.RECEIVED)){

			
			Distributor d = this.distributorService.findByPrincipal();
			Collection<Warehouse> coll = d.getWarehouses();
			List<Warehouse> list = new ArrayList<Warehouse>(coll);
			
			for(Warehouse w:list){
				if(w.getOrders().contains(order)){
					w.getOrders().remove(order);
				}
			}
			d.setWarehouses(list);
			this.distributorService.save(d);
			
			
			res = new ModelAndView("redirect:../warehouse/myWarehouses.do");

			}
		
			
		
		return res;			
		
		}
	
	
}
