
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PrivateMessageRepository;
import domain.PrivateMessage;

@Service
@Transactional
public class PrivateMessageService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private PrivateMessageRepository	privateMessageRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public PrivateMessageService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public PrivateMessage create() {
		PrivateMessage res;
		res = new PrivateMessage();
		return res;
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

	public PrivateMessage save(final PrivateMessage p) {
		Assert.notNull(p);
		return this.privateMessageRepository.save(p);

	}

	public void delete(final PrivateMessage p) {
		Assert.notNull(p);
		this.privateMessageRepository.delete(p);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.privateMessageRepository.flush();
	}
}
