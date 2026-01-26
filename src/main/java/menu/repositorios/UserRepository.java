package  menu.repositorios;

 
import  menu.modelo.User;
 import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User  findByUsername(String username);

	User findByEmail(String email);
	Optional<User> findById(Integer id); }
