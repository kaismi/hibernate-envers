package at.kaismi.domain;

import java.util.Date;
import java.util.Set;

public class CompanyRevision {

    private CompanyInfo companyInfo;
    private Set<PersonInfo> employees;
    private Date revisionDate;
    private int revisionId;
    private String modifiedBy;

    public static CompanyRevision fromCompany(Company company, Date revisionDate, int revisionId, String modifiedBy,
            Set<PersonInfo> employees) {
        CompanyRevision companyRevision = new CompanyRevision();
        companyRevision.setCompanyInfo(CompanyInfo.fromCompany(company));
        companyRevision.setRevisionDate(revisionDate);
        companyRevision.setRevisionId(revisionId);
        companyRevision.setModifiedBy(modifiedBy);
        companyRevision.setEmployees(employees);
        return companyRevision;
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
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

    public void setEmployees(Set<PersonInfo> employees) {
        this.employees = employees;
    }

    public Set<PersonInfo> getEmployees() {
        return employees;
    }
}
