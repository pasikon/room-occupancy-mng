package pl.exercise.coding.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.exercise.coding.api.model.CalculateOccupancyPriceRequestDTO;
import pl.exercise.coding.api.model.CalculateOccupancyPriceResponseDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class OccupancyCalculatorServiceTest {

    @Autowired OccupancyCalculatorService occupancyCalculatorService;

    public static final List<Float> CUSTOMER_DATA = List.of(23f, 45f, 155f, 374f, 22f, 99.99f, 100f, 101f, 115f, 209f);

    @Test
    void test1() {

        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO =
                occupancyCalculatorService.calculateOccupancy(prepareTestData(3, 3));

        assertEquals(738f, calculateOccupancyPriceResponseDTO.getPricePremium());
        assertEquals(167.99f, calculateOccupancyPriceResponseDTO.getPriceEconomy());
        assertEquals(3, calculateOccupancyPriceResponseDTO.getUsagePremium());
        assertEquals(3, calculateOccupancyPriceResponseDTO.getUsageEconomy());

    }

    @Test
    void test2() {

        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO =
                occupancyCalculatorService.calculateOccupancy(prepareTestData(7, 5));

        assertEquals(1054f, calculateOccupancyPriceResponseDTO.getPricePremium());
        assertEquals(189.99f, calculateOccupancyPriceResponseDTO.getPriceEconomy());
        assertEquals(6, calculateOccupancyPriceResponseDTO.getUsagePremium());
        assertEquals(4, calculateOccupancyPriceResponseDTO.getUsageEconomy());

    }

    @Test
    void test3() {

        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO =
                occupancyCalculatorService.calculateOccupancy(prepareTestData(2, 7));

        assertEquals(583f, calculateOccupancyPriceResponseDTO.getPricePremium());
        assertEquals(189.99f, calculateOccupancyPriceResponseDTO.getPriceEconomy());
        assertEquals(2, calculateOccupancyPriceResponseDTO.getUsagePremium());
        assertEquals(4, calculateOccupancyPriceResponseDTO.getUsageEconomy());

    }

    @Test
    void test4() {

        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO =
                occupancyCalculatorService.calculateOccupancy(prepareTestData(7, 1));

        assertEquals(1153.99f, calculateOccupancyPriceResponseDTO.getPricePremium());
        assertEquals(45f, calculateOccupancyPriceResponseDTO.getPriceEconomy());
        assertEquals(7, calculateOccupancyPriceResponseDTO.getUsagePremium());
        assertEquals(1, calculateOccupancyPriceResponseDTO.getUsageEconomy());

    }

    private static CalculateOccupancyPriceRequestDTO prepareTestData(int freePremiumRooms, int freeEconomyRooms) {
        CalculateOccupancyPriceRequestDTO calculateOccupancyPriceRequestDTO = new CalculateOccupancyPriceRequestDTO();
        calculateOccupancyPriceRequestDTO.setFreePremiumRooms(freePremiumRooms);
        calculateOccupancyPriceRequestDTO.setFreeEconomyRooms(freeEconomyRooms);
        calculateOccupancyPriceRequestDTO.setCustomerData(CUSTOMER_DATA);
        return calculateOccupancyPriceRequestDTO;
    }
}