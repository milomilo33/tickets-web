package domain;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String gender;
    private String birth;
    private UserRole role;
    private Collection<Ticket> tickets;
    private Collection<Manifestation> manifestations;
    private Double points;
    private UserType type;
    private Boolean blocked;
    private Boolean deleted;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", gender='" + gender + '\'' +
                ", birth='" + birth + '\'' +
                ", role=" + role +
                ", tickets=" + tickets +
                ", manifestations=" + manifestations +
                ", points=" + points +
                ", type=" + type +
                ", blocked=" + blocked +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(name, user.name) &&
                Objects.equals(surname, user.surname) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(birth, user.birth) &&
                role == user.role &&
                Objects.equals(tickets, user.tickets) &&
                Objects.equals(manifestations, user.manifestations) &&
                Objects.equals(points, user.points) &&
                Objects.equals(type, user.type) &&
                Objects.equals(blocked, user.blocked) &&
                Objects.equals(deleted, user.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, name, surname, gender, birth, role, tickets, manifestations, points, type, blocked, deleted);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Collection<Manifestation> getManifestations() {
        return manifestations;
    }

    public void setManifestations(Collection<Manifestation> manifestations) {
        this.manifestations = manifestations;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User(UUID id, String username, String password, String name, String surname, String gender, String birth, UserRole role, Collection<Ticket> tickets, Collection<Manifestation> manifestations, Double points, UserType type, Boolean blocked, Boolean deleted) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.birth = birth;
        this.role = role;
        this.tickets = tickets;
        this.manifestations = manifestations;
        this.points = points;
        this.type = type;
        this.blocked = blocked;
        this.deleted = deleted;
    }

    public User() {
    }
}
