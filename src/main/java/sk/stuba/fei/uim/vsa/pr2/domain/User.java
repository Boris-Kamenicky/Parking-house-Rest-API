package sk.stuba.fei.uim.vsa.pr2.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USER",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"FIRSTNAME", "LASTNAME"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true,nullable = false)
    private String email;

    @OneToMany
    private List<Car> cars;

    @OneToMany
    private List<DiscountCoupon> discountCoupons;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DiscountCoupon> getDiscountCoupons() {
        return discountCoupons;
    }

    public void setDiscountCoupons(List<DiscountCoupon> discountCoupons) {
        this.discountCoupons = discountCoupons;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    @Override
    public String toString() {
        return "Name{" + "id=" + id + ", firstName=" + firstname + ", lastName=" + lastname + ", email=" + email +  '}' + "\n";
    }
}