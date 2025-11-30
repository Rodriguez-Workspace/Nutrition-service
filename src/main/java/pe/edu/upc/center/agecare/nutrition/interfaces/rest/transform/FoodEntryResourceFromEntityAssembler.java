package pe.edu.upc.center.agecare.nutrition.interfaces.rest.transform;

import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.interfaces.rest.resources.FoodEntryResource;

import java.text.SimpleDateFormat;

public class FoodEntryResourceFromEntityAssembler {
    public static FoodEntryResource toResourceFromEntity(FoodEntry entity) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        String createdAt = entity.getCreatedAt() != null ? dateFormat.format(entity.getCreatedAt()) : null;
        
        return new FoodEntryResource(
                entity.getId(),
                entity.getMeal(),
                entity.getDescription(),
                entity.getDate(),
                entity.getTime(),
                createdAt,
                entity.getAddedBy(),
                entity.getAddedById(),
                entity.getResidentId()
        );
    }
}
