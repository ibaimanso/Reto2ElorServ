package  menu.modelo;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "tipos")
public class Tipo {

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName_eu() {
		return name_eu;
	}

	public void setName_eu(String name_eu) {
		this.name_eu = name_eu;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Id
    private Integer id;

    private String name;
    private String name_eu;

    @OneToMany(mappedBy = "tipo")
    private List<User> users;
}
