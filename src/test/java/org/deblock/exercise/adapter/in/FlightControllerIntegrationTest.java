package org.deblock.exercise.adapter.in;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableWireMock({
        @ConfigureWireMock(name = "crazy-air-mock", port = 8081),
        @ConfigureWireMock(name = "tough-air-mock", port = 8082)
})
class FlightControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    private String BASE_URL;

    @InjectWireMock("crazy-air-mock")
    WireMockServer crazyAirMockServer;
    @InjectWireMock("tough-air-mock")
    WireMockServer toughJetMockServer;

    @BeforeEach
    void setUp() {
        BASE_URL = "http://localhost:" + port + "/flights/search";
    }

    @Test
    void shouldAggregateFlightsFromBothSuppliers() {
        crazyAirMockServer.stubFor(post(urlEqualTo("/crazyair/flights"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                   {
                                     "airline": "CrazyAir",
                                     "price": 123.45,
                                     "cabinclass": "E",
                                     "departureAirportCode": "LHR",
                                     "destinationAirportCode": "AMS",
                                     "departureDate": "2025-05-01T10:00:00Z",
                                     "arrivalDate": "2025-05-01T12:00:00Z"
                                   }
                                ]
                                """)));
        toughJetMockServer.stubFor(post(urlEqualTo("/toughjet/flights"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                  {
                                    "carrier": "ToughJet",
                                    "basePrice": 100.00,
                                    "tax": 20.00,
                                    "discount": 5.00,
                                    "departureAirportName": "LHR",
                                    "arrivalAirportName": "AMS",
                                    "outboundDateTime": "2025-05-01T10:00:00Z",
                                    "inboundDateTime": "2025-05-01T12:00:00Z"
                                  }
                                ]
                                """)));

        final String url = BASE_URL + "?origin=LHR&destination=AMS&departureDate=2025-05-01&returnDate=2025-05-02&numberOfPassengers=1";
        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).contains("\"airline\":\"CrazyAir\"");
        assertThat(response.getBody()).contains("\"airline\":\"ToughJet\"");
    }

    @Test
    void shouldReturnEmptyListWhenNoFlights() {
        crazyAirMockServer.stubFor(post(urlEqualTo("/crazyair/flights"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("[]")));
        toughJetMockServer.stubFor(post(urlEqualTo("/toughjet/flights"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withBody("[]")));

        final String url = BASE_URL + "?origin=LHR&destination=AMS&departureDate=2025-05-01&returnDate=2025-05-02&numberOfPassengers=1";
        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo("[]");
    }

    @Test
    void shouldReturnBadRequestForInvalidOrigin() {
        final String url = BASE_URL + "?origin=L&destination=AMS&departureDate=2025-05-01&returnDate=2025-05-02&numberOfPassengers=1";
        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void shouldReturnBadRequestForInvalidNumberOfPassengers() {
        final String url = BASE_URL + "?origin=LHR&destination=AMS&departureDate=2025-05-01&returnDate=2025-05-02&numberOfPassengers=0";
        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    void shouldReturnReturnEmptyListErrorOnSupplierError() {
        crazyAirMockServer.stubFor(post(urlEqualTo("/crazyair/flights"))
                .willReturn(aResponse().withStatus(500)));
        toughJetMockServer.stubFor(post(urlEqualTo("/toughjet/flights"))
                .willReturn(aResponse().withStatus(500)));

        final String url = BASE_URL + "?origin=LHR&destination=AMS&departureDate=2025-05-01&returnDate=2025-05-02&numberOfPassengers=1";
        final ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isEqualTo("[]");
    }
}
