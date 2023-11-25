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

    public CalculateOccupancyPriceResponseDTO calculateOccupancy(CalculateOccupancyPriceRequestDTO requestDTO) {

        var accEco = BigDecimal.ZERO;
        var accPremium = BigDecimal.ZERO;
        var ecoRooms = requestDTO.getFreeEconomyRooms();
        var premiumRooms = requestDTO.getFreePremiumRooms();

        List<Float> toSort = new ArrayList<>(requestDTO.getCustomerData());
        toSort.sort(Comparator.reverseOrder());

        for (Float moneyOffer : toSort) {
            if (premiumRooms > 0 && moneyOffer >= 100) {
                accPremium = accPremium.add(new BigDecimal(moneyOffer).setScale(2, RoundingMode.UP));
                premiumRooms = premiumRooms - 1;
            } else if (ecoRooms > 0 && moneyOffer < 100) {
                accEco = accEco.add(new BigDecimal(moneyOffer).setScale(2, RoundingMode.UP));
                ecoRooms =  ecoRooms - 1;
            }
        }


        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO = new CalculateOccupancyPriceResponseDTO();
        calculateOccupancyPriceResponseDTO.setPricePremium(accPremium.floatValue());
        calculateOccupancyPriceResponseDTO.setPriceEconomy(accEco.floatValue());
        calculateOccupancyPriceResponseDTO.setUsageEconomy(requestDTO.getFreeEconomyRooms() - ecoRooms);
        calculateOccupancyPriceResponseDTO.setUsagePremium(requestDTO.getFreePremiumRooms() - premiumRooms);
        return calculateOccupancyPriceResponseDTO;
    }

}
