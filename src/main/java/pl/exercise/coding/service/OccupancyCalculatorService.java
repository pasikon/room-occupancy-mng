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

    static final int PREMIUM_PRICE_THRESHOLD = 100;

    /**
     * Calculate occupancy breakdown for given params.
     *
     * @param requestDTO prices, customers and rooms data
     * @return cost & occupancy breakdown
     */
    public CalculateOccupancyPriceResponseDTO calculateOccupancy(CalculateOccupancyPriceRequestDTO requestDTO) {

        var accEcoPrice = BigDecimal.ZERO;
        var accPremiumPrice = BigDecimal.ZERO;
        var freeEcoRooms = requestDTO.getFreeEconomyRooms();
        var freePremiumRooms = requestDTO.getFreePremiumRooms();
        var roomsLeftToAllocate = requestDTO.getCustomerData().size();

        int availableEconomyRooms = requestDTO.getFreeEconomyRooms();

        List<Float> customerPriceOffers = new ArrayList<>(requestDTO.getCustomerData());
        customerPriceOffers.sort(Comparator.reverseOrder());

        for (Float moneyOffer : customerPriceOffers) {
            if (isAllocateInPremiumRoom(moneyOffer, freePremiumRooms, availableEconomyRooms, roomsLeftToAllocate)) {
                accPremiumPrice = accPremiumPrice.add(applyFloatScaling(moneyOffer));
                freePremiumRooms--;
                roomsLeftToAllocate--;
            } else if (isAllocateInEconomyRoom(moneyOffer, freeEcoRooms)) {
                accEcoPrice = accEcoPrice.add(applyFloatScaling(moneyOffer));
                freeEcoRooms--;
                roomsLeftToAllocate--;
            }
        }

        return buildOutputDTO(requestDTO, accPremiumPrice, accEcoPrice, freeEcoRooms, freePremiumRooms);
    }

    private static CalculateOccupancyPriceResponseDTO buildOutputDTO(CalculateOccupancyPriceRequestDTO requestDTO, BigDecimal accPremiumPrice, BigDecimal accEcoPrice, Integer freeEcoRooms, Integer freePremiumRooms) {
        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO = new CalculateOccupancyPriceResponseDTO();
        calculateOccupancyPriceResponseDTO.setPricePremium(accPremiumPrice.floatValue());
        calculateOccupancyPriceResponseDTO.setPriceEconomy(accEcoPrice.floatValue());
        calculateOccupancyPriceResponseDTO.setUsageEconomy(requestDTO.getFreeEconomyRooms() - freeEcoRooms);
        calculateOccupancyPriceResponseDTO.setUsagePremium(requestDTO.getFreePremiumRooms() - freePremiumRooms);
        return calculateOccupancyPriceResponseDTO;
    }

    private static boolean isAllocateInPremiumRoom(Float moneyOffer, Integer freePremiumRooms, int availableEconomyRooms, int roomsLeftToAllocate) {
        return isPutInPremiumRoom(moneyOffer, freePremiumRooms) || isUpgradeToPremium(availableEconomyRooms, freePremiumRooms, roomsLeftToAllocate);
    }

    private static boolean isAllocateInEconomyRoom(float moneyOffer, int ecoRooms) {
        return ecoRooms > 0 && moneyOffer < PREMIUM_PRICE_THRESHOLD;
    }

    private static boolean isUpgradeToPremium(int freeEconomy, int freePremium, int leftToAllocate) {
        return freePremium > 0 && leftToAllocate > freeEconomy;
    }

    private static boolean isPutInPremiumRoom(float moneyOffer, int premiumRooms) {
        return premiumRooms > 0 && moneyOffer >= PREMIUM_PRICE_THRESHOLD;
    }

    private static BigDecimal applyFloatScaling(Float moneyOffer) {
        return new BigDecimal(moneyOffer).setScale(2, RoundingMode.UP);
    }

}
