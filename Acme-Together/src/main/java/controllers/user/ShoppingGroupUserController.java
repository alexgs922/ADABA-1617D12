
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.ShoppingGroup;
import domain.User;

@Controller
@RequestMapping("/shoppingGroup/user")
public class ShoppingGroupUserController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ShoppingGroupUserController() {
		super();
	}


	// Services -------------------------------------------------------------------

	@Autowired
	private ShoppingGroupService	shoppingGroupService;

	@Autowired
	private UserService				userService;


	// List my joined shoppingGroups ----------------------------------------------

	@RequestMapping(value = "/joinedShoppingGroups", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<ShoppingGroup> shoppingGroups;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroups = principal.getShoppingGroup();

		res = new ModelAndView("shoppingGroup/list2");
		res.addObject("shoppingGroups", shoppingGroups);
		res.addObject("requestURI", "/shoppingGroup/user/joinedShoppingGroups.do");
		res.addObject("principal", principal);

		return res;

	}

	//Lista de shoppings groups públicos del sistema para un usuario y los privados a los que el propio usuario pertenece

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list2() {

		ModelAndView result;
		Collection<ShoppingGroup> sGToShow;
		Collection<ShoppingGroup> joinedGroups;
		User principal;

		sGToShow = this.shoppingGroupService.listPublicForUsersOfSH();

		principal = this.userService.findByPrincipal();

		joinedGroups = principal.getShoppingGroup();

		result = new ModelAndView("shoppingGroup/list2");
		result.addObject("shoppingGroups", sGToShow);
		result.addObject("joinedGroups", joinedGroups);
		result.addObject("requestURI", "shoppingGroup/user/list.do");
		result.addObject("principal", principal);

		return result;

	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int shoppingGroupId) {
		ModelAndView result;
		final ShoppingGroup sGToShow = this.shoppingGroupService.findOne(shoppingGroupId);

		try {

			final User principal = this.userService.findByPrincipal();
			Assert.isTrue(sGToShow.isPrivate_group() == false || sGToShow.getUsers().contains(principal));

		} catch (final Exception e) {
			result = new ModelAndView("forbiddenOperation");
			return result;
		}

		result = new ModelAndView("shoppingGroup/display");
		result.addObject("shoppingGroup", sGToShow);
		result.addObject("requestURI", "shoppingGroup/user/display.do?shoppingGroupId=" + shoppingGroupId);
		result.addObject("category", sGToShow.getCategory());
		result.addObject("users", sGToShow.getUsers());
		result.addObject("products", sGToShow.getProducts());
		result.addObject("comments", sGToShow.getComments());

		return result;

	}

}
