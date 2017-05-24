
package services;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommentRepository;
import domain.Comment;
import domain.ShoppingGroup;
import domain.User;

@Service
@Transactional
public class CommentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CommentRepository	commentRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;


	// Constructors -----------------------------------------------------------

	public CommentService() {
		super();
	}

	//Simple CRUD -------------------------------------------------------------

	public Comment create(final ShoppingGroup sh) {
		Comment res;
		res = new Comment();

		res.setMoment(new Date());
		return res;
	}

	public Comment reconstruct(final Comment comment, final BindingResult binding) {
		Comment result;

		if (comment.getId() == 0) {
			result = comment;
			this.validator.validate(result, binding);
		} else {
			result = this.commentRepository.findOne(comment.getId());

			result.setText(comment.getText());
			result.setTitle(comment.getTitle());

			this.validator.validate(result, binding);
		}

		return result;
	}

	public Comment findOne(final int commentId) {
		Comment res;
		res = this.commentRepository.findOne(commentId);
		Assert.notNull(res);
		return res;
	}

	public boolean checkUserPrincipal() {
		final boolean res;
		User principal;

		principal = this.userService.findByPrincipal();

		res = principal != null;

		return res;
	}

	public Comment saveAndFlush(final Comment c) {
		Assert.isTrue(this.checkUserPrincipal());
		Assert.notNull(c);
		return this.commentRepository.saveAndFlush(c);

	}

	public void delete(final Comment p) {
		Assert.isTrue(this.checkUserPrincipal());
		Assert.notNull(p);
		this.commentRepository.delete(p);
	}

	public void flush() {
		this.commentRepository.flush();
	}

}
