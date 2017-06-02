
package funcionalTesting;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ActorService;
import services.PrivateMessageService;
import utilities.AbstractTest;
import domain.Actor;
import domain.PrivateMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class PrivateMessageServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private PrivateMessageService	privateMessageService;

	@Autowired
	private ActorService			actorService;


	//user = 591,...,594
	//distributor = 564,...,566
	//admin = 556

	//Crear message sin errores de validacion y otros casos comunes
	protected void template1(final String username, final int enviarId, final int recibirId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Actor envia = this.actorService.findOneToSent(enviarId);
			final Actor recibe = this.actorService.findOneToSent(recibirId);

			final Collection<PrivateMessage> enviadosInicial = envia.getMessageWrites();
			final int numeroDeMensajesEnviados = enviadosInicial.size();

			final Collection<PrivateMessage> recibidosInicial = recibe.getMessageReceives();
			final int numeroDeMensajesRecibidos = recibidosInicial.size();

			final PrivateMessage pm = this.privateMessageService.create();

			pm.setAttachments("");
			pm.setCopy(true);
			pm.setRecipient(recibe);
			pm.setSender(envia);
			pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");
			pm.setSubject("Tutoria");

			this.privateMessageService.save(pm);

			Assert.isTrue(envia.getMessageWrites().size() == numeroDeMensajesEnviados + 1);
			Assert.isTrue(recibe.getMessageReceives().size() == numeroDeMensajesRecibidos + 1);

			this.unauthenticate();
			this.privateMessageService.flush();

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
			{   //user 1 envia mensaje correcto a user 2
				"user1", 1222, 1223, null
			}, {
				//user 2 enviar mensaje correcto a user 1
				"user2", 1223, 1222, null
			}, {
				//user 1 envia mensaje correcto a admin
				"user1", 1222, 1187, null
			}, {
				//Simular post hacking. Logeado como user 3, pero en realidad soy user 2 intentando enviar mensaje a user 1
				"user3", 1223, 1222, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta enviar mensaje a otro actor.
				null, 1222, 1223, IllegalArgumentException.class
			}, {
				//User 1 intenta enviar a un user que no existe un mensaje
				"user1", 1222, 50000, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template1((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Crear mensajes con errores de validacion
	protected void template2(final String username, final int enviarId, final int recibirId, final int opcionId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Actor envia = this.actorService.findOneToSent(enviarId);
			final Actor recibe = this.actorService.findOneToSent(recibirId);

			final Collection<PrivateMessage> enviadosInicial = envia.getMessageWrites();
			final int numeroDeMensajesEnviados = enviadosInicial.size();

			final Collection<PrivateMessage> recibidosInicial = recibe.getMessageReceives();
			final int numeroDeMensajesRecibidos = recibidosInicial.size();

			final PrivateMessage pm = this.privateMessageService.create();

			if (opcionId == 1) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSender(envia);
				pm.setSubject("Reunion de mañana");

			}
			if (opcionId == 2) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSender(envia);
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 3) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setSender(envia);
				pm.setSubject("Reunion de mañana");
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 4) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSubject("Reunion de mañana");
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 5) {

			}

			this.privateMessageService.save(pm);

			this.unauthenticate();
			this.privateMessageService.flush();

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
			{
				//user 2 intenta enviar mensaje sin texto a user 1
				"user2", 1223, 1222, 1, ConstraintViolationException.class
			}, {
				//user 2 intenta enviar mensaje sin titulo a user 3
				"user2", 1223, 1224, 2, ConstraintViolationException.class
			}, {
				//user 1 intenta enviar mensaje sin recipient
				"user1", 1222, 1223, 3, NullPointerException.class
			}, {
				//No existe sender
				"user1", 1222, 1223, 4, NullPointerException.class
			}, {
				//Mensaje vacio
				"user1", 1222, 1223, 5, NullPointerException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Delete a received message
	protected void template3(final String username, final int privateMessageId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final PrivateMessage c = this.privateMessageService.findOne(privateMessageId);

			this.privateMessageService.deleteReceived(c);

			final Collection<PrivateMessage> cc = this.privateMessageService.findAll();

			Assert.isTrue(!cc.contains(c));

			this.unauthenticate();
			this.privateMessageService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	// user 2 tiene message2 598 y user 4 su copia 1233
	//message 3 = 1234
	@Test
	public void driver3() {

		final Object testingData[][] = {
			{
				//user 4 elimina un message suyo
				"user4", 1233, null
			}, {
				//user 4 intenta eliminar un message que no es suyo
				"user2", 1234, IllegalArgumentException.class
			}

			, {
				//user 2 intenta eliminar un message que no existe
				"user2", 20000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Delete a received message
	protected void template4(final String username, final int privateMessageId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final PrivateMessage c = this.privateMessageService.findOne(privateMessageId);

			this.privateMessageService.deleteSent(c);

			final Collection<PrivateMessage> cc = this.privateMessageService.findAll();

			Assert.isTrue(!cc.contains(c));

			this.unauthenticate();
			this.privateMessageService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//message 3 = 1234
	@Test
	public void driver4() {

		final Object testingData[][] = {
			{
				//user 2 elimina un message suyo
				"user2", 1233, null
			}, {
				//user 2 intenta eliminar un message que no es suyo
				"user2", 1234, IllegalArgumentException.class
			}

			, {
				//user 1 intenta eliminar un message que no existe
				"user1", 10000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template4((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

}
