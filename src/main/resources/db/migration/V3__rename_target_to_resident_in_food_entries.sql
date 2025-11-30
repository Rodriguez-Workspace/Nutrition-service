-- Rename column target_id to resident_id in food_entries
ALTER TABLE `food_entries` CHANGE COLUMN `target_id` `resident_id` BIGINT;