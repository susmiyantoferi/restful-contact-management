package projectjava.belajar.resfullapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectjava.belajar.resfullapi.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
