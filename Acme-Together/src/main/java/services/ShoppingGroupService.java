
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ShoppingGroupRepository;
import domain.Category;
import domain.Comment;
import domain.OrderDomain;
import domain.Product;
import domain.Punctuation;
import domain.ShoppingGroup;
import domain.Status;
import domain.User;
import forms.ShoppingGroupForm;
import forms.ShoppingGroupFormPrivate;

@Service
@Transactional
public class ShoppingGroupService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ShoppingGroupRepository	shoppingGroupRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService				userService;

	@Autowired
	private PrivateMessageService	privateMessageService;

	@Autowired
	private ProductService			productService;

	@Autowired
	private CommentService			commentService;

	@Autowired
	private OrderDomainService		orderService;


	// Constructors -----------------------------------------------------------

	public ShoppingGroupService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public ShoppingGroup create() {
		ShoppingGroup res;
		res = new ShoppingGroup();
		return res;
	}

	public Collection<ShoppingGroup> findAll() {
		Collection<ShoppingGroup> res;
		res = this.shoppingGroupRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public ShoppingGroup findOne(final int shoppingGroupId) {
		ShoppingGroup res;
		res = this.shoppingGroupRepository.findOne(shoppingGroupId);
		Assert.notNull(res);
		return res;
	}

	public ShoppingGroup save(final ShoppingGroup s) {
		Assert.notNull(s);

		if (s.getId() == 0) {
			final User principal = this.userService.findByPrincipal();
			principal.getMyShoppingGroups().add(s);
			principal.getShoppingGroup().add(s);
		}

		return this.shoppingGroupRepository.save(s);

	}

	public void delete(final ShoppingGroup s) {
		Assert.notNull(s);
		final User principal = this.userService.findByPrincipal();

		Assert.isTrue(s.getLastOrderDate() == null);
		Assert.isTrue(s.getCreator().getId() == principal.getId());

		this.privateMessageService.deleteGroupMessage(s, principal);

		for (final Product p : s.getProducts()) {
			this.productService.delete(p);
			this.productService.flush();
		}

		for (final Comment c : s.getComments()) {
			this.commentService.delete(c);
			this.commentService.flush();
		}
		this.shoppingGroupRepository.delete(s);
	}
	//Other business methods --------------------------------------

	public void flush() {
		this.shoppingGroupRepository.flush();
	}

	public Collection<ShoppingGroup> findShoppingGroupByCategory(final Category category) {
		Assert.notNull(category);

		Collection<ShoppingGroup> shoppingGroups;

		shoppingGroups = this.shoppingGroupRepository.findShoppingGroupsByCategory(category.getId());

		return shoppingGroups;
	}

	public Collection<ShoppingGroup> listPublicForUsersOfSH() {
		Collection<ShoppingGroup> shoppingGroups;

		final User principal = this.userService.findByPrincipal();

		shoppingGroups = this.shoppingGroupRepository.listPublicForUsersOfSH(principal.getId());

		return shoppingGroups;
	}

	public Collection<ShoppingGroup> ShoppingGroupsToWichBelongsAndNotCreatedBy(final User u) {
		Collection<ShoppingGroup> shoppingGroups;

		shoppingGroups = this.shoppingGroupRepository.ShoppingGroupsToWichBelongsAndNotCreatedBy(u.getId());

		return shoppingGroups;

	}

	public boolean alreadyPunctuate(final ShoppingGroup shoppingGroup, final User user) {
		Assert.notNull(shoppingGroup);

		Collection<Punctuation> userPunctuation;
		boolean res = false;

		userPunctuation = user.getPunctuations();

		for (final Punctuation userPunc : userPunctuation)
			if (userPunc.getShoppingGroup().getId() == shoppingGroup.getId()) {
				res = true;
				break;
			}

		return res;

	}


	@Autowired
	private Validator	validator;


	public ShoppingGroup reconstruct(final ShoppingGroupForm form, final BindingResult bindingResult) {
		ShoppingGroup result;
		Collection<Comment> cs;
		User principal;
		Collection<Product> products;
		Collection<Punctuation> punctuations;
		Collection<User> users;

		result = new ShoppingGroup();
		cs = new ArrayList<Comment>();
		principal = this.userService.findByPrincipal();
		products = new ArrayList<Product>();
		punctuations = new ArrayList<Punctuation>();
		users = new ArrayList<User>();
		users.add(principal);

		result.setCategory(form.getCategory());
		result.setComments(cs);
		result.setCreator(principal);
		result.setDescription(form.getDescription());
		result.setFreePlaces(form.getFreePlaces());
		result.setLastOrderDate(null);
		result.setName(form.getName());
		result.setPrivate_group(false);
		result.setProducts(products);
		result.setPunctuations(punctuations);
		result.setPuntuation(0);
		result.setSite(form.getSite());
		result.setUsers(users);

		this.validator.validate(result, bindingResult);

		return result;
	}

	public ShoppingGroup reconstruct2(final ShoppingGroup sh, final int shoppingGroupId, final BindingResult bindingResult) {
		ShoppingGroup result;

		result = this.shoppingGroupRepository.findOne(shoppingGroupId);

		Assert.isTrue(result.getLastOrderDate() == null);
		final User principal = this.userService.findByPrincipal();
		Assert.isTrue(principal.getId() == result.getCreator().getId());

		result.setCategory(sh.getCategory());
		result.setDescription(sh.getDescription());
		if (result.isPrivate_group() == false)
			result.setFreePlaces(sh.getFreePlaces());
		result.setLastOrderDate(null);
		result.setName(sh.getName());
		result.setSite(sh.getSite());

		this.validator.validate(result, bindingResult);

		return result;
	}

	public ShoppingGroup reconstructPrivate(final ShoppingGroupFormPrivate form, final BindingResult bindingResult) {
		ShoppingGroup result;
		Collection<Comment> cs;
		User principal;
		Collection<Product> products;
		Collection<Punctuation> punctuations;
		Collection<User> users;

		result = new ShoppingGroup();
		cs = new ArrayList<Comment>();
		principal = this.userService.findByPrincipal();
		products = new ArrayList<Product>();
		punctuations = new ArrayList<Punctuation>();
		users = new ArrayList<User>();
		users.add(principal);
		users.addAll(form.getUsers());

		result.setCategory(form.getCategory());
		result.setComments(cs);
		result.setCreator(principal);
		result.setDescription(form.getDescription());
		result.setFreePlaces(0);
		result.setLastOrderDate(null);
		result.setName(form.getName());
		result.setPrivate_group(true);
		result.setProducts(products);
		result.setPunctuations(punctuations);
		result.setPuntuation(0);
		result.setSite(form.getSite());
		result.setUsers(users);

		this.validator.validate(result, bindingResult);

		return result;
	}

	public void jointToAShoppingGroup(final ShoppingGroup sh) {

		final User principal = this.userService.findByPrincipal();

		Assert.isTrue(sh.getLastOrderDate() == null);
		Assert.isTrue(sh.isPrivate_group() == false);
		Assert.isTrue(sh.getCreator().getId() != principal.getId());
		Assert.isTrue(!sh.getUsers().contains(principal));
		Assert.isTrue(sh.getFreePlaces() > 0);

		principal.getShoppingGroup().add(sh);
		sh.getUsers().add(principal);

		sh.setFreePlaces(sh.getFreePlaces() - 1);

		this.shoppingGroupRepository.save(sh);
		this.shoppingGroupRepository.flush();
		this.userService.save(principal);

	}

	public void leaveAgroup(final ShoppingGroup sh) {
		final User principal = this.userService.findByPrincipal();

		Assert.isNull(sh.getLastOrderDate());
		Assert.isTrue(sh.getCreator().getId() != principal.getId());
		Assert.isTrue(sh.getUsers().contains(principal));

		final Collection<Product> col = new ArrayList<Product>();
		col.addAll(sh.getProducts());

		for (final Product p : col)
			if (p.getUserProduct().getId() == principal.getId()) {
				sh.getProducts().remove(p);
				principal.getProducts().remove(p);
				this.productService.delete(p);
				this.productService.flush();
			}

		principal.getShoppingGroup().remove(sh);
		sh.getUsers().remove(principal);

		sh.setFreePlaces(sh.getFreePlaces() + 1);

		this.shoppingGroupRepository.save(sh);
		this.shoppingGroupRepository.flush();
		this.userService.save(principal);

	}

	public boolean allowedMakeOrder(final ShoppingGroup shoppingGroup) {
		Assert.notNull(shoppingGroup);

		List<Product> groupProducts;
		boolean res = false;

		groupProducts = new ArrayList<Product>(shoppingGroup.getProducts());

		if (!groupProducts.isEmpty() && groupProducts.get(0).getOrderProduct() == null)
			res = true;

		return res;

	}

	public void makeOrder(final ShoppingGroup shoppingGroup) {
		Assert.notNull(shoppingGroup);

		OrderDomain order;
		User principal;

		principal = this.userService.findByPrincipal();
		order = this.orderService.create();

		order.setInitDate(new Date());
		order.setStatus(Status.INPROCESS);
		order.setProducts(shoppingGroup.getProducts());
		Double totalPrice = 0.0;

		for (final Product p : shoppingGroup.getProducts())
			totalPrice += p.getPrice();

		order.setTotalPrice(totalPrice);
		order.setCreator(principal);
		principal.getOrders().add(order);

		this.userService.save(principal);
		this.userService.flush();

		this.orderService.saveAndFlush(order);

		for (final Product p : shoppingGroup.getProducts()) {
			p.setOrderProduct(order);
			this.productService.save(p);
		}

	}

	public Collection<ShoppingGroup> shoppingGroupsWithMorePuntuation() {

		Collection<ShoppingGroup> sg;

		sg = this.shoppingGroupRepository.shoppingGroupsWithMorePuntuation();

		return sg;

	}

	public Collection<ShoppingGroup> shoppingGroupsWithLessPuntuation() {

		Collection<ShoppingGroup> sg;

		sg = this.shoppingGroupRepository.shoppingGroupsWithLessPuntuation();

		return sg;

	}

	public Double percentShoppingGroupsWithFreePlaces() {

		Double sg;

		sg = this.shoppingGroupRepository.percentShoppingGroupsWithFreePlaces();

		return sg;

	}

	public Double percentShoppingGroupsWithoutFreePlaces() {

		Double sg;

		sg = this.shoppingGroupRepository.percentShoppingGroupsWithoutFreePlaces();

		return sg;

	}

}
