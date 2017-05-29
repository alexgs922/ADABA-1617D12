
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.CategoryService;
import services.CommentService;
import services.ShoppingGroupService;
import services.UserService;
import utilities.AbstractTest;
import domain.Category;
import domain.Comment;
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
	//user = 1222,...,1226
	//shopping groups = 1238,..,1240
	//user 1 tiene solo el shopping group 1
	//admin = 1187
	@Test
	public void driver1() {

		final Object testingData[][] = {
			{
				//user 1 postea un comentario sin errores en uno de sus grupos de compra
				"user1", 1222, 1238, 0, null
			}, {
				//user 1 postea un comentario sin titulo en uno de sus grupos de compra
				"user1", 1222, 1238, 1, ConstraintViolationException.class
			}, {
				//user 1 postea un comentario sin texto en uno de sus grupos de compra
				"user1", 1222, 1238, 2, ConstraintViolationException.class
			}, {
				//user 1 postea un comentario sin creador en uno de sus grupos de compra
				"user1", 1222, 1238, 3, ConstraintViolationException.class
			}, {
				//user 1 intenta posterar un comentario en un grupo de comprar que no es suyo
				"user1", 1222, 1239, 0, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta postear un mensaje
				null, 1222, 1239, 0, IllegalArgumentException.class
			}, {
				//Un actor del sistema que no es usuario intenta postear un comentario
				"admin", 1222, 1239, 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//CASO DE USO: LISTA DE SHOPPING GROUPS P�BLICO PARA USUARIOS (INCLUYE TODOS LOS GRUPOS P�BLICOS Y LOS PRIVADOS A LOS QUE EL USUARIO LOGEADO PERTENECE)
	//Para comprobar el correcto funcionamiento de este caso de uso probaremos que:
	//1. Para cada usuario se recoge la cantidad adecuada de grupos.
	//2. Estos grupos son los correctos
	//3. Debe estar logeado como usuario para poder acceder a �l

	protected void templateList1(final String username, final int numberOfGroups, final List<String> groups, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Collection<ShoppingGroup> col = this.shoppingGroupService.listPublicForUsersOfSH();

			//Comprobamos que se recojan para cada usuario la cantidad adecuada de grupos
			Assert.isTrue(col.size() == numberOfGroups);

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
				//Test pnegativo: s�lo los usuarios pueden acceder al listado
				null, 0, new ArrayList<String>(), IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateList1((String) testingData[i][0], (int) testingData[i][1], (List<String>) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//CASO DE USO: LISTA PRIVADA DE GRUPOS PARA UN USUARIO: CONTIENE TODOS LOS GRUPOS QUE HA CREADO �L MISMO Y TODOS AQUELLOS A LOS QUE PERTENECE
	//Para comprobar el correcto funcionamiento de este caso de uso probaremos que:
	//1. Para cada usuario se recoge la cantidad adecuada de grupos.
	//2. Estos grupos son los correctos
	//3. Debe estar logeado como usuario para poder acceder a �l

	protected void templateList2(final String username, final int numberOfGroupsMine, final int numberOfGroupsBelong, final List<String> groupsMine, final List<String> groupsBelong, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final User principal = this.userService.findByPrincipal();
			final Collection<ShoppingGroup> shBelong = this.shoppingGroupService.ShoppingGroupsToWichBelongsAndNotCreatedBy(principal);
			final Collection<ShoppingGroup> shMine = principal.getMyShoppingGroups();

			//Comprobamos que el n�mero de grupos creados es el correcto
			Assert.isTrue(shMine.size() == numberOfGroupsMine);
			//Comprobamos que el n�mero de grupos a los que pertece sin haberlos creado es correcto
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
				//Test pnegativo: s�lo los usuarios pueden acceder al listado
				null, 0, 0, null, null, IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateList2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (List<String>) testingData[i][3], (List<String>) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	//CASO DE USO: CREAR UN NUEVO GRUPO P�BLICO ----------------------------------------------------------------------------------------
	//Para este caso de uso comprobaremos que:
	//El grupo se crea correctamente
	//El grupo se a�ade a la lista de grupos creados del usuario autenticado
	//El grupo aparece en el listado p�blico
	//S�lo puede hacer esta operaci�n un usuario

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
		form.setDescription("Descripci�n para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobaci�n se hace en controlador
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
		form.setDescription("Descripci�n para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobaci�n se hace en controlador
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
		form.setDescription("Descripci�n para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobaci�n se hace en controlador
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

	//Test negativo: No se crean grupos si no se aceptan los t�rminos de uso
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
		form.setDescription("Descripci�n para tests");
		form.setFreePlaces(5);
		form.setSite("Amazon");
		form.setTermsOfUse(false);

		//Esta comprobaci�n se hace en controlador
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
	//El grupo se a�ade a la lista de grupos creados del usuario autenticado
	//El grupo aparece en el listado p�blico
	//S�lo puede hacer esta operaci�n un usuario

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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		form.setUsers(principal.getFriends());

		//Esta comprobaci�n se hace en controlador
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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		form.setUsers(principal.getFriends());

		//Esta comprobaci�n se hace en controlador
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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		form.setUsers(principal.getFriends());

		//Esta comprobaci�n se hace en controlador
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

	//Test negativo: No se crean grupos si no se aceptan los t�rminos de uso
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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(false);
		form.setUsers(principal.getFriends());

		//Esta comprobaci�n se hace en controlador
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

	//Test negativo: No se crean grupos si no se eligen usuarios para pertenecer a �l
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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);

		//Esta comprobaci�n se hace en controlador
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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		final List<User> users = new ArrayList<User>();
		users.add(this.userService.findOne(594));
		form.setUsers(users);

		//Esta comprobaci�n se hace en controlador
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
		form.setDescription("Descripci�n para tests");
		form.setSite("Amazon");
		form.setTermsOfUse(true);
		final List<User> users = new ArrayList<User>();
		users.add(this.userService.findOne(591));
		form.setUsers(users);

		//Esta comprobaci�n se hace en controlador
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

	//CASO DE USO: EDITAR UN GRUPO DE COMPRAS--------------------------------------------------------------------------------------
	//Para este caso de uso comprobaremos que:
	//El grupo se edita correctamente
	//S�lo puede ser editado por el usuario que lo ha creado
	//S�lo lo puede editar cuando no tiene pedidos pendientes

	//Test positivo
	@Test
	public void testEdit1() {

		this.authenticate("user1");
		final ShoppingGroup sh = this.shoppingGroupService.findOne(615);

		sh.setName("Ahora se va a llamar as�");

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getName() == "Ahora se va a llamar as�");

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

		sh.setName("Ahora se va a llamar as�");

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getName() == "Ahora se va a llamar as�");

	}

	//Test negativo: el usuario 3 no puede editar su grupo porque tiene un pedido en curso
	@Test(expected = IllegalArgumentException.class)
	public void testEdit4() {

		this.authenticate("user3");
		final ShoppingGroup sh = this.shoppingGroupService.findOne(616);

		sh.setName("Ahora se va a llamar as�");

		this.shoppingGroupService.reconstruct2(sh, sh.getId(), null);
		this.shoppingGroupService.save(sh);
		this.shoppingGroupService.flush();

		final ShoppingGroup shafter = this.shoppingGroupService.findOne(615);
		Assert.isTrue(shafter.getName() == "Ahora se va a llamar as�");

	}

	//CASO DE USO : ELIMINAR UN GRUPO DE COMPRAS
	//Para este caso de uso comprobaremos que:
	//El grupo se elimina correctamente y se envian los mensajes a los afectados
	//S�lo puede ser eliminado por el usuario que lo ha creado
	//S�lo lo puede edliminar cuando no tiene pedidos pendientes

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

}
