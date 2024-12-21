package pers.nefedov.socks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.nefedov.socks.models.Socks;

import java.util.Optional;

public interface SocksRepository extends JpaRepository<Socks, Long> {

    Socks getSocksByColorAndCottonPercentage(String color, Double cottonPercentage);

    Optional<Socks> findByColorAndCottonPercentage(String color, Double cottonPercentage);
}
