package domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private UUID id;
    private String specialId; //10 characters total
    private Manifestation manifestation;
    private LocalDateTime date;
    private Double price;
    private User buyer;
    private TicketStatus status;
    private TicketType type;

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", specialId='" + specialId + '\'' +
                ", manifestation=" + manifestation +
                ", date=" + date +
                ", price=" + price +
                ", buyer=" + buyer +
                ", status=" + status +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) &&
                Objects.equals(specialId, ticket.specialId) &&
                Objects.equals(manifestation, ticket.manifestation) &&
                Objects.equals(date, ticket.date) &&
                Objects.equals(price, ticket.price) &&
                Objects.equals(buyer, ticket.buyer) &&
                status == ticket.status &&
                type == ticket.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, specialId, manifestation, date, price, buyer, status, type);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public Manifestation getManifestation() {
        return manifestation;
    }

    public void setManifestation(Manifestation manifestation) {
        this.manifestation = manifestation;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Ticket(UUID id, String specialId, Manifestation manifestation, LocalDateTime date, Double price, User buyer, TicketStatus status, TicketType type) {
        this.id = id;
        this.specialId = specialId;
        this.manifestation = manifestation;
        this.date = date;
        this.price = price;
        this.buyer = buyer;
        this.status = status;
        this.type = type;
    }

    public Ticket() {
    }
}
