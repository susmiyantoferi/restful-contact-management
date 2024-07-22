package projectjava.belajar.resfullapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectjava.belajar.resfullapi.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
