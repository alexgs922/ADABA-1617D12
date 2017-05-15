
package services;

import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.User;
import forms.RegistrationForm;

@Service
@Transactional
public class UserService {

	// ---------- Repositories----------------------

	@Autowired
	private UserRepository			userRepository;

	// Supporting services ------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Md5PasswordEncoder		encoder;

	@Autowired
	private Validator				validator;

	@Autowired
	private AdministratorService	administratorService;


	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		User result;

		result = new User();

		return result;
	}

	public User reconstruct(final RegistrationForm customerForm) {
		// TODO hacer reconstruct!!!
		User result;
		UserAccount userAccount;
		Authority authority;
		Collection<Authority> authorities;
		String pwdHash;

		result = this.create();
		authorities = new HashSet<Authority>();
		userAccount = new UserAccount();

		result.setName(customerForm.getName());
		result.setSurName(customerForm.getSurName());
		result.setPhone(customerForm.getPhone());
		result.setEmail(customerForm.getEmail());

		authority = new Authority();
		authority.setAuthority(Authority.USER);
		authorities.add(authority);
		pwdHash = this.encoder.encodePassword(customerForm.getPassword(), null);
		userAccount.setAuthorities(authorities);
		userAccount.setPassword(pwdHash);
		userAccount.setUsername(customerForm.getUsername());
		result.setUserAccount(userAccount);

		return result;
	}

	public User reconstruct(final User user, final BindingResult binding) {
		// TODO hacer reconstruct!!!
		User result;
		final int principal = this.actorService.findByPrincipal().getId();
		final int principalUser = user.getId();

		Assert.isTrue(principal == principalUser);
		if (user.getId() == 0)
			result = user;
		else {
			result = this.userRepository.findOne(user.getId());
			result.setName(user.getName());
			result.setSurName(user.getSurName());
			result.setEmail(user.getEmail());
			result.setPhone(user.getPhone());
			this.validator.validate(result, binding);
		}
		return result;
	}

	public User findOne(final int userId) {
		User res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		res = this.userRepository.findOne(userId);
		Assert.notNull(res);

		return res;
	}

	public Collection<User> findAll() {
		Collection<User> res;
		res = this.userRepository.findAll();
		return res;
	}

	public User save(final User user) {
		Assert.notNull(user);
		return this.userRepository.save(user);

	}

	public User findOneToSent(final int userId) {

		User result;

		result = this.userRepository.findOne(userId);
		Assert.notNull(result);

		return result;

	}

	// Other business methods ----------------------------------------------------

	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public User findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		User result;

		result = this.userRepository.findByUserAccountId(userAccount.getId());

		return result;
	}

	public boolean checkAdminPrincipal() {
		final boolean res;
		Administrator principal;

		principal = this.administratorService.findByPrincipal();

		res = principal != null;

		return res;
	}

	public void flush() {
		this.userRepository.flush();

	}
}
