package ${package}.jakarta.cafe.model.entity;

import java.io.Serializable;

import ${jeePackage}.persistence.Entity;
import ${jeePackage}.persistence.GeneratedValue;
import ${jeePackage}.persistence.GenerationType;
import ${jeePackage}.persistence.Id;
import ${jeePackage}.persistence.NamedQuery;
import ${jeePackage}.validation.constraints.NotBlank;
import ${jeePackage}.validation.constraints.NotNull;
import ${jeePackage}.validation.constraints.PositiveOrZero;


@Entity
@NamedQuery(name = "findAllCoffees", query = "SELECT o FROM Coffee o ORDER BY o.name")
public class Coffee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
