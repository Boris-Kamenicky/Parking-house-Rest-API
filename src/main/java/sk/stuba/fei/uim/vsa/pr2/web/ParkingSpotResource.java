package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarParkFloorDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotResponseFactory;

@Path("/parkingspots")
public class ParkingSpotResource {

    public static final String EMPTY_RESPONSE = "{}";

    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final ParkingSpotResponseFactory factory = new ParkingSpotResponseFactory();

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {

        ParkingSpot spot = (ParkingSpot) service.getParkingSpot(id);
        try {
            return json.writeValueAsString(factory.transformToDto(spot));
        } catch (JsonProcessingException e) {
            try {
                return json.writeValueAsString(MessageDto.buildError(e.getMessage()));
            } catch (JsonProcessingException ex) {
                // ignore
                return EMPTY_RESPONSE;
            }
        }
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSpot(@PathParam("id") Long id, String body) {
        try {
            if (service.getParkingSpot(id)==null)
                return null;
            ParkingSpotDto p = json.readValue(body, ParkingSpotDto.class);
            p.setId(id);
            ParkingSpot p2 = factory.transformToEntity(p);
            p2 = (ParkingSpot) service.updateParkingSpot(p2); //bez DTO

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
    public void deleteSpot(@PathParam("id") Long id) {
        service.deleteParkingSpot(id);
    }
}
