package pl.exercise.coding.service;


import org.springframework.stereotype.Service;
import pl.exercise.coding.api.model.CalculateOccupancyPriceRequestDTO;
import pl.exercise.coding.api.model.CalculateOccupancyPriceResponseDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OccupancyCalculatorService {

    public static final int PREMIUM_PRICE_THRESHOLD = 100;

    /**
     * Calculate occupancy breakdown for given params
     *
     * @param requestDTO
     *  prices, customers and rooms data
     *
     * @return
     *  cost & occupancy breakdown
     */
    public CalculateOccupancyPriceResponseDTO calculateOccupancy(CalculateOccupancyPriceRequestDTO requestDTO) {

        var accEco = BigDecimal.ZERO;
        var accPremium = BigDecimal.ZERO;
        var ecoRooms = requestDTO.getFreeEconomyRooms();
        var premiumRooms = requestDTO.getFreePremiumRooms();

        var leftToAllocate = requestDTO.getCustomerData().size();

        List<Float> customerPriceOffer = new ArrayList<>(requestDTO.getCustomerData());
        customerPriceOffer.sort(Comparator.reverseOrder());

        for (Float moneyOffer : customerPriceOffer) {
            if (premiumRooms > 0 && moneyOffer >= PREMIUM_PRICE_THRESHOLD) {
                accPremium = accPremium.add(applyFloatScaling(moneyOffer));
                premiumRooms = premiumRooms - 1;
                leftToAllocate = leftToAllocate - 1;
            } else if (premiumRooms > 0 && leftToAllocate > requestDTO.getFreeEconomyRooms()) {
                accPremium = accPremium.add(applyFloatScaling(moneyOffer));
                premiumRooms = premiumRooms - 1;
                leftToAllocate = leftToAllocate - 1;
            } else if (ecoRooms > 0 && moneyOffer < PREMIUM_PRICE_THRESHOLD) {
                accEco = accEco.add(applyFloatScaling(moneyOffer));
                ecoRooms =  ecoRooms - 1;
                leftToAllocate = leftToAllocate - 1;
            }
        }


        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO = new CalculateOccupancyPriceResponseDTO();
        calculateOccupancyPriceResponseDTO.setPricePremium(accPremium.floatValue());
        calculateOccupancyPriceResponseDTO.setPriceEconomy(accEco.floatValue());
        calculateOccupancyPriceResponseDTO.setUsageEconomy(requestDTO.getFreeEconomyRooms() - ecoRooms);
        calculateOccupancyPriceResponseDTO.setUsagePremium(requestDTO.getFreePremiumRooms() - premiumRooms);
        return calculateOccupancyPriceResponseDTO;
    }

    private static BigDecimal applyFloatScaling(Float moneyOffer) {
        return new BigDecimal(moneyOffer).setScale(2, RoundingMode.UP);
    }

}
