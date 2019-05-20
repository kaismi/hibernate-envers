package at.kaismi.hibernateenvers.domain;

import java.util.Date;

public class PersonRevision {

    private PersonInfo personInfo;
    private Date revisionDate;
    private int revisionId;
    private String modifiedBy;

    public static PersonRevision fromPerson(Person person, Date revisionDate, int revisionId, String modifiedBy) {
        PersonRevision companyRevision = new PersonRevision();
        companyRevision.setPersonInfo(PersonInfo.fromPerson(person));
        companyRevision.setRevisionDate(revisionDate);
        companyRevision.setRevisionId(revisionId);
        companyRevision.setModifiedBy(modifiedBy);
        return companyRevision;
    }

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfo = personInfo;
    }

    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public Date getRevisionDate() {
        return revisionDate;
    }

    public int getRevisionId() {
        return revisionId;
    }

    public void setRevisionId(int revisionId) {
        this.revisionId = revisionId;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }
}
