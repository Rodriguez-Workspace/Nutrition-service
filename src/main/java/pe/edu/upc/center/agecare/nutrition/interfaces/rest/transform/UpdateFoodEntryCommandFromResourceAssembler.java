package pe.edu.upc.center.agecare.nutrition.interfaces.rest.transform;

import pe.edu.upc.center.agecare.nutrition.domain.model.commands.UpdateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources.UpdateFoodEntryResource;

public class UpdateFoodEntryCommandFromResourceAssembler {
    public static UpdateFoodEntryCommand toCommandFromResource(Long foodEntryId, UpdateFoodEntryResource resource) {
        return new UpdateFoodEntryCommand(
                foodEntryId,
                resource.meal(),
                resource.description(),
                resource.date(),
                resource.time()
        );
    }
}
