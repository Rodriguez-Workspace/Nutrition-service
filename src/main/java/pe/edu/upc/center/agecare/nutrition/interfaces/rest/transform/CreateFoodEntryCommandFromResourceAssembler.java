package pe.edu.upc.center.agecare.nutrition.interfaces.rest.transform;

import pe.edu.upc.center.agecare.nutrition.domain.model.commands.CreateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources.CreateFoodEntryResource;

public class CreateFoodEntryCommandFromResourceAssembler {
    public static CreateFoodEntryCommand toCommandFromResource(CreateFoodEntryResource resource) {
        return new CreateFoodEntryCommand(
                resource.meal(),
                resource.description(),
                resource.date(),
                resource.time(),
                resource.addedBy(),
                resource.addedById(),
                resource.residentId()
        );
    }
}
