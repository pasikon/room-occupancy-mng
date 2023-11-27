package pl.exercise.coding.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.exercise.coding.api.OccupancyApi;
import pl.exercise.coding.api.model.CalculateOccupancyPriceRequestDTO;
import pl.exercise.coding.api.model.CalculateOccupancyPriceResponseDTO;
import pl.exercise.coding.service.OccupancyCalculatorService;


@Slf4j
@RestController
/**
 * REST API implementation for REST interface
 */
public class OccupancyRestFacade implements OccupancyApi {

    final
    OccupancyCalculatorService occupancyCalculatorService;

    public OccupancyRestFacade(OccupancyCalculatorService occupancyCalculatorService) {
        this.occupancyCalculatorService = occupancyCalculatorService;
    }

    @Override
    public ResponseEntity<CalculateOccupancyPriceResponseDTO> occupancyPriceGet(CalculateOccupancyPriceRequestDTO calculateOccupancyPriceRequestDTO) {
        return ResponseEntity.ok(occupancyCalculatorService.calculateOccupancy(calculateOccupancyPriceRequestDTO));
    }
}
