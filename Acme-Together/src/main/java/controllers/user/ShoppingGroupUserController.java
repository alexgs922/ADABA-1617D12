
package controllers.user;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CategoryService;
import services.ProductService;
import services.PunctuationService;
import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.Category;
import domain.Product;
import domain.Punctuation;
import domain.ShoppingGroup;
import domain.User;
import forms.ShoppingGroupForm;
import forms.ShoppingGroupForm2;

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

	@Autowired
	private CategoryService			categoryService;

	@Autowired
	private PunctuationService		punctuationService;


	// List my joined shoppingGroups ----------------------------------------------

	@RequestMapping(value = "/joinedShoppingGroups", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView res;
		Collection<ShoppingGroup> shoppingGroups;
		Collection<ShoppingGroup> sh;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroups = principal.getMyShoppingGroups();
		sh = this.shoppingGroupService.ShoppingGroupsToWichBelongsAndNotCreatedBy(principal);

		res = new ModelAndView("shoppingGroup/list");
		res.addObject("myShoppingGroups", shoppingGroups);
		res.addObject("shoppingGroupsBelongs", sh);
		res.addObject("requestURI", "/shoppingGroup/user/joinedShoppingGroups.do");
		res.addObject("principal", principal);

		return res;

	}

	//Lista de shoppings groups públicos del sistema para un usuario y los privados a los que el propio usuario pertenece

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list2() {

		ModelAndView result;
		Collection<ShoppingGroup> sGToShow;
		User principal;

		sGToShow = this.shoppingGroupService.listPublicForUsersOfSH();

		principal = this.userService.findByPrincipal();

		result = new ModelAndView("shoppingGroup/list2");
		result.addObject("shoppingGroups", sGToShow);
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
		result.addObject("alreadyPunctuate", this.shoppingGroupService.alreadyPunctuate(sGToShow, this.userService.findByPrincipal()));
		result.addObject("principalPunctuation", this.punctuationService.getPunctuationByShoppingGroupAndUser(sGToShow, this.userService.findByPrincipal()));
		return result;

	}

	//Create a new Shopping Group  ------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final ShoppingGroupForm shoppingGroupForm;
		Collection<Category> cats;

		shoppingGroupForm = new ShoppingGroupForm();
		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit");
		result.addObject("shoppingGroup", shoppingGroupForm);
		result.addObject("categories", cats);
		result.addObject("requestURI", "shoppingGroup/user/create.do");

		return result;

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("shoppingGroup") @Valid final ShoppingGroupForm shoppingGroupForm, final BindingResult bindingResult) {

		ModelAndView result;
		ShoppingGroup shoppingGroup;

		if (bindingResult.hasErrors()) {

			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(shoppingGroupForm, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(shoppingGroupForm);

		} else
			try {
				shoppingGroup = this.shoppingGroupService.reconstruct(shoppingGroupForm, bindingResult);
				this.shoppingGroupService.save(shoppingGroup);
				result = new ModelAndView("redirect:joinedShoppingGroups.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(shoppingGroupForm, "sh.commit.error");
			}

		return result;

	}

	//Edit a new Shopping Group  ------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int shoppingGroupId) {
		ModelAndView result;
		final ShoppingGroup shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		final User principal = this.userService.findByPrincipal();

		try {
			//solo el creador del grupo puede editarlo
			Assert.isTrue(principal.getId() == shoppingGroup.getCreator().getId());
		} catch (final Exception e) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.not.mine");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;

		}

		try {
			//Si está a null significa que no está esperando ningún pedido, y por tanto está en estado 'inicial' y se puede modificar y eliminar
			Assert.isTrue(shoppingGroup.getLastOrderDate() == null);
		} catch (final Exception e) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.noteditableInList");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;

		}

		final ShoppingGroupForm2 shoppingGroupForm;

		Collection<Category> cats;

		shoppingGroupForm = new ShoppingGroupForm2();
		shoppingGroupForm.setName(shoppingGroup.getName());
		shoppingGroupForm.setCategory(shoppingGroup.getCategory());
		shoppingGroupForm.setDescription(shoppingGroup.getDescription());
		shoppingGroupForm.setFreePlaces(shoppingGroup.getFreePlaces());
		shoppingGroupForm.setPrivate_group(shoppingGroup.isPrivate_group());
		shoppingGroupForm.setSite(shoppingGroup.getSite());

		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit2");
		result.addObject("shoppingGroup", shoppingGroupForm);
		result.addObject("categories", cats);
		result.addObject("requestURI", "shoppingGroup/user/edit.do?shoppingGroupId=" + shoppingGroupId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@ModelAttribute("shoppingGroup") @Valid final ShoppingGroupForm2 shoppingGroupForm, @RequestParam final int shoppingGroupId, final BindingResult bindingResult) {

		ModelAndView result;
		ShoppingGroup shoppingGroup;

		if (bindingResult.hasErrors()) {

			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(shoppingGroupForm, shoppingGroupId, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(shoppingGroupForm, shoppingGroupId);

		} else
			try {
				try {

					shoppingGroup = this.shoppingGroupService.reconstruct2(shoppingGroupForm, shoppingGroupId, bindingResult);

				} catch (final IllegalArgumentException iae) {

					result = new ModelAndView("errorOperation");
					result.addObject("errorOperation", "sh.not.edit2");
					result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
					return result;
				}

				this.shoppingGroupService.save(shoppingGroup);
				result = new ModelAndView("redirect:joinedShoppingGroups.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(shoppingGroupForm, shoppingGroupId, "sh.commit.error");
			}

		return result;

	}

	//Delete a shoppingGroup ------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int shoppingGroupId) {
		ModelAndView result;

		final ShoppingGroup sh = this.shoppingGroupService.findOne(shoppingGroupId);
		Assert.notNull(sh);

		try {

			final User principal = this.userService.findByPrincipal();
			Assert.isTrue(principal.getId() == sh.getCreator().getId());

		} catch (final Exception e) {
			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.not.mine");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;
		}

		try {

			Assert.isNull(sh.getLastOrderDate());

		} catch (final Exception e) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.noteditableInList");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;

		}

		try {
			this.shoppingGroupService.delete(sh);
			result = new ModelAndView("redirect:joinedShoppingGroups.do");

		} catch (final Throwable th) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.commit.error");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");

		}

		return result;
	}

	// Punctuate a shopping group ---------------------------------------------------------

	@RequestMapping(value = "/punctuate", method = RequestMethod.GET)
	public ModelAndView punctuate(@RequestParam final int shoppingGroupId) {
		ModelAndView res;

		Punctuation punctuation;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		punctuation = this.punctuationService.create();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);

		try {
			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
			res = this.createCreateModelAndView(punctuation);
			res.addObject("shoppingGroup", shoppingGroup);
		} catch (final Throwable th) {
			res = new ModelAndView("forbiddenOperation");
		}

		return res;

	}

	@RequestMapping(value = "/punctuate", method = RequestMethod.POST, params = "save")
	public ModelAndView punctuate(@ModelAttribute("punctuation") final Punctuation punctuation, final BindingResult binding, @RequestParam final int shoppingGroupId) {
		ModelAndView res;

		final Punctuation punctuationRes;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		punctuation.setShoppingGroup(shoppingGroup);
		punctuation.setUser(principal);
		principal.getPunctuations().add(punctuation);
		shoppingGroup.getPunctuations().add(punctuation);

		punctuationRes = this.punctuationService.reconstruct(punctuation, binding);

		if (binding.hasErrors()) {
			res = this.createCreateModelAndView(punctuation);
			res.addObject("shoppingGroup", shoppingGroup);
		} else
			try {
				shoppingGroup.setPuntuation(shoppingGroup.getPuntuation() + punctuationRes.getValue());
				this.userService.save(principal);
				this.shoppingGroupService.save(shoppingGroup);
				this.punctuationService.saveAndFlush(punctuation);

				res = new ModelAndView("redirect: display.do?shoppingGroupId=" + shoppingGroup.getId());
			} catch (final Throwable th) {
				res = new ModelAndView("forbiddenOperation");
			}

		return res;
	}

	@RequestMapping(value = "/editPunctuation", method = RequestMethod.GET)
	public ModelAndView editPunctuate(@RequestParam final int shoppingGroupId) {
		ModelAndView res;

		Punctuation punctuation;
		ShoppingGroup shoppingGroup;
		User principal;
		int oldValue;

		principal = this.userService.findByPrincipal();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);

		punctuation = this.punctuationService.getPunctuationByShoppingGroupAndUser(shoppingGroup, principal);

		oldValue = punctuation.getValue();

		try {
			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
			res = this.createEditModelAndView(punctuation);
			res.addObject("shoppingGroup", shoppingGroup);
			res.addObject("oldValue", oldValue);
		} catch (final Throwable th) {
			res = new ModelAndView("forbiddenOperation");
		}

		return res;

	}

	@RequestMapping(value = "/editPunctuation", method = RequestMethod.POST, params = "save")
	public ModelAndView editPnctuate(@ModelAttribute("punctuation") final Punctuation punctuation, final BindingResult binding, @RequestParam final int shoppingGroupId, final int oldValue) {
		ModelAndView res;

		final Punctuation punctuationRes;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		punctuation.setShoppingGroup(shoppingGroup);
		punctuation.setUser(principal);

		punctuationRes = this.punctuationService.reconstruct(punctuation, binding);

		shoppingGroup.setPuntuation(shoppingGroup.getPuntuation() - oldValue + punctuationRes.getValue());

		if (binding.hasErrors()) {
			res = this.createEditModelAndView(punctuation);
			res.addObject("shoppingGroup", shoppingGroup);
		} else
			try {

				this.shoppingGroupService.save(shoppingGroup);
				this.punctuationService.saveAndFlush(punctuationRes);
				res = new ModelAndView("redirect: display.do?shoppingGroupId=" + shoppingGroup.getId());
			} catch (final Throwable th) {
				res = new ModelAndView("forbiddenOperation");
			}

		return res;
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
	public ModelAndView editProduct(@ModelAttribute("product") final Product product, final BindingResult binding, @RequestParam final int shoppingGroupId) {
		ModelAndView res;

		final Product productRes;
		User principal;
		ShoppingGroup shoppingGroup;

		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		principal = this.userService.findByPrincipal();
		product.setUserProduct(principal);
		product.setShoppingGroupProducts(shoppingGroup);

		productRes = this.productService.reconstruct(product, binding);

		if (binding.hasErrors())
			if (binding.getGlobalError() != null)

				res = this.createEditModelAndView(product, binding.getGlobalError().getCode());

			else {

				res = this.createEditModelAndView(product);
				res.addObject("shoppingGroup", product.getShoppingGroupProducts());
			}
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

	protected ModelAndView createEditModelAndView(final ShoppingGroupForm form) {
		ModelAndView res;

		res = this.createEditModelAndView(form, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final ShoppingGroupForm form, final String message) {
		ModelAndView result;

		Collection<Category> cats;
		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit");
		result.addObject("shoppingGroup", form);
		result.addObject("categories", cats);
		result.addObject("requestURI", "shoppingGroup/user/create.do");
		result.addObject("message", message);

		return result;
	}
	protected ModelAndView createEditModelAndView(final ShoppingGroupForm2 form, final int shoppingGroupId) {
		ModelAndView res;

		res = this.createEditModelAndView(form, shoppingGroupId, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final ShoppingGroupForm2 form, final int shoppingGroupId, final String message) {
		ModelAndView result;

		Collection<Category> cats;
		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit");
		result.addObject("shoppingGroup", form);
		result.addObject("categories", cats);
		result.addObject("requestURI", "shoppingGroup/user/edit.do?shoppingGroupId=" + shoppingGroupId);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createCreateModelAndView(final Punctuation punctuation) {
		ModelAndView res;

		res = this.createCreateModelAndView(punctuation, null);

		return res;
	}

	protected ModelAndView createCreateModelAndView(final Punctuation punctuation, final String message) {
		ModelAndView result;

		result = new ModelAndView("punctuation/create");
		result.addObject("punctuation", punctuation);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Punctuation punctuation) {
		ModelAndView res;

		res = this.createEditModelAndView(punctuation, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final Punctuation punctuation, final String message) {
		ModelAndView result;

		result = new ModelAndView("punctuation/edit");
		result.addObject("punctuation", punctuation);
		result.addObject("message", message);

		return result;
	}

}
