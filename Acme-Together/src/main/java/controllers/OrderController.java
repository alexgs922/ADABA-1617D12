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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DistributorService;
import services.OrderDomainService;
import domain.Distributor;
import domain.OrderDomain;
import domain.Status;

@Controller
@RequestMapping("/order")
public class OrderController extends AbstractController {

	@Autowired
	private OrderDomainService	orderDomainService;

	@Autowired
	private DistributorService	distributorService;

	
	// Change Status of the Order ---------------------------------------------------------------		

	@RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
	public ModelAndView listOrders(@RequestParam final int orderId)  {
		
			ModelAndView res;

			OrderDomain order = 	this.orderDomainService.findOne(orderId);
			if(order.getStatus().equals(Status.INPROCESS))
			
			order.setStatus(Status.SENT);
			this.orderDomainService.save(order);
			
			res = new ModelAndView("redirect:../warehouse/myWarehouses.do");

			return res;
		}
	
	
	
}
