package pe.edu.upc.center.agecare.nutrition.domain.services;

import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface FoodEntryQueryService {
    List<FoodEntry> handle(GetAllFoodEntriesQuery query);
    Optional<FoodEntry> handle(GetFoodEntryByIdQuery query);
    List<FoodEntry> handle(GetFoodEntriesByResidentIdQuery query);
    List<FoodEntry> handle(GetFoodEntriesByDateQuery query);
    List<FoodEntry> handle(GetFoodEntriesByMealTypeQuery query);
}
