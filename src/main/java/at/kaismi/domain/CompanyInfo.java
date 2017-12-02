package at.kaismi.domain;

import java.util.Objects;

public class CompanyInfo {

    private Long id;
    private String name;

    public static CompanyInfo fromCompany(Company company) {
        Objects.requireNonNull(company);
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setId(company.getId());
        companyInfo.setName(company.getName());
        return companyInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CompanyInfo that = (CompanyInfo)o;

        if (id != null ? !id.equals(that.id) : that.id != null)
            return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
