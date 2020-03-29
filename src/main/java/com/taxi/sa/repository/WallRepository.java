package com.taxi.sa.repository;

import com.taxi.sa.parsing.city.Wall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WallRepository extends JpaRepository<Wall, String> {
}
