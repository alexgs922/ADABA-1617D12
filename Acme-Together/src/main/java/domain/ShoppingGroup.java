
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class ShoppingGroup extends PuntuableEntity {

	//Constructor ------------------------------------

	public ShoppingGroup() {
		super();
	}


	//Attributes --------------------------------------

	private boolean				isPrivate;
	private String				name;
	private String				description;
	private Date				lastOrderDate;
	private int					freePlaces;
	private Collection<String>	frecuentedSites;
	private int					puntuation;


	//Getters and Setters ------------------------------

	public boolean isPrivate() {
		return this.isPrivate;
	}

	public void setPrivate(final boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getLastOrderDate() {
		return this.lastOrderDate;
	}

	public void setLastOrderDate(final Date lastOrderDate) {
		this.lastOrderDate = lastOrderDate;
	}

	@NotNull
	public int getFreePlaces() {
		return this.freePlaces;
	}

	public void setFreePlaces(final int freePlaces) {
		this.freePlaces = freePlaces;
	}

	public Collection<String> getFrecuentedSites() {
		return this.frecuentedSites;
	}

	public void setFrecuentedSites(final Collection<String> frecuentedSites) {
		this.frecuentedSites = frecuentedSites;
	}

	@NotNull
	public int getPuntuation() {
		return this.puntuation;
	}

	public void setPuntuation(final int puntuation) {
		this.puntuation = puntuation;
	}
	
	private Collection<User>users;
	private Collection<Product>products;
	private Collection<Comment> comments;
	private Category category;
	private Collection<Engagement> engagements;

	@ManyToMany
	@Valid
	@NotNull
	public Collection<User> getUsers() {
		return users;
	}

	public void setUsers(Collection<User> users) {
		this.users = users;
	}

	@OneToMany(mappedBy="shoppingGroupProducts")
	@Valid
	@NotNull
	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

	@OneToMany(mappedBy="shoppingGroupComments")
	@Valid
	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	@ManyToOne(optional=false)
	@Valid
	@NotNull
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@OneToMany(mappedBy="shoppingGroupEngagements")
	@Valid
	@NotNull
	public Collection<Engagement> getEngagements() {
		return engagements;
	}

	public void setEngagements(Collection<Engagement> engagements) {
		this.engagements = engagements;
	}
	
	

}
