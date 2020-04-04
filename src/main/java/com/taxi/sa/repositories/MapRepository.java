package com.taxi.sa.repositories;

import com.taxi.sa.parsing.output.CityMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends JpaRepository<CityMap, String> {
}
