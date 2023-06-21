package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.CarPark;
import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFloorResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkResponseFactory;

import java.util.List;

@Path("/carparkfloors")
public class CarParkFloorResource {

    public static final String EMPTY_RESPONSE = "{}";

    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarParkFloorResponseFactory factory = new CarParkFloorResponseFactory();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {

        CarParkFloor cp = (CarParkFloor) service.getCarParkFloor(id);

        try {
            return json.writeValueAsString(factory.transformToDto(cp));
        } catch (JsonProcessingException e) {
            try {
                return json.writeValueAsString(MessageDto.buildError(e.getMessage()));
            } catch (JsonProcessingException ex) {
                // ignore
                return EMPTY_RESPONSE;
            }
        }
    }


}
