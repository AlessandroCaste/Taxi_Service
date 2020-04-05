package com.taxi.sa.repositories;

import com.taxi.sa.parsing.output.city.CityMap;
import com.taxi.sa.parsing.output.user.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, String> {
    Optional<List<Taxi>> findAllByCityMap(CityMap cityMap);
}
