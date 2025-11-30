package pe.edu.upc.center.agecare.nutrition.domain.services;

import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.CreateFoodEntryCommand;
 
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.UpdateFoodEntryCommand;

import java.util.Optional;

public interface FoodEntryCommandService {
    Long handle(CreateFoodEntryCommand command);
    Optional<FoodEntry> handle(UpdateFoodEntryCommand command);
}
