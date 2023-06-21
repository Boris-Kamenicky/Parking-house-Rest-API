package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.DiscountCoupon;
import sk.stuba.fei.uim.vsa.pr2.web.response.DiscountCouponDto;

public class DiscountCouponResponseFactory implements ResponseFactory<DiscountCoupon, DiscountCouponDto>{
    @Override
    public DiscountCouponDto transformToDto(DiscountCoupon entity) {
        DiscountCouponDto dto = new DiscountCouponDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDiscount(entity.getDiscount());
        return dto;
    }

    @Override
    public DiscountCoupon transformToEntity(DiscountCouponDto dto) {
        DiscountCoupon coupon = new DiscountCoupon();
        coupon.setId(dto.getId());
        coupon.setDiscount(dto.getDiscount());
        coupon.setName(dto.getName());
        return coupon;
    }
}
