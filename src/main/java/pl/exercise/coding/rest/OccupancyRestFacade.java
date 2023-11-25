package pl.exercise.coding.rest;

import org.springframework.http.ResponseEntity;
import pl.exercise.coding.api.OccupancyApi;
import pl.exercise.coding.api.model.CalculateOccupancyPriceRequestDTO;
import pl.exercise.coding.api.model.CalculateOccupancyPriceResponseDTO;

public class OccupancyRestFacade implements OccupancyApi {
    @Override
    public ResponseEntity<CalculateOccupancyPriceResponseDTO> occupancyPriceGet(CalculateOccupancyPriceRequestDTO calculateOccupancyPriceRequestDTO) {
        return null;
    }
}
