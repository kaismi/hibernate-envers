package at.kaismi.web;

import at.kaismi.domain.Company;
import at.kaismi.domain.CompanyInfo;
import at.kaismi.domain.Person;
import at.kaismi.repository.PersonRepository;
import at.kaismi.service.PersonPersistenceService;
import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/dwh") @RestController public class DwhRestController {

    @Autowired private EntityManager entityManager;

    @RequestMapping(value = "/companies/name/like/{searchString}", method = RequestMethod.GET)
    public Set<CompanyInfo> getCompaniesWhereNameLike(@PathVariable String searchString) {
        List<Company> companies = AuditReaderFactory.get(entityManager).createQuery()
                .forRevisionsOfEntity(Company.class, true, true)
                .add(AuditEntity.property("name").ilike(searchString, MatchMode.ANYWHERE)).getResultList();

        return companies.stream().map(CompanyInfo::fromCompany).distinct().collect(Collectors.toSet());
    }

    @RequestMapping(value = "/companies/employee/lastname/{searchString}", method = RequestMethod.GET)
    public Set<CompanyInfo> getCompaniesEmployeeWhereLastnameLike(@PathVariable String searchString) {
        List<Person> employees = AuditReaderFactory.get(entityManager).createQuery()
                .forRevisionsOfEntity(Person.class, true, true)
                .add(AuditEntity.property("lastname").ilike(searchString, MatchMode.EXACT))
                .add(AuditEntity.property("company").isNotNull()).getResultList();

        return employees.stream().map(Person::getCompany).map(CompanyInfo::fromCompany).distinct()
                .collect(Collectors.toSet());
    }
}
