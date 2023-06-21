package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.grizzly.http.util.HttpStatus;
import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.CarPark;
import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.*;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkFloorResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarParkResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ParkingSpotResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ResponseFactory;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/carparks")

public class CarParkResource {

    public static final String EMPTY_RESPONSE = "{}";

    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final CarParkResponseFactory factory = new CarParkResponseFactory();
    private final CarParkFloorResponseFactory factory2 = new CarParkFloorResponseFactory();
    private final ParkingSpotResponseFactory factory3 = new ParkingSpotResponseFactory();

    @HeaderParam(HttpHeaders.AUTHORIZATION) String authorization;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAll(@QueryParam("name") String CarParkName) {

        if (!checkLogin()) {
            return HttpStatus.UNAUTHORIZED_401.getReasonPhrase();
        }
        List<CarParkDto> list = new ArrayList<CarParkDto>();
        if (CarParkName==null) {
            List<CarPark> carparks = (List<CarPark>) (Object) service.getCarParks();
            for (CarPark value : carparks) {
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
        else {
            CarPark cp = (CarPark) service.getCarPark(CarParkName);
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

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {

        CarPark cp = (CarPark) service.getCarPark(id);
        CarParkDto cp2 = factory.transformToDto(cp);

        try {
            return json.writeValueAsString(cp2);
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

            CarPark p = json.readValue(body, CarPark.class);
            p = (CarPark) service.createCarPark(p.getName(),p.getAddress(),p.getPricePerHour()); //bez DTO

            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(p))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, String body) {
        try {
            if (service.getCarPark(id)==null)
                return null;
            CarPark p = json.readValue(body, CarPark.class);
            p.setId(id);
            CarPark p2 = (CarPark) service.updateCarPark(p); //bez DTO

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(p2))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        service.deleteCarPark(id);
    }

    @GET
    @Path("{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByIdFloors(@PathParam("id") Long id) {

        List<CarParkFloor> floors = (List<CarParkFloor>) (Object) service.getCarParkFloors(id);

        try {
            return json.writeValueAsString(floors);
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
    @Path("{id}/floors")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createFloor(@PathParam("id") Long id, String body) {
        try {
            CarParkFloorDto p = json.readValue(body, CarParkFloorDto.class);

            CarParkFloor p2 = (CarParkFloor) service.createCarParkFloor(id,p.getIdentifier());
            if (p.getId()!=null){
                p2.setId(p.getId()); //not working
            }
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(p2))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @PUT
    @Path("{id}/floors/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFloor(@PathParam("id") Long id,@PathParam("identifier") String identifier, String body) {
        try {
            if (service.getCarParkFloor(id,identifier)==null)
                return null;
            CarParkFloorDto p = json.readValue(body, CarParkFloorDto.class);
            p.setId(id);
            CarParkFloor p2 = factory2.transformToEntity(p);
            p2 = (CarParkFloor) service.updateCarParkFloor(p2); //bez DTO

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(p2))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("/{id}/floors/{identifier}")
    public void deleteFloor(@PathParam("id") Long id, @PathParam("identifier") String identifier) {
        service.deleteCarParkFloor(id,identifier);
    }

    @GET
    @Path("/{id}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpots(@QueryParam("free") Boolean free, @PathParam("id") Long id) {
        CarPark cp = (CarPark) service.getCarPark(id);
        List<ParkingSpotDto> list = new ArrayList<>();
        if (free==null) {
            Map<String, List<ParkingSpot>> spots =  service.getParkingSpotsNeww(cp.getId()); //1. moznost
            for (List<ParkingSpot> value : spots.values()) {
                for (ParkingSpot temp : value) {
                    ParkingSpotDto dto = factory3.transformToDto(temp);
                    list.add(dto);
                }
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
        if (free==true) {
            Map<String, List<ParkingSpot>> spots =  service.getAvailableParkingSpotsNew(cp.getName()); //1. moznost
            for (List<ParkingSpot> value : spots.values()) {
                for (ParkingSpot temp : value) {
                    ParkingSpotDto dto = factory3.transformToDto(temp);
                    list.add(dto);
                }
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

        if (free==false) {
            Map<String, List<ParkingSpot>> spots =  service.getOccupiedParkingSpotsNew(cp.getName()); //1. moznost
            for (List<ParkingSpot> value : spots.values()) {
                for (ParkingSpot temp : value) {
                    ParkingSpotDto dto = factory3.transformToDto(temp);
                    list.add(dto);
                }
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
        return EMPTY_RESPONSE;
    }

    @GET
    @Path("{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSpotsByFloor(@PathParam("id") Long id,@PathParam("identifier") String identifier) {
        CarPark cp = (CarPark) service.getCarPark(id);
        List<ParkingSpotDto> list = new ArrayList<>();
        List<ParkingSpot> spots = service.getParkingSpotsUltraNew(id,identifier);
        for (ParkingSpot temp : spots) {
            ParkingSpotDto dto = factory3.transformToDto(temp);
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

    @POST
    @Path("/{id}/floors/{identifier}/spots")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createSpot(@PathParam("id") Long id,@PathParam("identifier") String identifier, String body) {
        try {
            ParkingSpotDto p = json.readValue(body, ParkingSpotDto.class);

            ParkingSpot p2 = (ParkingSpot) service.createParkingSpot(id,identifier,p.getIdentifier());
            //ParkingSpot p3 = (ParkingSpot) service.updateParkingSpot(factory3.transformToEntity(p));
            if (p.getId()!=null){
                p2.setId(p.getId()); //not working
            };
            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(factory3.transformToDto(p2)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }







    /*
    @GET
    @Path("loginCheck")
    @Produces("text/plain")
    public String checkLogin(@HeaderParam(HttpHeaders.AUTHORIZATION) String authorization) {
        if (isAuthenticated(authorization)){
            return "successful login";
        }
        else {
            return "unsuccessful login";
        }
    }*/


    public boolean checkLogin() {
        if (isAuthenticated(this.authorization)){
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isAuthenticated(String auth){
        String decodeString = "";
        String[] authParts = auth.split("\\s+");
        String authInfo = authParts[1];
        byte[] bytes = null;

        try {
            bytes = new BASE64Decoder().decodeBuffer(authInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        decodeString = new String(bytes);
        System.out.println(decodeString);

        String [] details= decodeString.split(":");
        String email = details[0];
        String password = details[1];

        return service.authencitate(email,password);
    }

}
