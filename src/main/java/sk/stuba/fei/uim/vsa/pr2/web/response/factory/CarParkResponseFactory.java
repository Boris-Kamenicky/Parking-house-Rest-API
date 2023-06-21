package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.CarPark;
import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkDto;

import java.util.List;
import java.util.stream.Collectors;

public class CarParkResponseFactory implements ResponseFactory<CarPark, CarParkDto>{
    @Override
    public CarParkDto transformToDto(CarPark entity) {
        CarParkDto dto = new CarParkDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setPrices(entity.getPricePerHour());
        CarParkFloorResponseFactory factory = new CarParkFloorResponseFactory();
        dto.setFloors(entity.getCarParkFloors().stream().map(factory::transformToDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public CarPark transformToEntity(CarParkDto dto) {
        CarPark carpark = new CarPark();
        //carpark.setId(dto.getId());
        carpark.setName(dto.getName());
        carpark.setAddress(dto.getAddress());
        carpark.setPricePerHour(dto.getPrices());
        CarParkFloorResponseFactory factory = new CarParkFloorResponseFactory();
        List<CarParkFloor> pubs = dto.getFloors().stream().map(factory::transformToEntity).collect(Collectors.toList());
        carpark.setCarParkFloors(pubs);
        return carpark;
    }
}
