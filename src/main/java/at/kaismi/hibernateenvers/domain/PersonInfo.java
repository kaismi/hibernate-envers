package at.kaismi.hibernateenvers.domain;

import java.util.Objects;

public class PersonInfo {

    private Long id;
    private String firstname;
    private String lastname;
    private Long companyId;

    public static PersonInfo fromPerson(Person person) {
        Objects.requireNonNull(person);
        PersonInfo personInfo = new PersonInfo();
        personInfo.setFirstname(person.getFirstname());
        personInfo.setLastname(person.getLastname());
        personInfo.setId(person.getId());
        personInfo.setCompanyId(Objects.isNull(person.getCompany()) ? null : person.getCompany().getId());
        return personInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getCompanyId() {
        return companyId;
    }
}
