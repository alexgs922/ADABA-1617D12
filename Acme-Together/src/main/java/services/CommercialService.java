
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CommercialRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Commercial;
import domain.PuntuableEntity;
import forms.CommercialForm;

@Service
@Transactional
public class CommercialService {

	// ---------- Repositories----------------------

	@Autowired
	private CommercialRepository	commercialRepository;

	// Supporting services ------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Md5PasswordEncoder		encoder;

	@Autowired
	private Validator				validator;

	@Autowired
	private AdministratorService	administratorService;
	
	@Autowired
	private PuntuableEntityService puntuableEntity;	

	// Simple CRUD methods ----------------------------------------------------

	public Commercial create() {
		Commercial result;

		result = new Commercial();

		return result;
	}

	public Commercial reconstruct(final CommercialForm customerForm) {
		// TODO hacer reconstruct!!!
		Commercial result;
		UserAccount userAccount;
		Authority authority;
		Collection<Authority> authorities;
		String pwdHash;
	
		Collection<PuntuableEntity> puntuable;
		
		result = this.create();
		authorities = new HashSet<Authority>();
		userAccount = new UserAccount();
		puntuable = new ArrayList<PuntuableEntity>();
		
		result.setName(customerForm.getName());
		result.setSurName(customerForm.getSurName());
		result.setPhone(customerForm.getPhone());
		result.setEmail(customerForm.getEmail());
		result.setCompanyName(customerForm.getCompanyName());
		result.setVatNumber(customerForm.getVatNumber());
		result.setToPuntuate(puntuable);
		
		authority = new Authority();
		authority.setAuthority(Authority.COMMERCIAL);
		authorities.add(authority);
		pwdHash = this.encoder.encodePassword(customerForm.getPassword(), null);
		userAccount.setAuthorities(authorities);
		userAccount.setPassword(pwdHash);
		userAccount.setUsername(customerForm.getUsername());
		result.setUserAccount(userAccount);

		return result;
	}

	public Commercial reconstruct(final Commercial commercial, final BindingResult binding) {
		// TODO hacer reconstruct!!!
		Commercial result;
		final int principal = this.actorService.findByPrincipal().getId();
		final int principalUser = commercial.getId();

		Assert.isTrue(principal == principalUser);
		if (commercial.getId() == 0)
			result = commercial;
		else {
			result = this.commercialRepository.findOne(commercial.getId());
			result.setName(commercial.getName());
			result.setSurName(commercial.getSurName());
			result.setEmail(commercial.getEmail());
			result.setPhone(commercial.getPhone());
			this.validator.validate(result, binding);
		}
		return result;
	}

	public Commercial findOne(final int commercialId) {
		Commercial res;
		final Actor principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);

		res = this.commercialRepository.findOne(commercialId);
		Assert.notNull(res);

		return res;
	}

	public Collection<Commercial> findAll() {
		Collection<Commercial> res;
		res = this.commercialRepository.findAll();
		return res;
	}

	public Commercial save(final Commercial commercial) {
		Assert.notNull(commercial);
		return this.commercialRepository.save(commercial);

	}

	public Commercial findOneToSent(final int commercialId) {

		Commercial result;

		result = this.commercialRepository.findOne(commercialId);
		Assert.notNull(result);

		return result;

	}

	// Other business methods ----------------------------------------------------

	public Commercial findByPrincipal() {
		Commercial result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		result = this.findByUserAccount(userAccount);

		return result;
	}

	public Commercial findByUserAccount(final UserAccount userAccount) {
		Assert.notNull(userAccount);

		Commercial result;

		result = this.commercialRepository.findByUserAccountId(userAccount.getId());

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
		this.commercialRepository.flush();

	}
}
