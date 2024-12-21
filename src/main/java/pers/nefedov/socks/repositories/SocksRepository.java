package pers.nefedov.socks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pers.nefedov.socks.models.Socks;

import java.util.Optional;

public interface SocksRepository extends JpaRepository<Socks, Long> {

    Socks getSocksByColorAndCottonPercentage(String color, Double cottonPercentage);

    Optional<Socks> findByColorAndCottonPercentage(String color, Double cottonPercentage);

    @Query("SELECT SUM(s.quantity) FROM Socks s " +
            "WHERE (:color IS NULL OR s.color = :color) " +
            "AND (:cottonPercentage IS NULL OR " +
            "(:comparison = 'lessThan' AND s.cottonPercentage < :cottonPercentage) " +
            "OR (:comparison = 'moreThan' AND s.cottonPercentage > :cottonPercentage) " +
            "OR (:comparison = 'equal' AND s.cottonPercentage = :cottonPercentage))")
    Optional<Integer> sumQuantityWithFilters(
            @Param("color") String color,
            @Param("cottonPercentage") Double cottonPercentage,
            @Param("comparison") String comparison);
}