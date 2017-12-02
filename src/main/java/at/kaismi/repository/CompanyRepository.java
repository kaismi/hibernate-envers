package at.kaismi.repository;

import at.kaismi.domain.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company getByName(String name);
}
