package domain;

import java.util.Objects;

public class UserType {
    private String id;
    private String name;
    private Double discount;
    private Double threshold;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", discount=" + discount +
                ", threshold=" + threshold +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserType userType = (UserType) o;
        return Objects.equals(id, userType.id) &&
                Objects.equals(name, userType.name) &&
                Objects.equals(discount, userType.discount) &&
                Objects.equals(threshold, userType.threshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, discount, threshold);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserType(String id, String name, Double discount, Double threshold) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.threshold = threshold;
    }

    public UserType() {
    }
}
