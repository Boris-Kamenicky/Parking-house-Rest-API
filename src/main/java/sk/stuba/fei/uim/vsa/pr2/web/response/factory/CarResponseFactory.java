package sk.stuba.fei.uim.vsa.pr2.web.response.factory;

import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkDto;

import java.util.List;
import java.util.stream.Collectors;

public class CarResponseFactory implements ResponseFactory<Car, CarDto>{
    private final CarParkService service = new CarParkService();
    @Override
    public CarDto transformToDto(Car entity) {
        CarDto dto = new CarDto();
        dto.setId(entity.getId());
        dto.setBrand(entity.getBrand());
        dto.setModel(entity.getModel());
        dto.setVrp(entity.getVehicleRegistrationPlate());
        dto.setColour(entity.getColour());
        dto.setOwner(entity.getUser());
        List<Reservation> list = (List<Reservation>) (Object) service.getReservationsForCar(entity.getId());
        ReservationResponseFactory factory = new ReservationResponseFactory();
        dto.setReservations(list.stream().map(factory::transformToDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public Car transformToEntity(CarDto dto) {
        Car car = new Car();
        car.setId(dto.getId());
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setVehicleRegistrationPlate(dto.getVrp());
        car.setColour(dto.getColour());
        car.setUser(dto.getOwner());
        ReservationResponseFactory factory = new ReservationResponseFactory();
        List<Reservation> pubs = dto.getReservations().stream().map(factory::transformToEntity).collect(Collectors.toList());
        car.setReservations(pubs);
        return car;
    }
}
