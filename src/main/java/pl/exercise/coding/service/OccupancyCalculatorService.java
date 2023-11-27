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

        List<Float> customerPriceOffers = new ArrayList<>(requestDTO.getCustomerData());
        customerPriceOffers.sort(Comparator.reverseOrder());

        for (Float moneyOffer : customerPriceOffers) {
            if (isAllocateInPremiumRoom(moneyOffer, premiumRooms) || isUpgradeToPremium(requestDTO.getFreeEconomyRooms(), premiumRooms, leftToAllocate)) {
                accPremium = accPremium.add(applyFloatScaling(moneyOffer));
                premiumRooms--;
                leftToAllocate--;
            } else if (isAllocateInEconomyRoom(moneyOffer, ecoRooms)) {
                accEco = accEco.add(applyFloatScaling(moneyOffer));
                ecoRooms--;
                leftToAllocate--;
            }
        }


        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO = new CalculateOccupancyPriceResponseDTO();
        calculateOccupancyPriceResponseDTO.setPricePremium(accPremium.floatValue());
        calculateOccupancyPriceResponseDTO.setPriceEconomy(accEco.floatValue());
        calculateOccupancyPriceResponseDTO.setUsageEconomy(requestDTO.getFreeEconomyRooms() - ecoRooms);
        calculateOccupancyPriceResponseDTO.setUsagePremium(requestDTO.getFreePremiumRooms() - premiumRooms);
        return calculateOccupancyPriceResponseDTO;
    }

    private static boolean isAllocateInEconomyRoom(float moneyOffer, int ecoRooms) {
        return ecoRooms > 0 && moneyOffer < PREMIUM_PRICE_THRESHOLD;
    }

    private static boolean isUpgradeToPremium(int freeEconomy, int freePremium, int leftToAllocate) {
        return freePremium > 0 && leftToAllocate > freeEconomy;
    }

    private static boolean isAllocateInPremiumRoom(float moneyOffer, int premiumRooms) {
        return premiumRooms > 0 && moneyOffer >= PREMIUM_PRICE_THRESHOLD;
    }

    private static BigDecimal applyFloatScaling(Float moneyOffer) {
        return new BigDecimal(moneyOffer).setScale(2, RoundingMode.UP);
    }

}
