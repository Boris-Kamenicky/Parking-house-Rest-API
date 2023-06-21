package sk.stuba.fei.uim.vsa.pr2.web.response;

public class DiscountCouponDto extends Dto{

    private String name;
    private Integer discount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}
