package pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources;

import pe.edu.upc.center.agecare.nutrition.domain.model.valueobjects.MealType;

public record UpdateFoodEntryResource(
        MealType meal,
        String description,
        String date,
        String time
) {
}
