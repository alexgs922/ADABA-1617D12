
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CommentRepository;
import domain.Comment;

@Service
@Transactional
public class CommentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CommentRepository	commentRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public CommentService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Comment create() {
		Comment res;
		res = new Comment();
		return res;
	}

	public Collection<Comment> findAll() {
		Collection<Comment> res;
		res = this.commentRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Comment findOne(final int commentId) {
		Comment res;
		res = this.commentRepository.findOne(commentId);
		Assert.notNull(res);
		return res;
	}

	public Comment save(final Comment c) {
		Assert.notNull(c);
		return this.commentRepository.save(c);

	}

	public void delete(final Comment c) {
		Assert.notNull(c);
		this.commentRepository.delete(c);
	}

	//Other business methods --------------------------------------

	public void flush() {
		this.commentRepository.flush();
	}
}
