package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.CarPark;
import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;

import java.util.List;
import java.util.stream.Collectors;

public class ParkingSpotResponseFactory implements ResponseFactory<ParkingSpot, ParkingSpotDto> {

    private final CarParkService service = new CarParkService();

    @Override
    public ParkingSpotDto transformToDto(ParkingSpot entity) {
        ParkingSpotDto dto = new ParkingSpotDto();
        dto.setId(entity.getId());  //dobrovolne ak nezada tak automaticky vytvori
        dto.setCarParkFloor(entity.getFloorIdentifier());
        dto.setIdentifier(entity.getSpotIdentifier());
        dto.setCarPark(entity.getCarpark().getId());
        dto.setFree(entity.getCar()==null);
        List<Reservation> list = (List<Reservation>) (Object) service.getReservationsForFloor(entity.getId());
        ReservationResponseFactory factory = new ReservationResponseFactory();
        dto.setReservations(list.stream().map(factory::transformToDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public ParkingSpot transformToEntity(ParkingSpotDto dto) {
        ParkingSpot spot = new ParkingSpot();
        //if id lebo dobrovolne
        CarParkFloor floor = (CarParkFloor) service.getCarParkFloor(dto.getCarPark(), dto.getCarParkFloor());
        spot.setCarparkfloor(floor);   //mozno floor DTO ?
        spot.setId(dto.getId()); //tu dobrovolne
        spot.setFloorIdentifier(dto.getCarParkFloor());
        spot.setSpotIdentifier(dto.getIdentifier());
        spot.setCarpark((CarPark) service.getCarPark(dto.getCarPark())); //dopici
        //spot.setCar(dto.getFree()); to sa neda nastavit
        ReservationResponseFactory factory = new ReservationResponseFactory();
        List<Reservation> pubs = dto.getReservations().stream().map(factory::transformToEntity).collect(Collectors.toList());
        spot.setReservations(pubs);
        return spot;
    }
}
