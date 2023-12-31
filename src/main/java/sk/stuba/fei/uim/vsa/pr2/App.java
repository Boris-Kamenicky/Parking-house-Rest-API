package sk.stuba.fei.uim.vsa.pr2;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import sk.stuba.fei.uim.vsa.pr2.domain.CarParkFloor;
import sk.stuba.fei.uim.vsa.pr2.domain.ParkingSpot;
import sk.stuba.fei.uim.vsa.pr2.web.*;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class App extends Application {

    static final Set<Class<?>> appClasses = new HashSet<>();

    static {
        appClasses.add(CarParkResource.class);
        appClasses.add(CarParkFloorResource.class);
        appClasses.add(ParkingSpotResource.class);
        appClasses.add(CarResource.class);
        appClasses.add(UserResource.class);
        appClasses.add(ReservationResource.class);
        appClasses.add(DiscountCouponResource.class);
        appClasses.add(BasicAuthRequestFilter.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return appClasses;
    }
}
