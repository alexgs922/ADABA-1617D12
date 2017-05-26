
package funcionalTesting;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.CommentService;
import services.ShoppingGroupService;
import services.UserService;
import utilities.AbstractTest;
import domain.Comment;
import domain.ShoppingGroup;
import domain.User;

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

}
