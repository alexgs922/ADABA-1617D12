
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
import services.CommentService;
import services.CouponService;
import services.CreditCardService;
import services.DistributorService;
import services.ProductService;
import services.PunctuationService;
import services.ShoppingGroupService;
import services.UserService;
import controllers.AbstractController;
import domain.Category;
import domain.Comment;
import domain.Distributor;
import domain.Product;
import domain.Punctuation;
import domain.ShoppingGroup;
import domain.User;
import forms.CouponsForOrferForm;
import forms.JoinToForm;
import forms.ShoppingGroupForm;
import forms.ShoppingGroupFormPrivate;

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

	@Autowired
	private CommentService			commentService;

	@Autowired
	private CouponService			couponService;

	@Autowired
	private CreditCardService		cardService;

	@Autowired
	private DistributorService		distributorService;


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
		result.addObject("allowedMakeOrder", this.shoppingGroupService.allowedMakeOrder(sGToShow));
		return result;

	}

	// Create a new private Shopping Group ------------------------------------------------------

	@RequestMapping(value = "/createPrivate", method = RequestMethod.GET)
	public ModelAndView createPrivate() {
		ModelAndView result;
		final ShoppingGroupFormPrivate shoppingGroupForm;
		Collection<Category> cats;
		Collection<User> usuarios;
		User principal;

		principal = this.userService.findByPrincipal();
		usuarios = new ArrayList<User>();
		usuarios.addAll(this.userService.findAllMyFriends(principal.getId()));

		final Collection<User> usuarios2 = new ArrayList<User>(usuarios);

		for (final User u : usuarios2)
			if (u.isBanned())
				usuarios.remove(u);

		shoppingGroupForm = new ShoppingGroupFormPrivate();
		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit3");
		result.addObject("shoppingGroup", shoppingGroupForm);
		result.addObject("categories", cats);
		result.addObject("usuarios", usuarios);
		result.addObject("requestURI", "shoppingGroup/user/createPrivate.do");

		return result;

	}

	@RequestMapping(value = "/createPrivate", method = RequestMethod.POST, params = "save")
	public ModelAndView savePrivate(@ModelAttribute("shoppingGroup") @Valid final ShoppingGroupFormPrivate shoppingGroupForm, final BindingResult bindingResult) {

		ModelAndView result;
		ShoppingGroup shoppingGroup;

		if (bindingResult.hasErrors()) {

			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView3(shoppingGroupForm, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView3(shoppingGroupForm);

		} else
			try {
				shoppingGroup = this.shoppingGroupService.reconstructPrivate(shoppingGroupForm, bindingResult);
				this.shoppingGroupService.save(shoppingGroup);
				result = new ModelAndView("redirect:joinedShoppingGroups.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView3(shoppingGroupForm, "sh.commit.error");
			}

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

	//Edit a ShoppingGroup  ------------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int shoppingGroupId) {
		ModelAndView result;
		final ShoppingGroup shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		final User principal = this.userService.findByPrincipal();

		try {

			//solo el creador del grupo puede editarlo
			Assert.isTrue(principal.getId() == shoppingGroup.getCreator().getId());

		} catch (final IllegalArgumentException iae) {
			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.not.mine");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;
		}

		try {

			//Si está a null significa que no está esperando ningún pedido, y por tanto está en estado 'inicial' y se puede modificar y eliminar
			Assert.isNull(shoppingGroup.getLastOrderDate());

		} catch (final IllegalArgumentException iae) {
			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.noteditableInList");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;
		}

		Collection<Category> cats;
		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit2");
		result.addObject("shoppingGroup", shoppingGroup);
		result.addObject("categories", cats);
		result.addObject("requestURI", "shoppingGroup/user/edit.do?shoppingGroupId=" + shoppingGroupId);

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(@Valid ShoppingGroup shoppingGroup, final BindingResult bindingResult, @RequestParam final int shoppingGroupId) {
		ModelAndView result;
		Collection<Category> cats;
		cats = this.categoryService.findAll2();

		if (bindingResult.hasErrors()) {
			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(shoppingGroup, shoppingGroupId, cats, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(shoppingGroup, shoppingGroupId, cats);
		} else
			try {

				try {

					shoppingGroup = this.shoppingGroupService.reconstruct2(shoppingGroup, shoppingGroupId, bindingResult);

				} catch (final Throwable th) {
					result = this.createEditModelAndView(shoppingGroup, shoppingGroupId, cats);
					return result;
				}

				this.shoppingGroupService.save(shoppingGroup);
				result = new ModelAndView("redirect:joinedShoppingGroups.do");

			} catch (final Throwable th) {
				result = this.createEditModelAndView(shoppingGroup, shoppingGroupId, cats, "sh.commit.error");
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
				this.punctuationService.rate(shoppingGroup, punctuationRes);

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
				this.punctuationService.editRate(shoppingGroup, punctuationRes);
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

	//Make group order -------------------------------------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/makeOrder", method = RequestMethod.GET)
	public ModelAndView makeOrder(@RequestParam final int shoppingGroupId) {
		ModelAndView result;

		final ShoppingGroup sh = this.shoppingGroupService.findOne(shoppingGroupId);
		final User principal = this.userService.findByPrincipal();
		final Collection<Distributor> allDis = this.distributorService.findAll();

		try {

			Assert.isTrue(this.cardService.validateCreditCardNumber(principal.getCreditCard().getNumber()));
			Assert.isTrue(this.cardService.validateDate(principal.getCreditCard().getExpirationMonth(), principal.getCreditCard().getExpirationYear()));

		} catch (final Throwable th) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.not.cc.validation");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;

		}

		try {
			Assert.isTrue(this.shoppingGroupService.allowedMakeOrder(sh));
			Assert.isTrue(sh.getCreator().getId() == principal.getId());
		} catch (final Throwable th) {
			result = new ModelAndView("forbiddenOperation");
			return result;
		}

		final CouponsForOrferForm form = new CouponsForOrferForm();

		result = new ModelAndView("couponToFormulario");
		result.addObject("couponsforOrderform", form);
		result.addObject("cupones", this.couponService.findAll());
		result.addObject("shId", shoppingGroupId);
		result.addObject("distributors", allDis);
		result.addObject("requestURI", "shoppingGroup/user/makeOrder.do?shoppingGroupId=" + shoppingGroupId);

		return result;

	}
	@RequestMapping(value = "/makeOrder", method = RequestMethod.POST, params = "save")
	public ModelAndView makeOrder(final CouponsForOrferForm couponsForOrferForm, final BindingResult bindingResult, @RequestParam final int shoppingGroupId) {
		ModelAndView result;
		ShoppingGroup sh;

		sh = this.shoppingGroupService.findOne(shoppingGroupId);

		if (bindingResult.hasErrors()) {
			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(couponsForOrferForm, shoppingGroupId, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(couponsForOrferForm, shoppingGroupId);
		} else
			try {

				if (couponsForOrferForm.getCoupon() != null)
					this.shoppingGroupService.makeOrder(sh, couponsForOrferForm.getCoupon(), couponsForOrferForm.getDistributor());
				else
					this.shoppingGroupService.makeOrder(sh, null, couponsForOrferForm.getDistributor());

				result = new ModelAndView("redirect: display.do?shoppingGroupId=" + shoppingGroupId);

			} catch (final Throwable th) {
				result = new ModelAndView("forbiddenOperation");
			}

		return result;

	}

	// Leaving a group --------------------------------------------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/leave", method = RequestMethod.GET)
	public ModelAndView leaveAGroup(@RequestParam final int shoppingGroupId) {
		ModelAndView result;

		final ShoppingGroup sh = this.shoppingGroupService.findOne(shoppingGroupId);
		final User principal = this.userService.findByPrincipal();

		try {

			Assert.isNull(sh.getLastOrderDate());
			Assert.isTrue(sh.getCreator().getId() != principal.getId());
			Assert.isTrue(sh.getUsers().contains(principal));

		} catch (final Throwable e) {
			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.not.join.error");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;
		}

		try {
			this.shoppingGroupService.leaveAgroup(sh);
			result = new ModelAndView("redirect:joinedShoppingGroups.do");

		} catch (final Throwable th) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.commit.error");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
		}

		return result;

	}

	//Join to a public group  ----------------------------------------------------------------------

	@RequestMapping(value = "/join", method = RequestMethod.GET)
	public ModelAndView joinToGroup(@RequestParam final int shoppingGroupId) {
		final ModelAndView result;

		final ShoppingGroup sh = this.shoppingGroupService.findOne(shoppingGroupId);
		final User principal = this.userService.findByPrincipal();

		try {

			Assert.isTrue(sh.getLastOrderDate() == null);
			Assert.isTrue(sh.isPrivate_group() == false);
			Assert.isTrue(sh.getCreator().getId() != principal.getId());
			Assert.isTrue(!sh.getUsers().contains(principal));
			Assert.isTrue(sh.getFreePlaces() > 0);

		} catch (final Exception e) {

			result = new ModelAndView("errorOperation");
			result.addObject("errorOperation", "sh.not.join.error");
			result.addObject("returnUrl", "shoppingGroup/user/joinedShoppingGroups.do");
			return result;

		}

		final JoinToForm form = new JoinToForm();

		result = new ModelAndView("joinToShFormulario");
		result.addObject("joinToForm", form);
		result.addObject("shToJoinName", sh.getName());
		result.addObject("requestURI", "shoppingGroup/user/join.do?shoppingGroupId=" + shoppingGroupId);

		return result;

	}
	@RequestMapping(value = "/join", method = RequestMethod.POST, params = "save")
	public ModelAndView joinToGroup(final JoinToForm joinToForm, final BindingResult bindingResult, @RequestParam final int shoppingGroupId) {
		ModelAndView result;
		ShoppingGroup sh;

		sh = this.shoppingGroupService.findOne(shoppingGroupId);

		if (bindingResult.hasErrors()) {
			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(joinToForm, shoppingGroupId, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(joinToForm, shoppingGroupId);
		} else
			try {

				Assert.isTrue(joinToForm.isTermsOfUse() == true);
				this.shoppingGroupService.jointToAShoppingGroup(sh);
				result = new ModelAndView("redirect:joinedShoppingGroups.do");

			} catch (final IllegalArgumentException iae) {

				result = this.createEditModelAndView(joinToForm, shoppingGroupId, "sh.jointTo.must.accept.conditions");

			} catch (final Throwable th) {

				result = this.createEditModelAndView(joinToForm, shoppingGroupId, "sh.commit.error");

			}
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
			Assert.isTrue(this.shoppingGroupService.allowedMakeOrder(shoppingGroup));
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

		try {

			final String s = this.shoppingGroupService.contieneWeb(shoppingGroup.getSite());
			Assert.isTrue(product.getUrl().contains(s));

		} catch (final Throwable th) {

			res = this.createCreateModelAndView(product, "product.url.not.valid");
			res.addObject("shoppingGroup", shoppingGroup);
			return res;
		}

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

	// Post comment ------------------------------------------------------------------------------------------------------

	@RequestMapping(value = "/comment", method = RequestMethod.GET)
	public ModelAndView comment(@RequestParam final int shoppingGroupId) {
		ModelAndView res;

		Comment comment;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		comment = this.commentService.create(shoppingGroup);

		try {
			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
			res = this.createEditModelAndViewComment(comment);
			res.addObject("shoppingGroup", shoppingGroup);
		} catch (final Throwable th) {
			res = new ModelAndView("forbiddenOperation");
		}

		return res;

	}

	@RequestMapping(value = "/comment", method = RequestMethod.POST, params = "save")
	public ModelAndView comment(@ModelAttribute("comment") final Comment comment, final BindingResult binding, @RequestParam final int shoppingGroupId) {
		ModelAndView res;

		final Comment commentRes;
		ShoppingGroup shoppingGroup;
		User principal;

		principal = this.userService.findByPrincipal();
		shoppingGroup = this.shoppingGroupService.findOne(shoppingGroupId);
		comment.setShoppingGroupComments(shoppingGroup);
		comment.setUserComment(principal);
		comment.setMoment(new Date());

		commentRes = this.commentService.reconstruct(comment, binding);

		if (binding.hasErrors()) {
			res = this.createEditModelAndViewComment(comment);
			res.addObject("shoppingGroup", shoppingGroup);
		} else
			try {
				this.commentService.saveAndFlush(commentRes);
				shoppingGroup.getComments().add(commentRes);
				this.shoppingGroupService.save(shoppingGroup);
				res = new ModelAndView("redirect: display.do?shoppingGroupId=" + shoppingGroup.getId());
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

	protected ModelAndView createEditModelAndViewComment(final Comment comment) {
		ModelAndView res;

		res = this.createEditModelAndViewComment(comment, null);

		return res;
	}

	protected ModelAndView createEditModelAndViewComment(final Comment comment, final String message) {
		ModelAndView result;

		result = new ModelAndView("shoppingGroup/user/comment");
		result.addObject("comment", comment);
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

	protected ModelAndView createEditModelAndView(final ShoppingGroup sh, final int shoppingGroupId, final Collection<Category> cats) {
		ModelAndView res;

		res = this.createEditModelAndView(sh, shoppingGroupId, cats, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final ShoppingGroup sh, final int shoppingGroupId, final Collection<Category> cats, final String message) {
		ModelAndView result;

		result = new ModelAndView("shoppingGroup/edit2");
		result.addObject("shoppingGroup", sh);
		result.addObject("categories", cats);
		result.addObject("requestURI", "shoppingGroup/user/edit.do?shoppingGroupId=" + shoppingGroupId);
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

	protected ModelAndView createEditModelAndView(final JoinToForm form, final int shoppingGroupId) {
		ModelAndView res;

		res = this.createEditModelAndView(form, shoppingGroupId, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final JoinToForm form, final int shoppingGroupId, final String message) {
		ModelAndView result;

		final ShoppingGroup sh = this.shoppingGroupService.findOne(shoppingGroupId);

		result = new ModelAndView("joinToShFormulario");
		result.addObject("joinToForm", form);
		result.addObject("shToJoinName", sh.getName());
		result.addObject("requestURI", "shoppingGroup/user/join.do?shoppingGroupId=" + shoppingGroupId);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CouponsForOrferForm form, final int shoppingGroupId) {
		ModelAndView res;

		res = this.createEditModelAndView(form, shoppingGroupId, null);

		return res;
	}

	protected ModelAndView createEditModelAndView(final CouponsForOrferForm form, final int shoppingGroupId, final String message) {
		ModelAndView result;
		final Collection<Distributor> allDis = this.distributorService.findAll();

		result = new ModelAndView("couponToFormulario");
		result.addObject("couponsforOrderform", form);
		result.addObject("cupones", this.couponService.findAll());
		result.addObject("shId", shoppingGroupId);
		result.addObject("distributors", allDis);
		result.addObject("requestURI", "shoppingGroup/user/makeOrder.do?shoppingGroupId=" + shoppingGroupId);
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

	protected ModelAndView createEditModelAndView3(final ShoppingGroupFormPrivate form) {
		ModelAndView res;

		res = this.createEditModelAndView3(form, null);

		return res;
	}

	protected ModelAndView createEditModelAndView3(final ShoppingGroupFormPrivate form, final String message) {

		ModelAndView result;

		Collection<User> usuarios;
		User principal;

		principal = this.userService.findByPrincipal();
		usuarios = new ArrayList<User>();
		usuarios.addAll(principal.getFriends());

		for (final User u : usuarios)
			if (u.isBanned())
				usuarios.remove(u);

		Collection<Category> cats;
		cats = this.categoryService.findAll2();

		result = new ModelAndView("shoppingGroup/edit3");
		result.addObject("shoppingGroup", form);
		result.addObject("categories", cats);
		result.addObject("usuarios", usuarios);
		result.addObject("requestURI", "shoppingGroup/user/createPrivate.do");
		result.addObject("message", message);

		return result;
	}
}
