package at.kaismi.hibernateenvers.listener;

import at.kaismi.hibernateenvers.domain.UserNameRevision;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserNameRevisionListener implements RevisionListener {

    @Override public void newRevision(Object revisionEntity) {
        UserNameRevision rev = (UserNameRevision)revisionEntity;
        rev.setUserName(getUserName());
    }

    public String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
