package at.kaismi.hibernateenvers.web;

import at.kaismi.domain.*;
import at.kaismi.hibernateenvers.domain.*;
import at.kaismi.hibernateenvers.repository.CompanyRepository;
import at.kaismi.hibernateenvers.repository.PersonRepository;
import at.kaismi.hibernateenvers.service.PersonPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequestMapping("/companies") @RestController public class CompanyRestController {

    @Autowired private CompanyRepository companyRepository;
    @Autowired private PersonRepository personRepository;
    @Autowired private PersonPersistenceService personPersistenceService;
    @Autowired private EntityManager entityManager;

    @RequestMapping(method = RequestMethod.POST) @ResponseStatus(HttpStatus.CREATED)
    public CompanyInfo create(@RequestBody CompanyInfo companyInfo) {
        if (!Objects.isNull(companyInfo.getId())) {
            throw new IllegalArgumentException("Require no id for create.");
        }

        Company company = new Company();
        validateExistingName(companyInfo);

        Optional.ofNullable(companyRepository.getByName(companyInfo.getName())).ifPresent(c -> {
            throw new IllegalArgumentException(String.format("Company with name %s already exists.", c.getName()));
        });

        company.setName(companyInfo.getName());
        companyRepository.save(company);

        return CompanyInfo.fromCompany(company);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET) public CompanyInfo get(@PathVariable Long id) {
        return CompanyInfo.fromCompany(
                Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(EntityNotFoundException::new));
    }

    @RequestMapping(value = "/{id}/revisions", method = RequestMethod.GET)
    public Set<Long> getRevisions(@PathVariable Long id) {
        return AuditReaderFactory.get(entityManager).getRevisions(Company.class, id).stream()
                .mapToLong(Number::longValue).boxed().collect(Collectors.toSet());
    }

    @RequestMapping(value = "/{id}/revisions/{revisionId}", method = RequestMethod.GET)
    public CompanyRevision getRevision(@PathVariable Long id, @PathVariable Integer revisionId) {
        Company company = AuditReaderFactory.get(entityManager).find(Company.class, id, revisionId);
        UserNameRevision userNameRevision = AuditReaderFactory.get(entityManager)
                .findRevision(UserNameRevision.class, revisionId);

        return CompanyRevision.fromCompany(company, userNameRevision.getRevisionDate(), userNameRevision.getId(),
                userNameRevision.getUserName(),
                company.getEmployees().stream().map(PersonInfo::fromPerson).collect(Collectors.toSet()));
    }

    @RequestMapping(method = RequestMethod.GET) public Set<CompanyInfo> getAll() {
        Iterable<Company> companies = companyRepository.findAll();

        return StreamSupport.stream(companies.spliterator(), false).map(CompanyInfo::fromCompany)
                .collect(Collectors.toSet());
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET) public Integer getCount() {
        return (int)companyRepository.count();
    }

    @RequestMapping(value = "/{id}/employees", method = RequestMethod.GET)
    public Set<PersonInfo> getEmployees(@PathVariable Long id) {
        return Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(EntityNotFoundException::new)
                .getEmployees().stream().map(PersonInfo::fromPerson).collect(Collectors.toSet());
    }

    @RequestMapping(value = "/{id}/employees/count", method = RequestMethod.GET)
    public Integer getEmployeesCount(@PathVariable Long id) {
        return Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(EntityNotFoundException::new)
                .getEmployees().size();
    }

    @RequestMapping(value = "/{id}/employees/{employeeId}", method = RequestMethod.GET)
    public PersonInfo getEmployee(@PathVariable Long id, @PathVariable Long employeeId) {
        Company company = Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(EntityNotFoundException::new);

        Person person = Optional.ofNullable(personRepository.findOne(employeeId))
                .orElseThrow(EntityNotFoundException::new);

        if (!person.isEmployedAt(company)) {
            throw new IllegalArgumentException(String.format("Person with id %d is not an employee.", person.getId()));
        }

        return PersonInfo.fromPerson(person);
    }

    @Transactional @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public CompanyInfo update(@PathVariable Long id, @RequestBody CompanyInfo companyInfo) {
        Company company = Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(EntityNotFoundException::new);

        Optional.ofNullable(companyInfo.getId()).ifPresent(i -> {
            if (!i.equals(company.getId())) {
                throw new IllegalArgumentException("No id match.");
            }
        });

        validateExistingName(companyInfo);

        Optional.ofNullable(companyRepository.getByName(companyInfo.getName())).ifPresent(c -> {
            if (!c.getId().equals(company.getId())) {
                throw new IllegalArgumentException(String.format("Company with name %s already exists.", c.getName()));
            }
        });

        company.setName(companyInfo.getName());
        return CompanyInfo.fromCompany(company);
    }

    @Transactional @RequestMapping(value = "/{id}/employees", method = RequestMethod.PUT)
    public Set<PersonInfo> updateEmployees(@PathVariable Long id, @RequestBody Set<PersonInfo> employees) {
        Company company = Optional.ofNullable(companyRepository.findOne(id)).orElseThrow(EntityNotFoundException::new);

        final List<Long> validEmployeeIds = employees.parallelStream().mapToLong(e -> {
            if (Objects.isNull(e.getId())) {
                throw new IllegalArgumentException("Invalid id.");
            }

            return e.getId();
        }).boxed().collect(Collectors.toList());

        employees.forEach(personInfo -> {
            Optional.ofNullable(personInfo.getCompanyId()).ifPresent(companyId -> {
                if (!company.getId().equals(companyId)) {
                    throw new IllegalArgumentException("Invalid company id.");
                }
            });

            if (Collections.frequency(validEmployeeIds, personInfo.getId()) > 1) {
                throw new IllegalArgumentException("Duplicate ids.");
            }

            personInfo.setCompanyId(company.getId());
            personPersistenceService.update(personInfo);
        });

        company.getEmployees().stream().map(PersonInfo::fromPerson)
                .filter(personInfo -> !validEmployeeIds.contains(personInfo.getId())).forEach(personInfo -> {
            personInfo.setCompanyId(null);
            personPersistenceService.update(personInfo);
        });

        return employees;
    }

    private void validateExistingName(CompanyInfo companyInfo) {
        if (StringUtils.isBlank(companyInfo.getName())) {
            throw new IllegalArgumentException("Name must not be blank.");
        }
    }
}
