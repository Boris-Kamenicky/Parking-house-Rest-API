package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.*;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponseFactory implements ResponseFactory<User, UserDto>{
    private final CarParkService service = new CarParkService();

    @Override
    public UserDto transformToDto(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());  //dobrovolne ak nezada tak automaticky vytvori
        dto.setFirstName(entity.getFirstname());
        dto.setLastName(entity.getLastname());
        dto.setEmail(entity.getEmail());

        List<Car> listok = (List<Car>) (Object) service.getCars(entity.getId());
        CarResponseFactory factory = new CarResponseFactory();
        dto.setCars(listok.stream().map(factory::transformToDto).collect(Collectors.toList()));

        List<DiscountCoupon> list = (List<DiscountCoupon>) (Object) service.getCoupons(entity.getId());
        DiscountCouponResponseFactory factory2 = new DiscountCouponResponseFactory();
        dto.setCoupons(list.stream().map(factory2::transformToDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public User transformToEntity(UserDto dto) {
        User user = new User();
        //if id lebo dobrovolne
        user.setId(dto.getId()); //tu dobrovolne
        user.setFirstname(dto.getFirstName());
        user.setLastname(dto.getLastName());
        user.setEmail(dto.getEmail());

        CarResponseFactory factory = new CarResponseFactory();
        List<Car> pubs = dto.getCars().stream().map(factory::transformToEntity).collect(Collectors.toList());
        user.setCars(pubs);

        DiscountCouponResponseFactory factory2 = new DiscountCouponResponseFactory();
        List<DiscountCoupon> pubs2 = dto.getCoupons().stream().map(factory2::transformToEntity).collect(Collectors.toList());
        user.setDiscountCoupons(pubs2);
        return user;
    }
}
