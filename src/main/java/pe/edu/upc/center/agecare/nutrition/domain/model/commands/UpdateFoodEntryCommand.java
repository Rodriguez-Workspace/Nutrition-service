package pe.edu.upc.center.agecare.nutrition.domain.model.commands;

import pe.edu.upc.center.agecare.nutrition.domain.model.valueobjects.MealType;

public record UpdateFoodEntryCommand(
        Long foodEntryId,
        MealType meal,
        String description,
        String date,
        String time
) {
}
