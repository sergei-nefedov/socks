package pers.nefedov.socks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pers.nefedov.socks.models.Socks;

public interface SocksRepository extends JpaRepository<Socks, Long> {

    Socks getSocksByColorAndCottonPercentage(String color, Double cottonPercentage);

}
