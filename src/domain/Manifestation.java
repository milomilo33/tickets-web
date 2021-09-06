package domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Manifestation {
    private UUID id;
    private String name;
    private String type;
    private String capacity;
    private LocalDateTime date;
    private Double ticketPrice;
    private Boolean active;
    private Location location;
    private String picture;
    private Boolean deleted;
    private Double rating;

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Manifestation() {
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getLocationAddr(){
        return location.getAddress();
    }

    @Override
    public String toString() {
        return "Manifestation{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", capacity='" + capacity + '\'' +
                ", date=" + date +
                ", ticketPrice=" + ticketPrice +
                ", active=" + active +
                ", location=" + location +
                ", picture='" + picture + '\'' +
                ", deleted=" + deleted +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manifestation that = (Manifestation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(capacity, that.capacity) &&
                Objects.equals(date, that.date) &&
                Objects.equals(ticketPrice, that.ticketPrice) &&
                Objects.equals(active, that.active) &&
                Objects.equals(location, that.location) &&
                Objects.equals(picture, that.picture) &&
                Objects.equals(deleted, that.deleted) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, capacity, date, ticketPrice, active, location, picture, deleted,rating);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Manifestation(UUID id, String name, String type, String capacity, LocalDateTime date, Double ticketPrice, Boolean active, Location location, String picture, Boolean deleted, Double rating) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.date = date;
        this.ticketPrice = ticketPrice;
        this.active = active;
        this.location = location;
        this.picture = picture;
        this.deleted = deleted;
        this.rating = rating;
    }
}
