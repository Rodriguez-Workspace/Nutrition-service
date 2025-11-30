package pe.edu.upc.center.agecare.nutrition.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.center.agecare.nutrition.domain.model.aggregates.FoodEntry;
import pe.edu.upc.center.agecare.nutrition.domain.model.valueobjects.MealType;

import java.util.List;

@Repository
public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByResidentId(Long residentId);
    List<FoodEntry> findByDate(String date);
    List<FoodEntry> findByMeal(MealType meal);
    List<FoodEntry> findByAddedById(Long addedById);
}
