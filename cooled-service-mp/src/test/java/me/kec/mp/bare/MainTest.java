
package me.kec.mp.bare;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import io.helidon.common.reactive.Single;
import io.helidon.microprofile.tests.junit5.HelidonTest;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.kec.se.bare.CoolingSEFilterService;

@HelidonTest
class MainTest {

    @Test
    @SuppressWarnings("unchecked")
    void testCoolingService(WebTarget client) {

        String coolResponseHeader = Single.create((CompletionStage<Response>) client
                .path("/greet")
                .request()
                .header(CoolingSEFilterService.COOL_HEADER_NAME, "Is the request cool?")
                .async()
                .get())
                .map(h -> Optional.ofNullable(h.getHeaderString(CoolingSEFilterService.COOL_HEADER_NAME)))
                .await(5, TimeUnit.SECONDS)
                .orElseThrow(() -> new AssertionFailedError("Header expected in response", CoolingSEFilterService.COOL_HEADER_NAME, null));

        assertEquals(CoolingSEFilterService.COOLING_VALUE + "Is the request cool?", coolResponseHeader);
    }

}
