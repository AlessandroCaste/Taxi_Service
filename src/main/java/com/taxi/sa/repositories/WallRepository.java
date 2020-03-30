package com.taxi.sa.repositories;

import com.taxi.sa.parsing.city.CityMap;
import com.taxi.sa.parsing.city.Wall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WallRepository extends JpaRepository<Wall, String> {
    Optional<List<Wall>> findAllByCityMap(CityMap cityMap);
}
