package pe.edu.upc.center.agecare.nutrition.domain.model.commands;

import pe.edu.upc.center.agecare.nutrition.domain.model.valueobjects.MealType;

public record CreateFoodEntryCommand(
        MealType meal,
        String description,
        String date,
        String time,
        String addedBy,
        Long addedById,
        Long residentId
) {
}
