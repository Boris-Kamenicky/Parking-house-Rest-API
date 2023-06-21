package sk.stuba.fei.uim.vsa.pr2.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sk.stuba.fei.uim.vsa.pr2.domain.DiscountCoupon;
import sk.stuba.fei.uim.vsa.pr2.domain.Reservation;
import sk.stuba.fei.uim.vsa.pr2.domain.User;
import sk.stuba.fei.uim.vsa.pr2.service.CarParkService;
import sk.stuba.fei.uim.vsa.pr2.web.response.DiscountCouponDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.MessageDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.ReservationDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.UserDto;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.DiscountCouponResponseFactory;
import sk.stuba.fei.uim.vsa.pr2.web.response.factory.ReservationResponseFactory;

import java.util.ArrayList;
import java.util.List;

@Path("/coupons")
public class DiscountCouponResource {

    public static final String EMPTY_RESPONSE = "{}";
    private final CarParkService service = new CarParkService();
    private final ObjectMapper json = new ObjectMapper();
    private final DiscountCouponResponseFactory factory = new DiscountCouponResponseFactory();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getUsers(@QueryParam("user") Long userId) {
        List<DiscountCouponDto> list = new ArrayList<DiscountCouponDto>();

        if (userId!=null) {
            List<DiscountCoupon> coupons = (List<DiscountCoupon>) (Object) service.getCoupons(userId);
            for (DiscountCoupon value : coupons) {
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
            List<DiscountCoupon> coupons = (List<DiscountCoupon>) (Object) service.getCouponsAll();
            for (DiscountCoupon value : coupons) {
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

        DiscountCoupon coupon = (DiscountCoupon) service.getCoupon(id);

        try {
            return json.writeValueAsString(factory.transformToDto(coupon));
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

            DiscountCouponDto p = json.readValue(body, DiscountCouponDto.class);
            DiscountCoupon coupon = (DiscountCoupon) service.createDiscountCoupon(p.getName(),p.getDiscount());

            return Response
                    .status(Response.Status.CREATED)
                    .entity(json.writeValueAsString(factory.transformToDto(coupon)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @POST
    @Path("{id}/give/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endReservation(@PathParam("id") Long couponId, @PathParam("userId") Long userId) {
        try {

            service.giveCouponToUser(couponId,userId);
            DiscountCoupon coupon = (DiscountCoupon) service.getCoupon(couponId);

            return Response
                    .status(Response.Status.OK)
                    .entity(json.writeValueAsString(factory.transformToDto(coupon)))
                    .build();
        } catch (JsonProcessingException e) {
            return Response.noContent().build();
        }
    }

    @DELETE
    @Path("{id}")
    public void deleteSpot(@PathParam("id") Long id) {
        service.deleteCoupon(id);
    }
}
