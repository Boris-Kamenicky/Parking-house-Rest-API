package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;

import java.util.stream.Collectors;

public class ReservationResponseFactory implements ResponseFactory<Reservation, ReservationDto>{

    private final CarParkService service = new CarParkService();

    @Override
    public ReservationDto transformToDto(Reservation entity) {
        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId());  //dobrovolne ak nezada tak automaticky vytvori
        dto.setStart(entity.getStartDate());
        dto.setEnd(entity.getFinishDate());
        dto.setPrices(entity.getFinalPrice());
        dto.setCar(entity.getCar().getId());
        dto.setSpot(entity.getParkingSpot().getId());
        dto.setCoupon(entity.getDiscountCoupon()); //dobrovolne
        return dto;
    }

    @Override
    public Reservation transformToEntity(ReservationDto dto) {
        Reservation res = new Reservation();
        //if id lebo dobrovolne
        res.setId(dto.getId()); //tu dobrovolne
        res.setStartDate(dto.getStart());
        res.setFinishDate(dto.getEnd());
        res.setFinalPrice(dto.getPrices());
        res.setCar((Car)service.getCar(dto.getCar()));  //najdi auto
        res.setParkingSpot((ParkingSpot) service.getParkingSpot(dto.getSpot()));
        res.setDiscountCoupon(dto.getCoupon());
        return res;
    }
}
