
package domain;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import security.UserAccount;

public class Actor extends ToPuntuate {

	//Constructor ---------------------------------------

	public Actor() {
		super();
	}


	//Attributes -----------------------------------------

	private String	name;
	private String	surName;
	private String	email;
	private String	phone;


	//Getters and Setters ---------------------------------

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurName() {
		return this.surName;
	}

	public void setSurName(final String surName) {
		this.surName = surName;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?\\d{1,3}?[- .]?\\d+$")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	private UserAccount	userAccount;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	private Collection<Message>	messageReceives;
	private Collection<Message>	messageWrites;


	@OneToMany(mappedBy = "recipient")
	public Collection<Message> getMessageReceives() {
		return this.messageReceives;
	}

	public void setMessageReceives(final Collection<Message> messageReceives) {
		this.messageReceives= messageReceives;
	}

	@OneToMany(mappedBy = "sender")
	public Collection<Message> getMessageWrites() {
		return this.messageWrites;
	}

	public void setMessageWrites(final Collection<Message> messageWrites) {
		this.messageWrites= messageWrites;
	}


	
	
	
}
