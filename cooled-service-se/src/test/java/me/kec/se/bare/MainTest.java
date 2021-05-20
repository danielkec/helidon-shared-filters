
package me.kec.se.bare;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonBuilderFactory;

import io.helidon.media.jsonp.JsonpSupport;
import io.helidon.webclient.WebClient;
import io.helidon.webclient.WebClientResponse;
import io.helidon.webserver.WebServer;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {

    private static WebServer webServer;
    private static WebClient webClient;

    @BeforeAll
    public static void startTheServer() throws Exception {
        webServer = Main.startServer().await();

        webClient = WebClient.builder()
                .baseUri("http://localhost:" + webServer.port())
                .addMediaSupport(JsonpSupport.create())
                .build();
    }

    @AfterAll
    public static void stopServer() throws Exception {
        if (webServer != null) {
            webServer.shutdown()
                    .toCompletableFuture()
                    .get(10, TimeUnit.SECONDS);
        }
    }

    @Test
    public void testCoolingHeader() throws Exception {

        String coolResponseHeader = webClient.get()
                .path("/greet")
                .headers(wch -> {
                    wch.add(CoolingSEFilterService.COOL_HEADER_NAME, "Is the request cool?");
                    return wch;
                })
                .request()
                .peek(r -> System.out.println(r.headers().toMap()))
                .map(WebClientResponse::headers)
                .map(h -> h.value(CoolingSEFilterService.COOL_HEADER_NAME))
                .await(5, TimeUnit.SECONDS)
                .orElseThrow(() -> new AssertionFailedError("Header expected in response", CoolingSEFilterService.COOL_HEADER_NAME, null));

        assertEquals(CoolingSEFilterService.COOLING_VALUE + "Is the request cool?", coolResponseHeader);
    }
}
