package me.kec.se.bare;

import java.util.Optional;

import io.helidon.webserver.Handler;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

public class CoolingSEFilterService implements Service, Handler {
    
    public static final String COOL_HEADER_NAME = "Cool-Header";
    public static final String COOLING_VALUE = "This is way cooler response than ";
    
    @Override
    public void update(Routing.Rules rules) {
        rules.any(this);
    }

    @Override
    public void accept(ServerRequest req, ServerResponse res) {
        Optional<String> coolReqHeader = req.headers().first(COOL_HEADER_NAME);
        coolReqHeader.ifPresent(s -> res.headers().add(COOL_HEADER_NAME, COOLING_VALUE + s));
        req.next();
    }
}
