package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.CarPark;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.domain.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ParkingSpotDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ReservationResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserResponseFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/reservations")
public class ReservationResource {

    public static final String EMPTY_RESPONSE = "{}";
    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final ReservationResponseFactory factory = new ReservationResponseFactory();


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getReservations(@QueryParam("user") Long idUser,@QueryParam("spot") Long spotId,@QueryParam("date") String date) throws ParseException {
        List<ReservationDto> list = new ArrayList<ReservationDto>();

        if (idUser!=null) {
            List<Reservation> spots = (List<Reservation>) (Object) service.getMyReservations(idUser);
            for (Reservation temp : spots) {
                ReservationDto dto = factory.transformToDto(temp);
                list.add(dto);
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

        if (spotId!=null && date!=null) {
            Date date1  =new SimpleDateFormat("yyyy-MM-dd").parse(date);
            List<Reservation> spots = (List<Reservation>) (Object) service.getReservations(spotId,date1);
            for (Reservation temp : spots) {
                ReservationDto dto = factory.transformToDto(temp);
                list.add(dto);
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
        List<Reservation> spots = (List<Reservation>) (Object) service.getAllReservations();
        for (Reservation temp : spots) {
            ReservationDto dto = factory.transformToDto(temp);
            list.add(dto);
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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {

        Reservation res = (Reservation) service.getReservation(id);

        try {
            return json.writeValueAsString(factory.transformToDto(res));
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
    public Response updateReservation(@PathParam("id") Long id, String body) {
        try {
            if (service.getReservation(id)==null)
                return null;
            ReservationDto p = json.readValue(body, ReservationDto.class);
            p.setId(id);
            Reservation p2 = factory.transformToEntity(p);
            p2 = (Reservation) service.updateReservation(p2); //bez DTO

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(factory.transformToDto(p2)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String body) {
        try {

            ReservationDto p = json.readValue(body, ReservationDto.class);
            Reservation res = (Reservation) service.createReservation(p.getSpot(),p.getCar());
            res.setStartDate(p.getStart());
            service.updateReservation(res);

            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(factory.transformToDto(res)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @POST
    @Path("{id}/end")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endReservation(@PathParam("id") Long id) {
        try {

            Reservation res = (Reservation) service.endReservation(id); //tu si

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(factory.transformToDto(res)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

}
