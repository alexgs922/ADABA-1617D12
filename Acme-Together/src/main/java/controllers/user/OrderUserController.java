
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.OrderDomainService;
import services.UserService;
import controllers.AbstractController;
import domain.OrderDomain;
import domain.Product;
import domain.User;

@Controller
@RequestMapping("/order/user")
public class OrderUserController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public OrderUserController() {
		super();
	}


	// Services -------------------------------------------------------------------

	@Autowired
	private OrderDomainService	orderService;

	@Autowired
	private UserService			userService;


	// List user orders ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<OrderDomain> orders;
		User principal;

		principal = this.userService.findByPrincipal();

		orders = principal.getOrders();

		res = new ModelAndView("order/list");
		res.addObject("orders", orders);
		res.addObject("requestURI", "/order/user/list.do");

		return res;

	}

	//Mark as a received order -----------------------------------------------

	@RequestMapping(value = "/markAsAReceived", method = RequestMethod.GET)
	public ModelAndView markAsAReceived(@RequestParam final int orderId) {
		ModelAndView result;
		User principal;
		OrderDomain order;
		List<Product> orderProducts;

		order = this.orderService.findOne(orderId);
		principal = this.userService.findByPrincipal();
		orderProducts = new ArrayList<Product>(order.getProducts());

		try {
			Assert.isTrue(orderProducts.get(0).getShoppingGroupProducts().getCreator().getId() == principal.getId());
			this.orderService.markAsAReceived(order);
			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable th) {
			result = new ModelAndView("forbiddenOperation");

		}

		return result;

	}
}
