package sk.stuba.fei.uim.vsa.pr2.web.response;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.DiscountCoupon;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.util.List;

public class UserDto extends Dto{

    private String firstName;
    private String lastName;
    private String email;
    private List<CarDto> cars;
    private List<DiscountCouponDto> coupons;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CarDto> getCars() {
        return cars;
    }

    public void setCars(List<CarDto> cars) {
        this.cars = cars;
    }

    public List<DiscountCouponDto> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<DiscountCouponDto> discountCoupons) {
        this.coupons = discountCoupons;
    }
}
