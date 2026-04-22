package models;

import models.enums.OrganizationType;
import java.io.Serializable;

public class Organization implements Serializable {
    private Integer annualTurnover;  // не null, >0
    private long employeesCount;      // >0
    private OrganizationType type;    // может быть null

    public Organization(Integer annualTurnover, long employeesCount, OrganizationType type) {
        setAnnualTurnover(annualTurnover);
        setEmployeesCount(employeesCount);
        setType(type);
    }

    public void setAnnualTurnover(Integer annualTurnover) {
        if (annualTurnover == null) {
            throw new IllegalArgumentException("Годовой оборот не может быть null");
        }
        if (annualTurnover <= 0) {
            throw new IllegalArgumentException("Годовой оборот должен быть больше 0");
        }
        this.annualTurnover = annualTurnover;
    }

    public void setEmployeesCount(long employeesCount) {
        if (employeesCount <= 0) {
            throw new IllegalArgumentException("Количество сотрудников должно быть больше 0");
        }
        this.employeesCount = employeesCount;
    }

    public void setType(OrganizationType type) {
        this.type = type; // может быть null
    }

    public Integer getAnnualTurnover() { return annualTurnover; }
    public long getEmployeesCount() { return employeesCount; }
    public OrganizationType getType() { return type; }

    @Override
    public String toString() {
        return String.format("Organization{turnover=%d, employees=%d, type=%s}",
                annualTurnover, employeesCount, type);
    }
}
