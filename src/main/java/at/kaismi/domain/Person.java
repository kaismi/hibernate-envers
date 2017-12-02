package at.kaismi.domain;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;

@Entity @Audited public class Person {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    @Column(nullable = false) @NotAudited private String firstname;
    @Column(nullable = false) private String lastname;

    @OneToOne private Company company;

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

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public boolean isEmployedAt(Company company) {
        return this.company != null && this.company.equals(company);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Person person = (Person)o;
        return id.equals(person.id);
    }

    @Override public int hashCode() {
        return id.hashCode();
    }

    @Override public String toString() {
        return "Person{" + "id=" + id + ", firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' + '}';
    }
}
