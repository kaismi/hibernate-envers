package at.kaismi.web;

import at.kaismi.domain.CompanyInfo;
import at.kaismi.domain.PersonInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore // Remove if your service is running and start test to generate example data
@RunWith(SpringRunner.class) public class LocalhostTestDataOneTimeTest {

    @TestConfiguration public static class LocalhostTestDataOneTimeTestConfig {

        @Bean public TestRestTemplate restTemplate() {
            return new TestRestTemplate("foo", "bar");
        }
    }

    @Autowired private TestRestTemplate testRestTemplate;

    @Test public void createTestData() {
        long microsoftId = createCompany("Microsoft");
        long appleId = createCompany("Apple");
        long googleId = createCompany("Google");

        // Employed persons of foo family
        createPerson("John", "Foo", microsoftId);
        createPerson("Peter", "Foo", appleId);
        createPerson("Gregory", "Foo", googleId);

        // Unemployed persons of family
        createPerson("Claudia", "Foo", null);
        createPerson("Michael", "Foo", null);
        createPerson("Dominik", "Foo", null);
        createPerson("Thomas", "Foo", null);
        createPerson("Martin", "Foo", null);
        createPerson("James", "Foo", null);
    }

    private void createPerson(String firstname, String lastname, Long companyId) {
        PersonInfo personInfo = new PersonInfo();
        personInfo.setFirstname(firstname);
        personInfo.setLastname(lastname);
        personInfo.setCompanyId(companyId);
        ResponseEntity<PersonInfo> personInfoResponseEntity = testRestTemplate
                .postForEntity("http://localhost:8080/persons", personInfo, PersonInfo.class);
    }

    private long createCompany(String name) {
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setName(name);
        ResponseEntity<CompanyInfo> companyInfoResponseEntity = testRestTemplate
                .postForEntity("http://localhost:8080/companies", companyInfo, CompanyInfo.class);
        return companyInfoResponseEntity.getBody().getId();
    }

}
