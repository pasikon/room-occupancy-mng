package pl.exercise.coding.service;


import org.springframework.stereotype.Service;
import pl.exercise.coding.api.model.CalculateOccupancyPriceRequestDTO;
import pl.exercise.coding.api.model.CalculateOccupancyPriceResponseDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OccupancyCalculatorService {

    public CalculateOccupancyPriceResponseDTO calculateOccupancy(CalculateOccupancyPriceRequestDTO requestDTO) {

        var acc = 0f;
        var ecoRooms = requestDTO.getFreeEconomyRooms();
        var premiumRooms = requestDTO.getFreePremiumRooms();

        List<Float> toSort = new ArrayList<>(requestDTO.getCustomerData());
        toSort.sort(Comparator.reverseOrder());

        for (Float moneyOffer : toSort) {
            if (premiumRooms > 0) {
                acc = acc + moneyOffer;
                premiumRooms = premiumRooms- 1;
            } else if (ecoRooms > 0) {
                acc = acc + moneyOffer;
                ecoRooms =  ecoRooms - 1;
            } else {
                break;
            }
        }


        CalculateOccupancyPriceResponseDTO calculateOccupancyPriceResponseDTO = new CalculateOccupancyPriceResponseDTO();
        calculateOccupancyPriceResponseDTO.setPricePremium(acc);
        calculateOccupancyPriceResponseDTO.setUsageEconomy(requestDTO.getFreeEconomyRooms() - ecoRooms);
        calculateOccupancyPriceResponseDTO.setUsagePremium(requestDTO.getFreePremiumRooms() - premiumRooms);
        return calculateOccupancyPriceResponseDTO;
    }

}
