package at.kaismi.hibernateenvers.service;

import at.kaismi.hibernateenvers.domain.Company;
import at.kaismi.hibernateenvers.domain.Person;
import at.kaismi.hibernateenvers.domain.PersonInfo;
import at.kaismi.hibernateenvers.repository.CompanyRepository;
import at.kaismi.hibernateenvers.repository.PersonRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Service public class PersonPersistenceService {

    @Autowired private PersonRepository personRepository;
    @Autowired private CompanyRepository companyRepository;

    public long create(PersonInfo personInfo) {
        Objects.requireNonNull(personInfo);
        if (!Objects.isNull(personInfo.getId())) {
            throw new IllegalArgumentException("Require no id for create.");
        }

        Person person = new Person();
        assignName(personInfo, person);
        assignCompany(personInfo, person);

        personRepository.save(person);

        return person.getId();
    }

    public void update(PersonInfo personInfo) {
        Objects.requireNonNull(personInfo);

        if (Objects.isNull(personInfo.getId())) {
            throw new IllegalArgumentException("Id for person must not be null.");
        }

        Person person = Optional.ofNullable(personRepository.findOne(personInfo.getId()))
                .orElseThrow(EntityNotFoundException::new);

        assignName(personInfo, person);
        assignCompany(personInfo, person);
    }

    private void assignCompany(PersonInfo personInfo, Person person) {
        if (!Objects.isNull(personInfo.getCompanyId())) {
            person.setCompany(loadCompany(personInfo.getCompanyId()));
        } else {
            person.setCompany(null);
        }
    }

    private Company loadCompany(long companyId) {
        // bug? - does not work in my java 8 version
       /* return Optional.ofNullable(companyRepository.findOne(companyId)).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });*/
        Company company = companyRepository.findOne(companyId);
        if (Objects.isNull(company)) {
            throw new EntityNotFoundException();
        }

        return company;
    }

    private void assignName(PersonInfo personInfo, Person person) {
        validateExistingName(personInfo);
        person.setFirstname(personInfo.getFirstname());
        person.setLastname(personInfo.getLastname());
    }

    private void validateExistingName(PersonInfo personInfo) {
        if (StringUtils.isBlank(personInfo.getFirstname()) || StringUtils.isBlank(personInfo.getLastname())) {
            throw new IllegalArgumentException("Name for person must not be blank.");
        }
    }
}
