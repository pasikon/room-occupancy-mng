package pl.exercise.coding.service;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import pl.exercise.coding.api.model.CalculateOccupancyPriceRequestDTO;
import pl.exercise.coding.api.model.CalculateOccupancyPriceResponseDTO;
import pl.exercise.coding.rest.OccupancyRestFacade;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.exercise.coding.service.OccupancyCalculatorServiceTest.CUSTOMER_DATA;

@SpringBootTest
public class OccupancyRestFacadeTest {

    @Autowired
    OccupancyRestFacade occupancyRestFacade;

    @Test
    void test1RestFacade() {
        CalculateOccupancyPriceRequestDTO calculateOccupancyPriceRequestDTO = new CalculateOccupancyPriceRequestDTO(3, 3, CUSTOMER_DATA);
        ResponseEntity<CalculateOccupancyPriceResponseDTO> response = occupancyRestFacade.occupancyPriceGet(calculateOccupancyPriceRequestDTO);
        assertEquals(200, response.getStatusCode().value(), "Response is HTTP 200");

        assertEquals(738f, Objects.requireNonNull(response.getBody()).getPricePremium());
        assertEquals(167.99f, Objects.requireNonNull(response.getBody()).getPriceEconomy());
        assertEquals(3, Objects.requireNonNull(response.getBody()).getUsagePremium());
        assertEquals(3, Objects.requireNonNull(response.getBody()).getUsageEconomy());
    }

    @Test
    void testRestFacadeValidationFreeEconomyRooms() {
        CalculateOccupancyPriceRequestDTO req = new CalculateOccupancyPriceRequestDTO(-1, 3, CUSTOMER_DATA);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> occupancyRestFacade.occupancyPriceGet(req));
        assertEquals("occupancyPriceGet.calculateOccupancyPriceRequestDTO.freeEconomyRooms: must be greater than or equal to 0", exception.getMessage());
    }

    @Test
    void testRestFacadeValidationFreePremiumRooms() {
        CalculateOccupancyPriceRequestDTO req = new CalculateOccupancyPriceRequestDTO(3, -1, CUSTOMER_DATA);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> occupancyRestFacade.occupancyPriceGet(req));
        assertEquals("occupancyPriceGet.calculateOccupancyPriceRequestDTO.freePremiumRooms: must be greater than or equal to 0", exception.getMessage());
    }

    @Test
    void testRestFacadeValidationFreeEconomyRoomsNull() {
        CalculateOccupancyPriceRequestDTO req = new CalculateOccupancyPriceRequestDTO(null, 3, CUSTOMER_DATA);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> occupancyRestFacade.occupancyPriceGet(req));
        assertEquals("occupancyPriceGet.calculateOccupancyPriceRequestDTO.freeEconomyRooms: must not be null", exception.getMessage());
    }

    @Test
    void testRestFacadeValidationFreePremiumRoomsNull() {
        CalculateOccupancyPriceRequestDTO req = new CalculateOccupancyPriceRequestDTO(3, null, CUSTOMER_DATA);
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> occupancyRestFacade.occupancyPriceGet(req));
        assertEquals("occupancyPriceGet.calculateOccupancyPriceRequestDTO.freePremiumRooms: must not be null", exception.getMessage());
    }

}
