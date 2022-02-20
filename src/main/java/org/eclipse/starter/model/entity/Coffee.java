package org.eclipse.starter.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@NamedQuery(name = "findAllCoffees", query = "SELECT o FROM Coffee o ORDER BY o.id")
public class Coffee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@NotBlank(message = "Name cannot be blank.")
	protected String name;

	@NotNull(message = "Price must be set.")
	@PositiveOrZero(message = "Price must be greater than or equal to zero.")
	protected Double price;

	public Coffee() {
		// Required empty constructor.
	}

	public Coffee(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Coffee)) {
			return false;
		}
		Coffee other = (Coffee) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Coffee[id=" + id + ", name=" + name + ", price=" + price + "]";
	}
}
