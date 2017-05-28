
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EngagementService;
import services.OrderDomainService;
import domain.Engagement;
import domain.OrderDomain;

@Controller
@RequestMapping("/engagement")
public class EngagementController extends AbstractController {

	@Autowired
	private OrderDomainService	orderDomainService;

	@Autowired
	private EngagementService	engagementService;


	// display engagement ----------------------------------------------------------

	@RequestMapping(value = "/see", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int orderId) {

		ModelAndView res;
		OrderDomain o;
		Engagement e;

		o = this.orderDomainService.findOne(orderId);
		e = o.getEngagement();

		res = new ModelAndView("engagements/details");
		res.addObject("lista", e.getListOrdersByUser());
		res.addObject("requestURI", "/engagement/see.do?orderId=" + orderId);

		return res;

	}

}
