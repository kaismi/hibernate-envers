package at.kaismi.domain;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Audited public class Company {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    @Column(unique = true, nullable = false) private String name;
    @OneToMany(mappedBy = "company") private Set<Person> employees = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployees(Set<Person> employees) {
        this.employees = employees;
    }

    public Set<Person> getEmployees() {
        return employees;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Company company = (Company)o;
        return id.equals(company.id);
    }

    @Override public int hashCode() {
        return id.hashCode();
    }

    @Override public String toString() {
        return "Company{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
