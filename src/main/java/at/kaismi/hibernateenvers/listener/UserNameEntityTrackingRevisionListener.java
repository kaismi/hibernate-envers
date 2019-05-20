package at.kaismi.hibernateenvers.listener;

import at.kaismi.hibernateenvers.domain.Company;
import at.kaismi.hibernateenvers.domain.Person;
import at.kaismi.hibernateenvers.domain.UserNameRevision;
import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;

public class UserNameEntityTrackingRevisionListener implements EntityTrackingRevisionListener {

    @Override
    public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType,
            Object revisionEntity) {
        if (entityClass.equals(Person.class)) {
            System.out.println(String.format("Person entity changed - id %s", entityId));
        }

        if (entityClass.equals(Company.class)) {
            System.out.println(String.format("Company entity changed - id %s", entityId));
        }
    }

    @Override public void newRevision(Object revisionEntity) {
        UserNameRevision rev = (UserNameRevision)revisionEntity;
        rev.setUserName(getUserName());
    }

    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
