
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.PrivateMessageRepository;
import domain.Actor;
import domain.PrivateMessage;
import domain.ShoppingGroup;
import domain.User;

@Service
@Transactional
public class PrivateMessageService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PrivateMessageRepository	privateMessageRepository;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private Validator					validator;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public PrivateMessageService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public PrivateMessage create() {
		PrivateMessage res;

		res = new PrivateMessage();
		res.setCopy(true);

		return res;
	}

	public PrivateMessage create(final Actor c) {
		PrivateMessage res;

		res = new PrivateMessage();

		res.setRecipient(c);
		res.setCopy(true);

		return res;
	}

	public PrivateMessage reconstruct(final PrivateMessage privateMessage, final BindingResult binding) {

		PrivateMessage result;
		final Actor t = this.actorService.findByPrincipal();

		result = privateMessage;
		//final String text = this.tasteService.checkContactInfo(privateMessage.getText());
		//final String subject = this.tasteService.checkContactInfo(privateMessage.getSubject());

		result.setRecipient(privateMessage.getRecipient());
		result.setSender(t);
		result.setText(privateMessage.getText());
		result.setSubject(privateMessage.getSubject());
		result.setAttachments(privateMessage.getAttachments());
		result.setMoment(new Date(System.currentTimeMillis() - 1000));
		result.setCopy(privateMessage.isCopy());
		this.validator.validate(result, binding);

		return result;

	}

	public void save(final PrivateMessage m) {
		Assert.notNull(m);

		final Actor c = this.actorService.findByPrincipal();

		final PrivateMessage privateMessage1 = new PrivateMessage();
		privateMessage1.setSubject(m.getSubject());
		privateMessage1.setText(m.getText());
		privateMessage1.setMoment(m.getMoment());
		privateMessage1.setAttachments(m.getAttachments());
		privateMessage1.setRecipient(m.getRecipient());
		privateMessage1.setSender(m.getSender());
		privateMessage1.setCopy(false);

		final PrivateMessage privateMessage2 = new PrivateMessage();
		privateMessage2.setSubject(m.getSubject());
		privateMessage2.setText(m.getText());
		privateMessage2.setMoment(m.getMoment());
		privateMessage2.setAttachments(m.getAttachments());
		privateMessage2.setRecipient(m.getRecipient());
		privateMessage2.setSender(m.getSender());
		privateMessage2.setCopy(true);

		final Collection<PrivateMessage> cr = m.getRecipient().getMessageReceives();
		cr.add(privateMessage1);

		final Collection<PrivateMessage> cs = m.getSender().getMessageWrites();
		cs.add(privateMessage2);

		Assert.isTrue(c.getId() == m.getSender().getId());

		this.privateMessageRepository.save(privateMessage2);
		this.privateMessageRepository.save(privateMessage1);

	}

	public Collection<PrivateMessage> findAll() {
		Collection<PrivateMessage> res;
		res = this.privateMessageRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public PrivateMessage findOne(final int privateMessageId) {
		PrivateMessage res;
		res = this.privateMessageRepository.findOne(privateMessageId);
		Assert.notNull(res);
		return res;
	}

	public void deleteReceived(final PrivateMessage m) {
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(m);

		Assert.isTrue(principal.getId() == m.getRecipient().getId());

		this.privateMessageRepository.delete(m.getId());

	}

	public void deleteSent(final PrivateMessage m) {
		Assert.notNull(m);
		final Actor principal = this.actorService.findByPrincipal();
		Assert.isTrue(principal.getId() == m.getSender().getId());

		this.privateMessageRepository.delete(m.getId());

	}

	public void delete(final PrivateMessage p) {
		Assert.notNull(p);
		this.privateMessageRepository.delete(p);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.privateMessageRepository.flush();
	}

	public Collection<PrivateMessage> mySendedMessages(final int actorId) {

		final Collection<PrivateMessage> sm = this.privateMessageRepository.mySendedMessages(actorId);

		return sm;
	}

	public Collection<PrivateMessage> myRecivedMessages(final int actorId) {

		final Collection<PrivateMessage> sm = this.privateMessageRepository.myRecivedMessages(actorId);

		return sm;
	}

	public void deleteGroupMessage(final ShoppingGroup shGroup, final User principal) {

		Assert.isTrue(principal.getMyShoppingGroups().contains(shGroup));
		final Collection<User> usersToMessage = new ArrayList<User>();
		usersToMessage.addAll(shGroup.getUsers());

		for (final User user : usersToMessage)
			if (user.getId() != principal.getId()) {

				final PrivateMessage mensaje = this.create();
				mensaje.setSubject("I have canceled this group:  " + shGroup.getName() + " // He cancelado este grupo: " + shGroup.getName());
				mensaje.setText("I have canceled this group, you may want to know! // ¡He cancelado este grupo, quizás quieras saberlo!");
				final Date current = new Date();
				mensaje.setMoment(current);
				mensaje.setAttachments("");
				mensaje.setRecipient(user);
				mensaje.setSender(principal);
				mensaje.setCopy(false);

				final Collection<PrivateMessage> cr = user.getMessageReceives();
				cr.add(mensaje);

				this.privateMessageRepository.save(mensaje);
			}
	}

}
