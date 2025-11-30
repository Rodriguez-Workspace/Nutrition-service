package pe.edu.upc.center.agecare.nutrition.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.domain.model.queries.*;
import pe.edu.upc.center.agecare.nutrition.domain.services.FoodEntryQueryService;
import pe.edu.upc.center.agecare.nutrition.infrastructure.persistence.jpa.repositories.FoodEntryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FoodEntryQueryServiceImpl implements FoodEntryQueryService {
    private final FoodEntryRepository foodEntryRepository;

    public FoodEntryQueryServiceImpl(FoodEntryRepository foodEntryRepository) {
        this.foodEntryRepository = foodEntryRepository;
    }

    @Override
    public List<FoodEntry> handle(GetAllFoodEntriesQuery query) {
        return foodEntryRepository.findAll();
    }

    @Override
    public Optional<FoodEntry> handle(GetFoodEntryByIdQuery query) {
        return foodEntryRepository.findById(query.foodEntryId());
    }

    @Override
    public List<FoodEntry> handle(GetFoodEntriesByResidentIdQuery query) {
        return foodEntryRepository.findByResidentId(query.residentId());
    }

    @Override
    public List<FoodEntry> handle(GetFoodEntriesByDateQuery query) {
        return foodEntryRepository.findByDate(query.date());
    }

    @Override
    public List<FoodEntry> handle(GetFoodEntriesByMealTypeQuery query) {
        return foodEntryRepository.findByMeal(query.mealType());
    }
}
