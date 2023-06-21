package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkFloorDto;

import java.util.List;
import java.util.stream.Collectors;

public class CarParkFloorResponseFactory implements ResponseFactory<CarParkFloor, CarParkFloorDto>{

    private final CarParkService service = new CarParkService();

    @Override
    public CarParkFloorDto transformToDto(CarParkFloor entity) {
        CarParkFloorDto dto = new CarParkFloorDto();
        dto.setId(entity.getId());
        dto.setIdentifier(entity.getFloorIdentifier());
        dto.setCarPark(entity.getCarParkId());
        //mozno stacia len idecka
        List<ParkingSpot> listok = (List<ParkingSpot>) (Object) service.getParkingSpots(entity.getCarParkId(),entity.getFloorIdentifier());
        ParkingSpotResponseFactory factory = new ParkingSpotResponseFactory();
        dto.setSpots(listok.stream().map(factory::transformToDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public CarParkFloor transformToEntity(CarParkFloorDto dto) {
        CarParkFloor carparkfloor = new CarParkFloor();
        //if id lebo dobrovolne
        carparkfloor.setId(dto.getId());
        carparkfloor.setFloorIdentifier(dto.getIdentifier());
        carparkfloor.setCarParkId(dto.getCarPark());
        ParkingSpotResponseFactory factory = new ParkingSpotResponseFactory();
        List<ParkingSpot> pubs = dto.getSpots().stream().map(factory::transformToEntity).collect(Collectors.toList());
        carparkfloor.setParkingSpots(pubs);
        return carparkfloor;
    }
}
