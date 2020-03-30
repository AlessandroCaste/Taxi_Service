package com.taxi.sa.repositories;

import com.taxi.sa.parsing.users.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxiRepository extends JpaRepository<Taxi, String> {
}
