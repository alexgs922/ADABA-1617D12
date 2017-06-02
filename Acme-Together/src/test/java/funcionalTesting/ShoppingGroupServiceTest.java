
package funcionalTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.CategoryService;
import services.CommentService;
import services.CouponService;
import services.DistributorService;
import services.OrderDomainService;
import services.ProductService;
import services.PunctuationService;
import services.ShoppingGroupService;
import services.UserService;
import utilities.AbstractTest;
import domain.Category;
import domain.Comment;
import domain.Coupon;
import domain.Distributor;
import domain.OrderDomain;
import domain.Product;
import domain.Punctuation;
import domain.ShoppingGroup;
import domain.User;
import forms.ShoppingGroupForm;
import forms.ShoppingGroupFormPrivate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ShoppingGroupServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private ShoppingGroupService	shoppingGroupService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserService				userService;

	@Autowired
	private CommentService			commentService;

	@Autowired
	private CategoryService			categoryService;

	@Autowired
	private ProductService			productService;

	@Autowired
	private PunctuationService		punctuationService;

	@Autowired
	private DistributorService		distributorService;

	@Autowired
	private CouponService			couponService;

	@Autowired
	private OrderDomainService		orderService;


	//Test dedicado a probar el caso de uso: Postear comentarios en tus grupos de compra
	protected void template1(final String username, final int principalId, final int shoppingGroupId, final int caseId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final User principal = this.userService.findOne(principalId);
			final ShoppingGroup sg = this.shoppingGroupService.findOne(shoppingGroupId);

			final Collection<Comment> commentInicial = sg.getComments();
			final int numeroDeComentarios = commentInicial.size();

			Assert.isTrue(principal.getShoppingGroup().contains(sg));

			if (caseId == 0) {
				final Comment c = this.commentService.create(sg);

				c.setTitle("Samsung Galaxy s8");
				c.setText("Iphone es mejor, no compreis samsung son el mal");
				c.setShoppingGroupComments(sg);
				c.setUserComment(principal);
				c.setMoment(new Date());

				this.commentService.saveAndFlush(c);

				commentInicial.add(c);

				Assert.isTrue(sg.getComments().size() == numeroDeComentarios + 1);

			}

			if (caseId == 1) {
				final Comment c = this.commentService.create(sg);

				c.setTitle("");
				c.setText("Iphone es mejor, no compreis samsung son el mal");
				c.setShoppingGroupComments(sg);
				c.setUserComment(principal);
				c.setMoment(new Date());

				this.commentService.saveAndFlush(c);

				commentInicial.add(c);

				Assert.isTrue(sg.getComments().size() == numeroDeComentarios + 1);

			}
			if (caseId == 2) {
				final Comment c = this.commentService.create(sg);

				c.setTitle("Samsung Galaxy s8");
				c.setText("");
				c.setShoppingGroupComments(sg);
				c.setUserComment(principal);
				c.setMoment(new Date());

				this.commentService.saveAndFlush(c);

				commentInicial.add(c);

				Assert.isTrue(sg.getComments().size() == numeroDeComentarios + 1);

			}
			if (caseId == 3) {
				final Comment c = this.commentService.create(sg);

				c.setTitle("Samsung Galaxy s8");
				c.setText("Iphone es mejor, no compreis samsung son el mal");
				c.setShoppingGroupComments(sg);
				c.setMoment(new Date());

				this.commentService.saveAndFlush(c);

				commentInicial.add(c);

				Assert.isTrue(sg.getComments().size() == numeroDeComentarios + 1);

			}

			this.unauthenticate();
			this.commentService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//user = 591,..,594
	//shopping groups = 615,..,617
	//user 1 tiene solo el shopping group 1
	//admin = 556
	@Test
	public void driver1() {

		final Object testingData[][] = {
			{
				//user 1 postea un comentario sin errores en uno de sus grupos de compra
				"user1", 591, 615, 0, null
			}, {
				//user 1 postea un comentario sin titulo en uno de sus grupos de compra
				"user1", 591, 615, 1, ConstraintViolationException.class
			}, {
				//user 1 postea un comentario sin texto en uno de sus grupos de compra
				"user1", 591, 615, 2, ConstraintViolationException.class
			}, {
				//user 1 postea un comentario sin creador en uno de sus grupos de compra
				"user1", 591, 615, 3, ConstraintViolationException.class
			}, {
				//user 1 intenta posterar un comentario en un grupo de comprar que no es suyo
				"user1", 591, 616, 0, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta postear un mensaje
				null, 591, 615, 0, IllegalArgumentException.class
			}, {
				//Un actor del sistema que no es usuario intenta postear un comentario
				"admin", 556, 615, 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//CASO DE USO: LISTA DE SHOPPING GROUPS PÚBLICO PARA USUARIOS (INCLUYE TODOS LOS GRUPOS PÚBLICOS Y LOS PRIVADOS A LOS QUE EL USUARIO LOGEADO PERTENECE)
	//Para comprobar el correcto funcionamiento de este caso de uso probaremos que:
	//1. Para cada usuario se recoge la cantidad adecuada de grupos.
	//2. Estos grupos son los correctos
	//3. Debe estar logeado como usuario para poder acceder a él

	protected void templateList1(final String username, final int numberOfGroups, final List<String> groups, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Collection<ShoppingGroup> col = this.shoppingGroupService.listPublicForUsersOfSH();

			//Comprobamos que se recojan para cada usuario la cantidad adecuada de grupos
			Assert.isTrue(col.size() == numberOfGroups);
			System.out.println(col.size());
			System.out.println(numberOfGroups);

			//Comprobamos que los grupos que se han recogido son los correctos
			for (final ShoppingGroup sh : col)
				Assert.isTrue(groups.contains(sh.getName()));

			this.unauthenticate();
			this.shoppingGroupService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void driverlist1() {

		final List<String> paraUser1y2 = new ArrayList<String>();
		paraUser1y2.add("Sh1");
		paraUser1y2.add("Sh3");

		final List<String> paraUser3y4y5 = new ArrayList<String>();
		paraUser3y4y5.add("Sh1");
		paraUser3y4y5.add("Sh2");
		paraUser3y4y5.add("Sh3");

		final Object testingData[][] = {
			{
				//Test positivo
				"user1", 2, paraUser1y2, null
			}, {
				//Test positivo
				"user2", 2, paraUser1y2, null
			}, {
				//Test positivo
				"user3", 3, paraUser3y4y5, null
			}, {
				//Test positivo
				"user4", 3, paraUser3y4y5, null
			}, {
				//Test positivo
				"user5", 3, paraUser3y4y5, null
			}, {
				//Test pnegativo: sólo los usuarios pueden acceder al listado
				null, 0, new ArrayList<String>(), IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateList1((String) testingData[i][0], (int) testingData[i][1], (List<String>) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//CASO DE USO: LISTA PRIVADA DE GRUPOS PARA UN USUARIO: CONTIENE TODOS LOS GRUPOS QUE HA CREADO ÉL MISMO Y TODOS AQUELLOS A LOS QUE PERTENECE
	//Para comprobar el correcto funcionamiento de este caso de uso probaremos que:
	//1. Para cada usuario se recoge la cantidad adecuada de grupos.
	//2. Estos grupos son los correctos
	//3. Debe estar logeado como usuario para poder acceder a él

	protected void templateList2(final String username, final int numberOfGroupsMine, final int numberOfGroupsBelong, final List<String> groupsMine, final List<String> groupsBelong, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final User principal = this.userService.findByPrincipal();
			final Collection<ShoppingGroup> shBelong = this.shoppingGroupService.ShoppingGroupsToWichBelongsAndNotCreatedBy(principal);
			final Collection<ShoppingGroup> shMine = principal.getMyShoppingGroups();

			//Comprobamos que el número de grupos creados es el correcto
			Assert.isTrue(shMine.size() == numberOfGroupsMine);
			//Comprobamos que el número de grupos a los que pertece sin haberlos creado es correcto
			Assert.isTrue(shBelong.size() == numberOfGroupsBelong);

			for (final ShoppingGroup sh : shMine)
				//Comprobamos que los que recogen son los correctos
				Assert.isTrue(groupsMine.contains(sh.getName()));

			for (final ShoppingGroup sh : shBelong)
				//Comprobamos que los que recogen son los correctos
				Assert.isTrue(groupsBelong.contains(sh.getName()));

			this.unauthenticate();
			this.shoppingGroupService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void driverlist2() {

		final List<String> paraUser1 = new ArrayList<String>();
		paraUser1.add("Sh1");

		final List<String> paraUser2 = new ArrayList<String>();
		paraUser2.add("Sh1");

		final List<String> paraUser3uno = new ArrayList<String>();
		paraUser3uno.add("Sh2");

		final List<String> paraUser3dos = new ArrayList<String>();
		paraUser3dos.add("Sh1");

		final List<String> paraUser4uno = new ArrayList<String>();
		paraUser4uno.add("Sh3");

		final List<String> paraUser4dos = new ArrayList<String>();
		paraUser4dos.add("Sh1");
		paraUser4dos.add("Sh2");

		final List<String> paraUser5 = new ArrayList<String>();
		paraUser5.add("Sh1");
		paraUser5.add("Sh2");
		paraUser5.add("Sh3");

		final Object testingData[][] = {
			{
				//Test positivo
				"user1", 1, 0, paraUser1, null, null
			}, {
				//Test positivo
				"user2", 0, 1, null, paraUser2, null
			}, {
				//Test positivo
				"user3", 1, 1, paraUser3uno, paraUser3dos, null
			}, {
				//Test positivo
				"user4", 1, 2, paraUser4uno, paraUser4dos, null
			}, {
				//Test positivo
				"user5", 0, 3, null, paraUser5, null
			}, {
				//Test pnegativo: sólo los usuarios pueden acceder al listado
				null, 0, 0, null, null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateList2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (List<String>) testingData[i][3], (List<String>) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//CASO DE USO: CREAR UN NUEVO GRUPO PÚBLICO ----------------------------------------------------------------------------------------
	//Para este caso de uso comprobaremos que:
	//El grupo se crea correctamente
	//El grupo se añade a la lista de grupos creados del usuario autenticado
	//El grupo aparece en el listado público
	//Sólo puede hacer esta operación un usuario

	//Test positivo
	@Test
	public void testCreateshoppingGroup() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo : hay que estar autenticado como usuario para poder crear un nuevo grupo
	@Test(expected = IllegalArgumentException.class)
	public void testCreateshoppingGroup2() {

		this.authenticate(null);

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No puede crearse un grupo que no pertenece a ninguna categoria
	@Test(expected = NullPointerException.class)
	public void testCreateshoppingGroup3() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No se crean grupos si no se aceptan los términos de uso
	@Test(expected = IllegalArgumentException.class)
	public void testCreateshoppingGroup4() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(false);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//CASO DE USO: CREAR UN NUEVO GRUPO PRIVADO ----------------------------------------------------------------------------------------
	//Para este caso de uso comprobaremos que:
	//El grupo se crea correctamente
	//El grupo se añade a la lista de grupos creados del usuario autenticado
	//El grupo aparece en el listado público
	//Sólo puede hacer esta operación un usuario

	//Test positivo
	@Test
	public void testCreateshoppingGroupPrivate() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		form.setUsers(principal.getFriends());

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);
		for (final User u : form.getUsers()) {
			Assert.isTrue(principal.getFriends().contains(u));
			Assert.isTrue(u.getId() != principal.getId());
		}

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo : hay que estar autenticado como usuario para poder crear un nuevo grupo
	@Test(expected = IllegalArgumentException.class)
	public void testCreateshoppingGroupPrivate2() {

		this.authenticate(null);

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		form.setUsers(principal.getFriends());

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);
		for (final User u : form.getUsers()) {
			Assert.isTrue(principal.getFriends().contains(u));
			Assert.isTrue(u.getId() != principal.getId());
		}

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No puede crearse un grupo que no pertenece a ninguna categoria
	@Test(expected = NullPointerException.class)
	public void testCreateshoppingGroupPrivate3() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		form.setUsers(principal.getFriends());

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);
		for (final User u : form.getUsers()) {
			Assert.isTrue(principal.getFriends().contains(u));
			Assert.isTrue(u.getId() != principal.getId());
		}

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No se crean grupos si no se aceptan los términos de uso
	@Test(expected = IllegalArgumentException.class)
	public void testCreateshoppingGroupPrivate4() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(false);
		form.setUsers(principal.getFriends());

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);
		for (final User u : form.getUsers()) {
			Assert.isTrue(principal.getFriends().contains(u));
			Assert.isTrue(u.getId() != principal.getId());
		}

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No se crean grupos si no se eligen usuarios para pertenecer a él
	@Test(expected = NullPointerException.class)
	public void testCreateshoppingGroupPrivate5() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No se crean grupos si los usuarios no son amigos del administrador del grupo
	@Test(expected = IllegalArgumentException.class)
	public void testCreateshoppingGroupPrivate6() {

		this.authenticate("user2");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		final List<User> users = new ArrayList<User>();
		users.add(this.userService.findOne(594));
		form.setUsers(users);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);
		for (final User u : form.getUsers()) {
			Assert.isTrue(principal.getFriends().contains(u));
			Assert.isTrue(u.getId() != principal.getId());
		}

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//Test negativo: No se crean grupos si el usuario intenta crear un grupo consigo mismo
	@Test(expected = IllegalArgumentException.class)
	public void testCreateshoppingGroupPrivate7() {

		this.authenticate("user1");

		final User principal = this.userService.findByPrincipal();

		final int createdBefore = principal.getMyShoppingGroups().size();
		final int allBefore = this.shoppingGroupService.findAll().size();
		final int allInPublicListBefore = this.shoppingGroupService.listPublicForUsersOfSH().size();

		final ShoppingGroupFormPrivate form = new ShoppingGroupFormPrivate();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		final List<User> users = new ArrayList<User>();
		users.add(this.userService.findOne(591));
		form.setUsers(users);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		for (final User u : form.getUsers()) {
			Assert.isTrue(principal.getFriends().contains(u));
			Assert.isTrue(u.getId() != principal.getId());
		}

		ShoppingGroup sh = this.shoppingGroupService.reconstructPrivate(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final int createdAfter = principal.getMyShoppingGroups().size();
		final int allAfter = this.shoppingGroupService.findAll().size();
		final int allInPublicListAfter = this.shoppingGroupService.listPublicForUsersOfSH().size();

		Assert.isTrue(createdBefore == 1);
		Assert.isTrue(createdAfter == createdBefore + 1);

		Assert.isTrue(allBefore == 3);
		Assert.isTrue(allAfter == allBefore + 1);

		Assert.isTrue(allInPublicListBefore == 2);
		Assert.isTrue(allInPublicListAfter == allInPublicListBefore + 1);

	}

	//CASO DE USO: EDITAR UN GRUPO DE COMPRAS --------------------------------------------------------------------------------------
	//Para este caso de uso comprobaremos que:
	//El grupo se edita correctamente
	//Sólo puede ser editado por el usuario que lo ha creado
	//Sólo lo puede editar cuando no tiene pedidos pendientes

	//Test positivo
	@Test
	public void testEdit1() {

		this.authenticate("user1");
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);

		sh.setName("Ahora se va a llamar así");

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getName() == "Ahora se va a llamar así");

	}

	//Test positivo : cambiar el grupo de categoria
	@Test
	public void testEdit2() {

		this.authenticate("user1");
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);

		sh.setCategory(this.categoryService.findOnePublic(sh.getCategory().getId() + 1));

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getCategory().getId() == 580);
		Assert.isTrue(shafter.getCategory().getName().equals("Hecho a mano"));

	}

	//Test negativo: el usuario 2 no es el creador de ese grupo de compras y por tanto no puede editarlo
	@Test(expected = IllegalArgumentException.class)
	public void testEdit3() {

		this.authenticate("user2");
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);

		sh.setName("Ahora se va a llamar así");

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getName() == "Ahora se va a llamar así");

	}

	//Test negativo: el usuario 3 no puede editar su grupo porque tiene un pedido en curso
	@Test(expected = IllegalArgumentException.class)
	public void testEdit4() {

		this.authenticate("user3");
		final ShoppingGroup sh = this.shoppingGroupService.findOne(616);

		sh.setName("Ahora se va a llamar así");

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getName() == "Ahora se va a llamar así");

	}

	//CASO DE USO : ELIMINAR UN GRUPO DE COMPRAS --------------------------------------------------------------------------------------
	//Para este caso de uso comprobaremos que:
	//El grupo se elimina correctamente y se envian los mensajes a los afectados
	//Sólo puede ser eliminado por el usuario que lo ha creado
	//Sólo lo puede edliminar cuando no tiene pedidos pendientes

	//Test positivo
	@Test
	public void testDelete1() {

		this.authenticate("user1");
		final User principal = this.userService.findByPrincipal();
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);
		final Collection<User> users = new ArrayList<User>();
		users.addAll(sh.getUsers());
		final Map<User, Integer> listaDeCantidadDeMensajesRecibidos = new HashMap<User, Integer>();

		for (final User u : users)
			listaDeCantidadDeMensajesRecibidos.put(u, u.getMessageReceives().size());

		this.shoppingGroupService.delete(sh);
		this.shoppingGroupService.flush();

		//comprobamos que en el sistema ya solo quedan 2 grupos
		Assert.isTrue(this.shoppingGroupService.findAll().size() == 2);

		//comprobamos que la lista de mensajes recibidos de cada usuario afectado ha incrementado en un mensaje
		for (final User u : users)
			if (u.getId() != principal.getId()) {
				final int tamanio = u.getMessageReceives().size();
				final int tamanio2 = listaDeCantidadDeMensajesRecibidos.get(u);
				Assert.isTrue(tamanio == (tamanio2 + 1));
			}

	}

	//Test negativo: el usuario 2 no puede eliminar un grupo que ha creado el usuario1
	@Test(expected = IllegalArgumentException.class)
	public void testDelete2() {

		this.authenticate("user2");
		final User principal = this.userService.findByPrincipal();
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);
		final Collection<User> users = new ArrayList<User>();
		users.addAll(sh.getUsers());
		final Map<User, Integer> listaDeCantidadDeMensajesRecibidos = new HashMap<User, Integer>();

		for (final User u : users)
			listaDeCantidadDeMensajesRecibidos.put(u, u.getMessageReceives().size());

		this.shoppingGroupService.delete(sh);
		this.shoppingGroupService.flush();

		//comprobamos que en el sistema ya solo quedan 2 grupos
		Assert.isTrue(this.shoppingGroupService.findAll().size() == 2);

		//comprobamos que la lista de mensajes recibidos de cada usuario afectado ha incrementado en un mensaje
		for (final User u : users)
			if (u.getId() != principal.getId()) {
				final int tamanio = u.getMessageReceives().size();
				final int tamanio2 = listaDeCantidadDeMensajesRecibidos.get(u);
				Assert.isTrue(tamanio == (tamanio2 + 1));
			}

	}

	//Test negativo: el usuario 3 no puede eliminar su grupo de compras porque tiene un pedido en curso
	@Test(expected = IllegalArgumentException.class)
	public void testDelete3() {

		this.authenticate("user3");
		final User principal = this.userService.findByPrincipal();
		final ShoppingGroup sh = this.shoppingGroupService.findOne(616);
		final Collection<User> users = new ArrayList<User>();
		users.addAll(sh.getUsers());
		final Map<User, Integer> listaDeCantidadDeMensajesRecibidos = new HashMap<User, Integer>();

		for (final User u : users)
			listaDeCantidadDeMensajesRecibidos.put(u, u.getMessageReceives().size());

		this.shoppingGroupService.delete(sh);
		this.shoppingGroupService.flush();

		//comprobamos que en el sistema ya solo quedan 2 grupos
		Assert.isTrue(this.shoppingGroupService.findAll().size() == 2);

		//comprobamos que la lista de mensajes recibidos de cada usuario afectado ha incrementado en un mensaje
		for (final User u : users)
			if (u.getId() != principal.getId()) {
				final int tamanio = u.getMessageReceives().size();
				final int tamanio2 = listaDeCantidadDeMensajesRecibidos.get(u);
				Assert.isTrue(tamanio == (tamanio2 + 1));
			}

	}

	//CASO DE USO: ABANDONAR UN GRUPO DE COMPRA --------------------------------------------------------------------------------------
	//Para este caso de uso se comprobará que:
	//El grupo no tenga ningún pedido en curso
	//El usuari que pretende abandonar el grupo no sea el creador del mismo
	//El usuario que pretende abandonar el grupo pertenezca a él
	//El grupo se queda con un integrante menos, y es justanmente el usuario autenticado que realiza la operación
	//El número de plazas libres aumenta en una unidad si el grupo es publico

	//Test positivo
	@Test
	public void testLeave1() {

		this.authenticate("user3");

		final User principal = this.userService.findByPrincipal();
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);
		final int placesfreeBefore = sh.getFreePlaces();

		this.shoppingGroupService.leaveAgroup(sh);
		this.shoppingGroupService.flush();

		//Comprobamos que hay un usuario menos entre los integrantes
		Assert.isTrue(sh.getUsers().size() == 4);

		//Comprobamos que el usuario ya no es integrante
		for (final User u : sh.getUsers())
			Assert.isTrue(u.getId() != principal.getId());

		if (sh.isPrivate_group() == false)
			Assert.isTrue(sh.getFreePlaces() == placesfreeBefore + 1);
		else
			Assert.isTrue(sh.getFreePlaces() == 0);

	}

	//Test negativo: el usuario 4 no puede abandonar el grupo porque tiene un pedido en curso
	@Test(expected = IllegalArgumentException.class)
	public void testLeave2() {

		this.authenticate("user4");

		final ShoppingGroup sh = this.shoppingGroupService.findOne(616);

		this.shoppingGroupService.leaveAgroup(sh);
		this.shoppingGroupService.flush();

	}

	//Test negativo: el usuario 1 no puede abandonar el grupo porque lo ha creado él mismo
	@Test(expected = IllegalArgumentException.class)
	public void testLeave3() {

		this.authenticate("user1");

		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);

		this.shoppingGroupService.leaveAgroup(sh);
		this.shoppingGroupService.flush();

	}

	//Test negativo: el usuario 1 no puede abandonar el grupo porque no pertenece a él
	@Test(expected = IllegalArgumentException.class)
	public void testLeave4() {

		this.authenticate("user1");

		final ShoppingGroup sh = this.shoppingGroupService.findOne(616);

		this.shoppingGroupService.leaveAgroup(sh);
		this.shoppingGroupService.flush();

	}

	//CASO UNIRSE A UN GRUPO DE COMPRA PÚBLICO  --------------------------------------------------------------------------------------
	//Para este caso de uso se comprobará que:
	//El usuario que pretende unirse al grupo no sea el creador del mismo
	//El usuario que pretende unirse al grupo no pertenezca a él
	//El grupo se queda con un integrante más, y es justanmente el usuario autenticado que realiza la operación
	//El grupo es público, ya que a los privados los une el propio administrador del grupo en la creación del mismo

	@Test
	public void testJoin1() {

		//Creamos un nuevo grupo público para unirnos
		this.authenticate("user1");

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		this.unauthenticate();

		//El usuario3 se une al nuevo grupo
		this.authenticate("user3");

		final User principal = this.userService.findByPrincipal();
		final int placesfreeBefore = sh.getFreePlaces();

		this.shoppingGroupService.jointToAShoppingGroup(sh);
		this.shoppingGroupService.flush();

		//Comprobamos que hay un usuario meás entre los integrantes
		Assert.isTrue(sh.getUsers().size() == 2);

		//Comprobamos que el usuario  es integrante
		Assert.isTrue(sh.getUsers().contains(principal));

		//Comprobamos que hay una plaza libre menos
		Assert.isTrue(sh.getFreePlaces() == placesfreeBefore - 1);

	}

	//Test negativo: el usuario pretende unirse al grupo que ha creado él mismo
	@Test(expected = IllegalArgumentException.class)
	public void testJoin2() {

		//Creamos un nuevo grupo público para unirnos
		this.authenticate("user1");

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final User principal = this.userService.findByPrincipal();
		final int placesfreeBefore = sh.getFreePlaces();

		this.shoppingGroupService.jointToAShoppingGroup(sh);
		this.shoppingGroupService.flush();

		//Comprobamos que hay un usuario meás entre los integrantes
		Assert.isTrue(sh.getUsers().size() == 2);

		//Comprobamos que el usuario  es integrante
		Assert.isTrue(sh.getUsers().contains(principal));

		//Comprobamos que hay una plaza libre menos
		Assert.isTrue(sh.getFreePlaces() == placesfreeBefore - 1);

	}

	//Test negativo el usuario pretende unirse a un grupo al que ya pertenece
	@Test(expected = IllegalArgumentException.class)
	public void testJoin3() {

		//Creamos un nuevo grupo público para unirnos
		this.authenticate("user1");

		final ShoppingGroupForm form = new ShoppingGroupForm();
		form.setName("Grupo para tests");
		final Category c = this.categoryService.findOnePublic(567);
		form.setCategory(c);
		form.setDescription("Descripción para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobación se hace en controlador
		Assert.isTrue(form.isTermsOfUse() == true);

		ShoppingGroup sh = this.shoppingGroupService.reconstruct(form, null);
		sh = this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		this.unauthenticate();

		//El usuario3 se une al nuevo grupo
		this.authenticate("user3");

		final User principal = this.userService.findByPrincipal();
		final int placesfreeBefore = sh.getFreePlaces();

		this.shoppingGroupService.jointToAShoppingGroup(sh);
		this.shoppingGroupService.flush();

		//Comprobamos que hay un usuario meás entre los integrantes
		Assert.isTrue(sh.getUsers().size() == 2);

		//Comprobamos que el usuario  es integrante
		Assert.isTrue(sh.getUsers().contains(principal));

		//Comprobamos que hay una plaza libre menos
		Assert.isTrue(sh.getFreePlaces() == placesfreeBefore - 1);

		this.unauthenticate();

		//El usuario pretende unirse otravez a un grupo al q ya pertenece

		this.authenticate("user3");

		this.shoppingGroupService.jointToAShoppingGroup(sh);
		this.shoppingGroupService.flush();

	}

	//Test negativo: el usuario 1 intenta unirse a un grupo que es privado
	@Test(expected = IllegalArgumentException.class)
	public void testJoin4() {

		//Creamos un nuevo grupo público para unirnos
		this.authenticate("user1");

		final ShoppingGroup sh = this.shoppingGroupService.findOne(616);

		this.shoppingGroupService.jointToAShoppingGroup(sh);
		this.shoppingGroupService.flush();

	}

	//Test: el usuario intenta añadir un producto a un grupo de compra en el que está apuntado.

	protected void testAddProduct(final String username, final ShoppingGroup shoppingGroup, final String name, final Double price, final String reference, final String url, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			//El usuario se encuentra inscrito en este shoppingGroup

			final User principal = this.userService.findByPrincipal();

			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));

			final Product product = this.productService.create();

			product.setName(name);
			product.setPrice(price);
			product.setReferenceNumber(reference);
			product.setShoppingGroupProducts(shoppingGroup);
			product.setUrl(url);
			product.setUserProduct(principal);

			this.productService.saveAndFlush(product);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverAddProduct() {

		final ShoppingGroup group1 = this.shoppingGroupService.findOne(615); // El user1 está apuntado a este grupo
		final ShoppingGroup group2 = this.shoppingGroupService.findOne(616); // El user1 no esta apuntado a este grupo

		final Object testingData[][] = {
			{   //El user1 intenta añadir correctamente un producto a un shoppingGroup en el que esta apuntado
				"user1", group1, "New Product", 30.0, "456321G", "http://amazon.com", null
			}, {   //El user1 intenta añadir correctamente un producto a un shoppingGroup en el que no esta apuntado
				"user1", group2, "New Product", 30.0, "456321G", "http://amazon.com", IllegalArgumentException.class
			}, {   //El user1 intenta añadir un producto a un shoppingGroup en el que esta apuntado
					//introduciendo todos los campos del producto en blanco menos el precio.
				"user1", group1, "", 30.0, "", "", ConstraintViolationException.class
			}, {   //El user1 intenta añadir un producto a un shoppingGroup en el que esta apuntado
					//introduciento de manera erronea una URL
				"user1", group1, "New Product", 30.0, "456321G", "ttp//amazon.com", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testAddProduct((String) testingData[i][0], (ShoppingGroup) testingData[i][1], (String) testingData[i][2], (Double) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);

	}

	//Test: el usuario intenta borrar un producto de un shoppingGroup

	protected void testDeleteProduct(final String username, final ShoppingGroup shoppingGroup, final Product product, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			//El usuario se encuentra inscrito en este shoppingGroup

			final User principal = this.userService.findByPrincipal();

			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));
			Assert.isTrue(shoppingGroup.getProducts().contains(product));

			this.productService.delete(product);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverDeleteProduct() {

		final Product product1 = this.productService.findOne(606);
		final ShoppingGroup group1 = this.shoppingGroupService.findOne(615);

		final Object testingData[][] = {
			{   //El user1 puede borrar un producto que es suyo y que esta en un grupo al que el pertenece
				"user1", group1, product1, null
			}, {
				//user 1 intenta borrar el product 1 del grupo 1, pero no puede porque dicho producto no ha sido creado por el
				"user2", group1, product1, InvalidDataAccessApiUsageException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testDeleteProduct((String) testingData[i][0], (ShoppingGroup) testingData[i][1], (Product) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Test: un user puntua a un shoppingGroup al que esta apuntado y que todavía no ha puntuado
	//Comprobamos también que la puntuación se ha cambiado correctamente

	protected void testScoreShoppingGroup(final String username, final Integer value, final ShoppingGroup shoppingGroup, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			//El usuario se encuentra inscrito en este shoppingGroup

			final User principal = this.userService.findByPrincipal();

			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));

			final Punctuation rate = this.punctuationService.create();

			rate.setShoppingGroup(shoppingGroup);
			rate.setUser(principal);
			rate.setValue(value);
			principal.getPunctuations().add(rate);
			shoppingGroup.getPunctuations().add(rate);

			shoppingGroup.setPuntuation(shoppingGroup.getPuntuation() + rate.getValue());

			this.userService.save(principal);
			this.shoppingGroupService.save(shoppingGroup);
			this.punctuationService.saveAndFlush(rate);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverScoreShoppingGroup() {

		final ShoppingGroup group1 = this.shoppingGroupService.findOne(616);
		final ShoppingGroup group2 = this.shoppingGroupService.findOne(617);

		final Object testingData[][] = {
			{   //El user introduce correctamente una puntuación
				"user3", 5, group1, null
			}, {
				//El user3 intenta introducir una puntuación con un valor que se sale de rango.
				"user3", -6, group1, ConstraintViolationException.class
			}, {
				//El user3 intenta introducir una puntuación a un grupo en el que no está apuntado
				"user3", 5, group2, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.testScoreShoppingGroup((String) testingData[i][0], (Integer) testingData[i][1], (ShoppingGroup) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	protected void testEditScoreShoppingGroup(final String username, final Punctuation rate, final Integer value, final ShoppingGroup shoppingGroup, final Integer expectedValue, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			//El usuario se encuentra inscrito en este shoppingGroup

			final User principal = this.userService.findByPrincipal();

			Assert.isTrue(principal.getShoppingGroup().contains(shoppingGroup));

			final Integer oldValue = rate.getValue();

			rate.setValue(value);

			shoppingGroup.setPuntuation(shoppingGroup.getPuntuation() - oldValue + rate.getValue());

			Assert.isTrue(shoppingGroup.getPuntuation() == expectedValue);

			this.userService.save(principal);
			this.shoppingGroupService.save(shoppingGroup);
			this.punctuationService.saveAndFlush(rate);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditScoreShoppingGroup() {

		final ShoppingGroup group1 = this.shoppingGroupService.findOne(615); //shoppingGroup en el que ya ha puntuado el user1
		final Punctuation rate1 = this.punctuationService.findOne(622); // puntuacion del user1 al group1

		final Object testingData[][] = {
			{   //El user introduce correctamente una puntuación
				"user1", rate1, -1, group1, 1, null
			}, {
				//El user3 intenta introducir una puntuación con un valor que se sale de rango.
				"user1", rate1, -6, group1, -4, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testEditScoreShoppingGroup((String) testingData[i][0], (Punctuation) testingData[i][1], (Integer) testingData[i][2], (ShoppingGroup) testingData[i][3], (Integer) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//TEST: Realizar un order ---------------------------------------------------

	protected void testMakeOrderShoppingGroup(final String username, final ShoppingGroup shoppingGroup, final Distributor distributor, final Coupon coupon, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			this.shoppingGroupService.makeOrder(shoppingGroup, coupon, distributor);

			this.shoppingGroupService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverMakeOrderShoppingGroup() {

		this.authenticate("user1");

		final ShoppingGroup group1 = this.shoppingGroupService.findOne(615); //shoppingGroup creado por el user1
		final ShoppingGroup group2 = this.shoppingGroupService.findOne(616); //shoppingGroup creado por el user3
		final Distributor distributor1 = this.distributorService.findOne(564);
		final Distributor distributor2 = this.distributorService.findOne(565);

		final Coupon coupon1 = this.couponService.findOne(560);
		final Coupon coupon2 = this.couponService.findOne(561);

		this.unauthenticate();

		final Object testingData[][] = {
			{   //El user1 creador del group1 hace realiza la order correctamente
				"user1", group1, distributor1, coupon1, null
			}, {
				//El user1 no es creador del group1 e intenta realizar una order
				"user1", group2, distributor2, coupon2, IllegalArgumentException.class
			}, {
				//El user 3 es el creador del group2 e intenta realizar un order cuando no esta permitido
				//ya que hay un order en curso.
				"user3", group2, distributor2, coupon2, IllegalArgumentException.class
			}, {
				//El user 5 intenta hacer una order. No puede realizarla porque su tarjeta de credito no es valida.
				"user5", group2, distributor2, coupon2, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testMakeOrderShoppingGroup((String) testingData[i][0], (ShoppingGroup) testingData[i][1], (Distributor) testingData[i][2], (Coupon) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//TEST: Marcar una order como recibida ----------------------------------------------------------------------

	protected void testMarkAsARecievedOrder(final String username, final OrderDomain order, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {
			this.authenticate(username);

			this.orderService.markAsAReceived(order);

			this.orderService.flush();

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverMarkAsARecieved() {

		final OrderDomain order1 = this.orderService.findOne(597); //order perteneciente al user3
		final OrderDomain order2 = this.orderService.findOne(596); //order perteneciente al user1

		final Object testingData[][] = {
			{   //El user3 marca una order como recibida correctamente.
				"user3", order1, null
			}, {
				//El user1 intenta marcar una order que pertenece al user3 como recibida
				"user1", order1, NullPointerException.class
			}, {
				//El user1 intenta marcar una order como recibida, cuando ya ha sido marcada como tal
				"user1", order2, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.testMarkAsARecievedOrder((String) testingData[i][0], (OrderDomain) testingData[i][1], (Class<?>) testingData[i][2]);

	}

}
