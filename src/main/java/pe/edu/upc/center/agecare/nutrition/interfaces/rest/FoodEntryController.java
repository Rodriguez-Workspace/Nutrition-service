package pe.edu.upc.center.agecare.nutrition.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.center.agecare.nutrition.domain.model.queries.*;
import pe.edu.upc.center.agecare.nutrition.domain.model.valueobjects.MealType;
import pe.edu.upc.center.agecare.nutrition.domain.services.FoodEntryCommandService;
import pe.edu.upc.center.agecare.nutrition.domain.services.FoodEntryQueryService;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources.CreateFoodEntryResource;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources.FoodEntryResource;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources.UpdateFoodEntryResource;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.transform.CreateFoodEntryCommandFromResourceAssembler;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.transform.FoodEntryResourceFromEntityAssembler;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.transform.UpdateFoodEntryCommandFromResourceAssembler;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/food-entries", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Food Entries", description = "Food Entry Management Endpoints")
public class FoodEntryController {
    private final FoodEntryCommandService foodEntryCommandService;
    private final FoodEntryQueryService foodEntryQueryService;

    public FoodEntryController(FoodEntryCommandService foodEntryCommandService,
                               FoodEntryQueryService foodEntryQueryService) {
        this.foodEntryCommandService = foodEntryCommandService;
        this.foodEntryQueryService = foodEntryQueryService;
    }

    @Operation(summary = "Create a new food entry", description = "Create a new food entry with the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Food entry created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<FoodEntryResource> createFoodEntry(@RequestBody CreateFoodEntryResource resource) {
        var createFoodEntryCommand = CreateFoodEntryCommandFromResourceAssembler.toCommandFromResource(resource);
        var foodEntryId = foodEntryCommandService.handle(createFoodEntryCommand);
        
        if (foodEntryId == 0L) {
            return ResponseEntity.badRequest().build();
        }
        
        var getFoodEntryByIdQuery = new GetFoodEntryByIdQuery(foodEntryId);
        var foodEntry = foodEntryQueryService.handle(getFoodEntryByIdQuery);
        
        if (foodEntry.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var foodEntryResource = FoodEntryResourceFromEntityAssembler.toResourceFromEntity(foodEntry.get());
        return new ResponseEntity<>(foodEntryResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all food entries", description = "Get all food entries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food entries found")
    })
    @GetMapping
    public ResponseEntity<List<FoodEntryResource>> getAllFoodEntries() {
        var getAllFoodEntriesQuery = new GetAllFoodEntriesQuery();
        var foodEntries = foodEntryQueryService.handle(getAllFoodEntriesQuery);
        var foodEntryResources = foodEntries.stream()
                .map(FoodEntryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodEntryResources);
    }

    @Operation(summary = "Get food entry by id", description = "Get food entry by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food entry found"),
            @ApiResponse(responseCode = "404", description = "Food entry not found")
    })
    @GetMapping("/{foodEntryId}")
    public ResponseEntity<FoodEntryResource> getFoodEntryById(@PathVariable Long foodEntryId) {
        var getFoodEntryByIdQuery = new GetFoodEntryByIdQuery(foodEntryId);
        var foodEntry = foodEntryQueryService.handle(getFoodEntryByIdQuery);
        
        if (foodEntry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var foodEntryResource = FoodEntryResourceFromEntityAssembler.toResourceFromEntity(foodEntry.get());
        return ResponseEntity.ok(foodEntryResource);
    }

    @Operation(summary = "Get food entries by resident id", description = "Get all food entries for a specific resident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food entries found")
    })
    @GetMapping("/resident/{residentId}")
    public ResponseEntity<List<FoodEntryResource>> getFoodEntriesByResidentId(@PathVariable Long residentId) {
        var getFoodEntriesByResidentIdQuery = new GetFoodEntriesByResidentIdQuery(residentId);
        var foodEntries = foodEntryQueryService.handle(getFoodEntriesByResidentIdQuery);
        var foodEntryResources = foodEntries.stream()
                .map(FoodEntryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodEntryResources);
    }

    @Operation(summary = "Get food entries by date", description = "Get all food entries for a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food entries found")
    })
    @GetMapping("/date/{date}")
    public ResponseEntity<List<FoodEntryResource>> getFoodEntriesByDate(@PathVariable String date) {
        var getFoodEntriesByDateQuery = new GetFoodEntriesByDateQuery(date);
        var foodEntries = foodEntryQueryService.handle(getFoodEntriesByDateQuery);
        var foodEntryResources = foodEntries.stream()
                .map(FoodEntryResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(foodEntryResources);
    }

    @Operation(summary = "Get food entries by meal type", description = "Get all food entries for a specific meal type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food entries found")
    })
    @GetMapping("/meal/{mealType}")
    public ResponseEntity<List<FoodEntryResource>> getFoodEntriesByMealType(@PathVariable String mealType) {
        try {
            MealType meal = MealType.valueOf(mealType.toUpperCase());
            var getFoodEntriesByMealTypeQuery = new GetFoodEntriesByMealTypeQuery(meal);
            var foodEntries = foodEntryQueryService.handle(getFoodEntriesByMealTypeQuery);
            var foodEntryResources = foodEntries.stream()
                    .map(FoodEntryResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(foodEntryResources);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update food entry", description = "Update food entry by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Food entry updated"),
            @ApiResponse(responseCode = "404", description = "Food entry not found")
    })
    @PutMapping("/{foodEntryId}")
    public ResponseEntity<FoodEntryResource> updateFoodEntry(@PathVariable Long foodEntryId,
                                                             @RequestBody UpdateFoodEntryResource resource) {
        var updateFoodEntryCommand = UpdateFoodEntryCommandFromResourceAssembler.toCommandFromResource(foodEntryId, resource);
        var updatedFoodEntry = foodEntryCommandService.handle(updateFoodEntryCommand);
        
        if (updatedFoodEntry.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var foodEntryResource = FoodEntryResourceFromEntityAssembler.toResourceFromEntity(updatedFoodEntry.get());
        return ResponseEntity.ok(foodEntryResource);
    }

    // Delete endpoint removed intentionally. Deleting food entries is not supported via API.
}
