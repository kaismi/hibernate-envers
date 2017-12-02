package at.kaismi.domain;

import at.kaismi.listener.UserNameEntityTrackingRevisionListener;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;

@Entity @RevisionEntity(UserNameEntityTrackingRevisionListener.class) public class UserNameRevision
        extends DefaultRevisionEntity {

    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
