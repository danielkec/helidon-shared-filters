package me.kec.mp.bare;

import javax.enterprise.context.ApplicationScoped;

import io.helidon.microprofile.server.RoutingName;
import io.helidon.webserver.Routing;
import io.helidon.webserver.Service;

import me.kec.se.bare.CoolingSEFilterService;

@ApplicationScoped
@RoutingName(value = "@default")
public class CoolingMpService implements Service {

    private final CoolingSEFilterService coolingSEFilterService;

    public CoolingMpService() {
        // Wrapping SE service as it is missing needed annotations
        coolingSEFilterService = new CoolingSEFilterService();
    }

    @Override
    public void update(Routing.Rules rules) {
        // Delegate routing the wrapped service
        coolingSEFilterService.update(rules);
    }
}
