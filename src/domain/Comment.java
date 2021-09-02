package domain;

import java.util.Objects;
import java.util.UUID;

public class Comment {
    private UUID id;
    private User user;
    private Manifestation manifestation;
    private String text;
    private Integer rating;

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", manifestation=" + manifestation +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(user, comment.user) &&
                Objects.equals(manifestation, comment.manifestation) &&
                Objects.equals(text, comment.text) &&
                Objects.equals(rating, comment.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, manifestation, text, rating);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Manifestation getManifestation() {
        return manifestation;
    }

    public void setManifestation(Manifestation manifestation) {
        this.manifestation = manifestation;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Comment(UUID id, User user, Manifestation manifestation, String text, Integer rating) {
        this.id = id;
        this.user = user;
        this.manifestation = manifestation;
        this.text = text;
        this.rating = rating;
    }

    public Comment() {
    }
}
