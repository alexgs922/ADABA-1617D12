
package funcionalTesting;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.PrivateMessageService;
import services.UserService;
import utilities.AbstractTest;
import domain.User;
import forms.RegistrationForm;

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
	

	//Test dedicado a probar el caso de uso de registro de usuario 
	protected void template3(final String username,final String adress,final Date birthDate,final String description,final String email,
			final String identefication,final String name,final String password,final String passwordCheck,final String phone,final String picture,
			final String surName,final String username1, final boolean termsOfUse, Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			RegistrationForm customerForm = new RegistrationForm();
			customerForm.setAdress(adress);
			customerForm.setBirthDate(birthDate);
			customerForm.setDescription(description);
			customerForm.setEmail(email);
			customerForm.setIdentefication(identefication);
			customerForm.setName(name);
			customerForm.setPassword(password);
			customerForm.setPasswordCheck(passwordCheck);
			customerForm.setPhone(phone);
			customerForm.setPicture(picture);
			customerForm.setSurName(surName);
			customerForm.setTermsOfUse(termsOfUse);
			customerForm.setUsername(username1);
			
			User user = this.userService.reconstruct(customerForm);
			
			
			Assert.notNull(user);
			this.userService.save(user);
			
			this.unauthenticate();
			this.userService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	
	@Test
	public void driver3() {

		final Object testingData[][] = {
			{   //Usuario no registrado se registra en el sistema
				null, "adress", new Date(),"description","email@gmail.com","53585603L","name","123456","123456",
				"+34955613568","http://www.google.com","surname","usuarioPrueba",true, null
			}, {
				//Usuario no registrado se intenta registrar en el sistema con email invalido
				null, "adress", new Date(),"description","email","53585603L","name","123456","123456",
				"+34955613568","http://www.google.com","surname","usuarioPrueba",true, DataIntegrityViolationException.class
			}, {
				//Usuario no registrado se registra con un dni invalido
				null, "adress", new Date(),"description","email@gmail.com","535803","name","123456","123456",
				"+34955613568","http://www.google.com","surname","usuarioPrueba",true, DataIntegrityViolationException.class
			}, {
				//Usuario no registrado se registra con contraseñas diferentes
				null, "adress", new Date(),"description","email@gmail.com","535803","name","456789","123456",
				"+34955613568","http://www.google.com","surname","usuarioPrueba",true, DataIntegrityViolationException.class
			}, {
				//Usuario no registrado se registra en el sistema con campos null
				null, "adress", new Date(),null,null,"535803","name","123456","123456",
				"+34955613568",null,"surname","usuarioPrueba",true, DataIntegrityViolationException.class	
			},{
				//Usuario no registrado se registra en el sistema sin aceptar las condiciones de uso
				null, "adress", new Date(),"description","email@gmail.com","53585603L","name","123456","123456",
				"+34955613568","http://www.google.com","surname","usuarioPrueba",false, DataIntegrityViolationException.class
			
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (String) testingData[i][1], (Date) testingData[i][2],
					(String) testingData[i][3],(String) testingData[i][4],(String) testingData[i][5],(String) testingData[i][6],
					(String) testingData[i][7],(String) testingData[i][8],(String) testingData[i][9],(String) testingData[i][10],
					(String) testingData[i][11],(String) testingData[i][12],(boolean) testingData[i][13],(Class<?>) testingData[i][14]);

		}
	


}