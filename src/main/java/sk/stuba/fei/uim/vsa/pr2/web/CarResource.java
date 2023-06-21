package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.CarPark;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotResponseFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/cars")
public class CarResource {

    public static final String EMPTY_RESPONSE = "{}";
    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarResponseFactory factory = new CarResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCars(@QueryParam("user") Long userId, @QueryParam("vrp") String vrp) {
        List<CarDto> list = new ArrayList<CarDto>();
        //dorobit ak nedam parameter ale v zadani to neni napisane :/
        if (userId!=null) {
            List<Car> cars = service.getCarsList(userId);
            for (Car value : cars) {
                list.add(factory.transformToDto(value));
            }

            try {
                return json.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                try {
                    return json.writeValueAsString(MessageDto.buildError(e.getMessage()));
                } catch (JsonProcessingException ex) {
                    // ignore
                    return EMPTY_RESPONSE;
                }
            }
        }

        if (vrp!=null) {
            Car car = (Car) service.getCar(vrp);
            list.add(factory.transformToDto(car));

            try {
                return json.writeValueAsString(list);
            } catch (JsonProcessingException e) {
                try {
                    return json.writeValueAsString(MessageDto.buildError(e.getMessage()));
                } catch (JsonProcessingException ex) {
                    // ignore
                    return EMPTY_RESPONSE;
                }
            }
        }
        return EMPTY_RESPONSE;

    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {

        Car car = (Car) service.getCar(id);

        try {
            return json.writeValueAsString(factory.transformToDto(car));
        } catch (JsonProcessingException e) {
            try {
                return json.writeValueAsString(MessageDto.buildError(e.getMessage()));
            } catch (JsonProcessingException ex) {
                // ignore
                return EMPTY_RESPONSE;
            }
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String body) {
        try {

            CarDto p = json.readValue(body, CarDto.class); //bez DTO
            Car car= (Car) service.createCar(p.getOwner().getId(),p.getBrand(),p.getModel(),p.getColour(),p.getVrp());

            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(factory.transformToDto(car)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCar(@PathParam("id") Long id, String body) {
        try {
            if (service.getCar(id)==null)
                return null;
            CarDto p = json.readValue(body, CarDto.class);
            p.setId(id);
            Car p2 = factory.transformToEntity(p);
            p2 = (Car) service.updateCar(p2); //bez DTO

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(p))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("{id}")
    public void deleteCar(@PathParam("id") Long id) {
        service.deleteCar(id);
    }
}
