package at.kaismi.hibernateenvers.repository;

import at.kaismi.hibernateenvers.domain.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company getByName(String name);
}
