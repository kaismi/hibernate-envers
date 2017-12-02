package at.kaismi.web;

import at.kaismi.domain.Person;
import at.kaismi.domain.PersonInfo;
import at.kaismi.domain.PersonRevision;
import at.kaismi.domain.UserNameRevision;
import at.kaismi.repository.PersonRepository;
import at.kaismi.service.PersonPersistenceService;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequestMapping("/persons") @RestController public class PersonRestController {

    @Autowired private PersonRepository personRepository;
    @Autowired private PersonPersistenceService personPersistenceService;
    @Autowired private EntityManager entityManager;

    @RequestMapping(method = RequestMethod.POST) @ResponseStatus(HttpStatus.CREATED)
    public PersonInfo create(@RequestBody PersonInfo personInfo) {
        long id = personPersistenceService.create(personInfo);
        personInfo.setId(id);
        return personInfo;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET) public PersonInfo get(@PathVariable Long id) {
        return PersonInfo.fromPerson(
                Optional.ofNullable(personRepository.findOne(id)).orElseThrow(EntityNotFoundException::new));
    }

    @RequestMapping(method = RequestMethod.GET) public Set<PersonInfo> getAll() {
        Iterable<Person> companies = personRepository.findAll();

        return StreamSupport.stream(companies.spliterator(), false).map(PersonInfo::fromPerson)
                .collect(Collectors.toSet());
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET) public Integer getCount() {
        return (int)personRepository.count();
    }

    @RequestMapping(value = "/{id}/revisions", method = RequestMethod.GET)
    public Set<Long> getRevisions(@PathVariable Long id) {
        return AuditReaderFactory.get(entityManager).getRevisions(Person.class, id).stream()
                .mapToLong(Number::longValue).boxed().collect(Collectors.toSet());
    }

    @RequestMapping(value = "/{id}/revisions/{revisionId}", method = RequestMethod.GET)
    public PersonRevision getRevision(@PathVariable Long id, @PathVariable Integer revisionId) {
        Person person = AuditReaderFactory.get(entityManager).find(Person.class, id, revisionId);
        UserNameRevision userNameRevision = AuditReaderFactory.get(entityManager)
                .findRevision(UserNameRevision.class, revisionId);
        return PersonRevision.fromPerson(person, userNameRevision.getRevisionDate(), userNameRevision.getId(),
                userNameRevision.getUserName());
    }

    @Transactional @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PersonInfo update(@PathVariable Long id, @RequestBody PersonInfo personInfo) {
        Optional.ofNullable(personInfo.getId()).ifPresent(i -> {
            if (!i.equals(id)) {
                throw new IllegalArgumentException("No id match.");
            }
        });

        personInfo.setId(id);
        personPersistenceService.update(personInfo);
        return personInfo;
    }

}
