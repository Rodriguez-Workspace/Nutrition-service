package pe.edu.upc.center.agecare.nutrition.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.CreateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.domain.model.commands.UpdateFoodEntryCommand;
import pe.edu.upc.center.agecare.nutrition.domain.services.FoodEntryCommandService;
import pe.edu.upc.center.agecare.nutrition.infrastructure.persistence.jpa.repositories.FoodEntryRepository;
import pe.edu.upc.center.agecare.nutrition.infrastructure.integration.NotificationServiceClient;

import java.util.Optional;

@Service
public class FoodEntryCommandServiceImpl implements FoodEntryCommandService {
    private final FoodEntryRepository foodEntryRepository;
    private final NotificationServiceClient notificationServiceClient;

    public FoodEntryCommandServiceImpl(FoodEntryRepository foodEntryRepository, NotificationServiceClient notificationServiceClient) {
        this.foodEntryRepository = foodEntryRepository;
        this.notificationServiceClient = notificationServiceClient;
    }

    @Override
    public Long handle(CreateFoodEntryCommand command) {
        var foodEntry = new FoodEntry(command);
        try {
            foodEntryRepository.save(foodEntry);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while saving food entry: " + e.getMessage());
        }

        // Send notification (non-blocking behaviour: don't fail creation if notification fails)
        try {
            String message = String.format("New meal recorded: %s - %s on %s %s",
                    command.meal() != null ? command.meal().name() : "UNKNOWN",
                    command.description() != null ? command.description() : "",
                    command.date() != null ? command.date().toString() : "",
                    command.time() != null ? command.time().toString() : "");

            // resident id is a Long in Nutrition create command
            notificationServiceClient.sendNotification(command.residentId(), message);
        } catch (Exception e) {
            System.err.println("Failed to send notification for food entry: " + e.getMessage());
        }

        return foodEntry.getId();
    }

    @Override
    public Optional<FoodEntry> handle(UpdateFoodEntryCommand command) {
        var foodEntryId = command.foodEntryId();

        if (!foodEntryRepository.existsById(foodEntryId)) {
            throw new IllegalArgumentException("Food entry does not exist");
        }

        var foodEntryToUpdate = foodEntryRepository.findById(foodEntryId).get();

        try {
            var updatedFoodEntry = foodEntryRepository.save(
                foodEntryToUpdate.updateInformation(
                    command.meal(),
                    command.description(),
                    command.date(),
                    command.time()
                )
            );
            return Optional.of(updatedFoodEntry);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while updating food entry: " + e.getMessage());
        }
    }

}
