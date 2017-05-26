
package funcionalTesting;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.PrivateMessageService;
import services.UserService;
import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private PrivateMessageService	privateMessageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserService				userService;


	//user = 1222,...,1226
	//distributor = 1195,...,1194
	//admin = 1187

	//Test dedicado a probar el caso de uso: Seguir a otros usuarios
	protected void template1(final String username, final int principalId, final int aSeguirId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final User principal = this.userService.findOne(principalId);
			final User aSeguir = this.userService.findOne(aSeguirId);

			final Collection<User> followsInicial = principal.getFriends();
			final int numeroDeUsuariosSeguidos = followsInicial.size();

			Assert.isTrue(principal.getId() != aSeguir.getId());
			this.userService.follow(aSeguir.getId());

			Assert.isTrue(principal.getFriends().size() == numeroDeUsuariosSeguidos + 1);

			this.unauthenticate();
			this.userService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//user = 1222,...,1226
	//distributor = 1195,...,1194
	//admin = 1187
	@Test
	public void driver1() {

		final Object testingData[][] = {
			{   //user 2 no sigue a user 3 y decide seguirlo
				"user2", 1223, 1224, null
			}, {
				//user 2 no sigue a user 4 y decide seguirlo
				"user2", 1223, 1225, null
			}, {
				//user 1 sigue a user 2 pero intenta seguir a user 2 de nuevo
				"user1", 1222, 1223, IllegalArgumentException.class
			}, {
				//user 1 intenta seguirse a si mismo 
				"user1", 1222, 1222, IllegalArgumentException.class
			}, {
				//Simular post hacking. Logeado como user 3, pero en realidad soy user 2 seguir a user 3
				"user3", 1223, 1224, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta seguir a otro usuario 
				null, 1223, 1224, IllegalArgumentException.class
			}, {
				//User 2 intenta seguir a un usuario que no existe
				"user2", 1223, 50000, IllegalArgumentException.class
			}, {
				//User 2 intenta seguir a un actor que no tiene como Rol User
				"user2", 1223, 1187, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//user = 1222,...,1226
	//distributor = 1195,...,1194
	//admin = 1187

	//Test dedicado a probar el caso de uso: Dejar de seguir a otros usuarios
	protected void template2(final String username, final int principalId, final int aSeguirId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final User principal = this.userService.findOne(principalId);
			final User aSeguir = this.userService.findOne(aSeguirId);

			final Collection<User> followsInicial = principal.getFriends();
			final int numeroDeUsuariosSeguidos = followsInicial.size();

			Assert.isTrue(principal.getId() != aSeguir.getId());
			this.userService.unfollow(aSeguir.getId());

			Assert.isTrue(principal.getFriends().size() == numeroDeUsuariosSeguidos - 1);

			this.unauthenticate();
			this.userService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//user = 1222,...,1226
	//distributor = 1195,...,1194
	//admin = 1187
	@Test
	public void driver2() {

		final Object testingData[][] = {
			{   //user 1 sigue a user 2 y decide dejar de seguirlo
				"user1", 1222, 1223, null
			}, {
				//user 1 sigue a user 3 y decide dejar de seguirlo
				"user1", 1222, 1224, null
			}, {
				//user 2 no sigue a user 3 pero intenta dejar de seguir a user 3 de nuevo
				"user2", 1223, 1224, IllegalArgumentException.class
			}, {
				//user 1 intenta dejar de seguirse a si mismo 
				"user1", 1222, 1222, IllegalArgumentException.class
			}, {
				//Simular post hacking. Logeado como user 3, pero en realidad soy user 1 dejando de seguir a user 2
				"user3", 1222, 1223, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta dejar de seguir a otro usuario 
				null, 1223, 1224, IllegalArgumentException.class
			}, {
				//User 1 intenta seguir a un usuario que no existe
				"user1", 1222, 50000, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

}
