package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.Car;
import sk.stuba.fei.uim.vsa.pr2.domain.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.CarDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.CarResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.UserResponseFactory;

import java.util.ArrayList;
import java.util.List;

@Path("/users")
public class UserResource {

    public static final String EMPTY_RESPONSE = "{}";
    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final UserResponseFactory factory = new UserResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers(@QueryParam("email") String email) {
        List<UserDto> list = new ArrayList<UserDto>();

        if (email!=null) {
            User user = (User) service.getUser(email);
            list.add(factory.transformToDto(user));
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
            List<User> users = (List<User>) (Object) service.getUsers();
            for (User value : users) {
                System.out.println(value);
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
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("id") Long id) {

        User user = (User) service.getUser(id);

        try {
            return json.writeValueAsString(factory.transformToDto(user));
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

            UserDto p = json.readValue(body, UserDto.class);
            User user= (User) service.createUser(p.getFirstName(),p.getLastName(),p.getEmail());

            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(factory.transformToDto(user)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @PUT
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, String body) {
        try {
            if (service.getUser(id)==null)
                return null;
            UserDto p = json.readValue(body, UserDto.class);
            p.setId(id);
            User p2 = factory.transformToEntity(p);
            p2 = (User) service.updateUser(p2); //bez DTO

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(factory.transformToDto(p2)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("{id}")
    public void deleteUser(@PathParam("id") Long id) {
        service.deleteUser(id);
    }
}
