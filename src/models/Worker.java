package models;

import models.enums.Position;
import models.enums.Status;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.LocalDate;


public class Worker implements Comparable<Worker>, Serializable {  // ДОБАВИТЬ Serializable
    private static final long serialVersionUID = 1L;

    private Integer id;                 // не null, >0, уникальный, авто
    private String name;                 // не null, не пустой
    private Coordinates coordinates;     // не null
    private ZonedDateTime creationDate;  // не null, авто
    private float salary;                // >0
    private LocalDate endDate;           // может быть null
    private Position position;           // может быть null
    private Status status;               // не null
    private Organization organization;    // может быть null


    public Worker(Integer id, String name, Coordinates coordinates, float salary,
                  LocalDate endDate, Position position, Status status, Organization organization) {
        // Для новых работников id может быть null
        if (id != null) {
            setId(id);
        }
        setName(name);
        setCoordinates(coordinates);
        this.creationDate = ZonedDateTime.now(); // авто
        setSalary(salary);
        setEndDate(endDate);
        setPosition(position);
        setStatus(status);
        setOrganization(organization);
    }

    public void setId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID не может быть null");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("ID должен быть больше 0");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть null или пустым");
        }
        this.name = name.trim();
    }

    public void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new IllegalArgumentException("Координаты не могут быть null");
        }
        this.coordinates = coordinates;
    }

    public void setSalary(float salary) {
        if (salary <= 0) {
            throw new IllegalArgumentException("Зарплата должна быть больше 0");
        }
        this.salary = salary;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate; // может быть null
    }

    public void setPosition(Position position) {
        this.position = position; // может быть null
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Статус не может быть null");
        }
        this.status = status;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization; // может быть null
    }

    // Геттеры
    public Integer getId() { return id; }
    public String getName() { return name; }
    public Coordinates getCoordinates() { return coordinates; }
    public ZonedDateTime getCreationDate() { return creationDate; }
    public float getSalary() { return salary; }
    public LocalDate getEndDate() { return endDate; }
    public Position getPosition() { return position; }
    public Status getStatus() { return status; }
    public Organization getOrganization() { return organization; }

    @Override
    public int compareTo(Worker other) {
        if (other == null) return 1;
        if (this.status == null && other.status == null) return 0;
        if (this.status == null) return -1;
        if (other.status == null) return 1;
        return this.status.compareTo(other.status);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.US, "Worker{id=%d, name='%s', salary=%.2f, status=%s}",
                id, name, salary, status);
    }
}
