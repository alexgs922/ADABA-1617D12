
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

import repositories.DistributorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Distributor;
import forms.RegistrationForm;

@Service
@Transactional
public class DistributorService {

	// ---------- Repositories----------------------

	@Autowired
	private DistributorRepository	distributorRepository;

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

	public Distributor create() {
		Distributor result;

		result = new Distributor();

		return result;
	}

	public Distributor reconstruct(final RegistrationForm customerForm) {
		// TODO hacer reconstruct!!!
		Distributor result;
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
		authority.setAuthority(Authority.DISTRIBUTOR);
		authorities.add(authority);
		pwdHash = this.encoder.encodePassword(customerForm.getPassword(), null);
		userAccount.setAuthorities(authorities);
		userAccount.setPassword(pwdHash);
		userAccount.setUsername(customerForm.getUsername());
		result.setUserAccount(userAccount);

		return result;
	}

	public Distributor reconstruct(final Distributor distributor, final BindingResult binding) {
		// TODO hacer reconstruct!!!
		Distributor result;
		final int principal = this.actorService.findByPrincipal().getId();
		final int principalUser = distributor.getId();

		Assert.isTrue(principal == principalUser);
		if (distributor.getId() == 0)
			result = distributor;
		else {
			result = this.distributorRepository.findOne(distributor.getId());
			result.setName(distributor.getName());
			result.setSurName(distributor.getSurName());
			result.setEmail(distributor.getEmail());
			result.setPhone(distributor.getPhone());
			this.validator.validate(result, binding);
		}
		return result;
	}

	public Distributor findOne(final int distributorId) {
		Distributor res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		res = this.distributorRepository.findOne(distributorId);
		Assert.notNull(res);

		return res;
	}

	public Collection<Distributor> findAll() {
		Collection<Distributor> res;
		res = this.distributorRepository.findAll();
		return res;
	}

	public Distributor save(final Distributor distributor) {
		Assert.notNull(distributor);
		return this.distributorRepository.save(distributor);

	}

	public Distributor findOneToSent(final int distributorId) {

		Distributor result;

		result = this.distributorRepository.findOne(distributorId);
		Assert.notNull(result);

		return result;

	}

	// Other business methods ----------------------------------------------------

	public Distributor findByPrincipal() {
		Distributor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public Distributor findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Distributor result;

		result = this.distributorRepository.findByUserAccountId(userAccount.getId());

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
		this.distributorRepository.flush();

	}
}
