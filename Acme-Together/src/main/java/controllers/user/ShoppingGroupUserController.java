
package controllers.user;

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

import services.ProductService;
import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.Product;
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

	@Autowired
	private ProductService			productService;


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
		result.addObject("principal", this.userService.findByPrincipal());
		return result;

	}

	// Add product to shopping group ------------------------------------------------------

	@RequestMapping(value = "/addProduct", method = RequestMethod.GET)
	public ModelAndView addProduct(@RequestParam final int shoppingGroupId) {
		ModelAndView res;

		Product product;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		product = this.productService.create();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);

		try {
			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
			res = this.createCreateModelAndView(product);
			res.addObject("shoppingGroup", shoppingGroup);
		} catch (final Throwable th) {
			res = new ModelAndView("forbiddenOperation");
		}

		return res;

	}

	@RequestMapping(value = "/addProduct", method = RequestMethod.POST, params = "save")
	public ModelAndView addProduct(@ModelAttribute("product") final Product product, final BindingResult binding, @RequestParam final int shoppingGroupId) {
		ModelAndView res;

		final Product productRes;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		product.setShoppingGroupProducts(shoppingGroup);
		product.setUserProduct(principal);

		productRes = this.productService.reconstruct(product, binding);

		if (binding.hasErrors()) {
			res = this.createCreateModelAndView(product);
			res.addObject("shoppingGroup", shoppingGroup);
		} else
			try {
				this.productService.saveAndFlush(productRes);
				shoppingGroup.getProducts().add(productRes);
				this.shoppingGroupService.save(shoppingGroup);
				res = new ModelAndView("redirect: display.do?shoppingGroupId=" + shoppingGroup.getId());
			} catch (final Throwable th) {
				res = new ModelAndView("forbiddenOperation");
			}

		return res;
	}

	// Edit product in shopping group ------------------------------------------

	@RequestMapping(value = "/editProduct", method = RequestMethod.GET)
	public ModelAndView editProduct(@RequestParam final int productId) {
		ModelAndView res;

		Product product;
		User principal;

		principal = this.userService.findByPrincipal();
		product = this.productService.findOne(productId);

		try {
			Assert.isTrue(product.getUserProduct().getId() == principal.getId());
			res = this.createEditModelAndView(product);
			res.addObject("shoppingGroup", product.getShoppingGroupProducts());

		} catch (final Throwable th) {
			res = new ModelAndView("forbiddenOperation");
		}

		return res;

	}

	@RequestMapping(value = "/editProduct", method = RequestMethod.POST, params = "save")
	public ModelAndView editProduct(@ModelAttribute("product") final Product product, final BindingResult binding) {
		ModelAndView res;

		final Product productRes;
		User principal;

		principal = this.userService.findByPrincipal();
		product.setUserProduct(principal);

		productRes = this.productService.reconstruct(product, binding);

		if (binding.hasErrors())
			if (binding.getGlobalError() != null)

				res = this.createEditModelAndView(product, binding.getGlobalError().getCode());

			else

				res = this.createEditModelAndView(product);
		else
			try {

				this.productService.saveAndFlush(productRes);
				res = new ModelAndView("redirect: display.do?shoppingGroupId=" + productRes.getShoppingGroupProducts().getId());
			} catch (final Throwable th) {
				res = new ModelAndView("forbiddenOperation");
			}

		return res;
	}

	// Delete product ----------------------------------------------------------------------

	@RequestMapping(value = "/deleteProduct", method = RequestMethod.GET)
	public ModelAndView deleteProduct(@RequestParam final int productId) {
		ModelAndView res;

		Product product;
		User principal;

		principal = this.userService.findByPrincipal();
		product = this.productService.findOne(productId);

		try {
			Assert.isTrue(product.getUserProduct().getId() == principal.getId());
			this.productService.delete(product);
			res = new ModelAndView("redirect: display.do?shoppingGroupId=" + product.getShoppingGroupProducts().getId());

		} catch (final Throwable th) {
			res = new ModelAndView("forbiddenOperation");
		}

		return res;

	}

	//  Ancillary methods -------------------------------------------------------

	protected ModelAndView createCreateModelAndView(final Product product) {
		ModelAndView res;

		res = this.createCreateModelAndView(product, null);

		return res;
	}

	protected ModelAndView createCreateModelAndView(final Product product, final String message) {
		ModelAndView result;

		result = new ModelAndView("product/addProduct");
		result.addObject("product", product);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Product product) {
		ModelAndView res;

		res = this.createEditModelAndView(product, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final Product product, final String message) {
		ModelAndView result;

		result = new ModelAndView("product/editProduct");
		result.addObject("product", product);
		result.addObject("message", message);

		return result;
	}

}
