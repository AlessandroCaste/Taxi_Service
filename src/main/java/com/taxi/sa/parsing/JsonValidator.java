package com.taxi.sa.parsing;

import com.taxi.sa.parsing.city.Checkpoint;
import com.taxi.sa.parsing.city.CityMap;
import com.taxi.sa.parsing.city.Wall;
import com.taxi.sa.parsing.users.Taxi;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

@Service
public class JsonValidator {

    private MapRepository mapRepository;
    private WallRepository wallRepository;
    private CheckpointRepository checkpointRepository;
    private TaxiRepository taxiRepository;

    @Autowired
    private void setMapRepository(MapRepository mapRepository){
        this.mapRepository = mapRepository;
    }

    @Autowired
    private void setWallRepository(WallRepository wallRepository) {
        this.wallRepository = wallRepository;
    }

    @Autowired
    private void setCheckpointRepository(CheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    @Autowired
    private void setTaxiRepository(TaxiRepository taxiRepository) {
        this.taxiRepository = taxiRepository;
    }

    public void storeCityMap(ReceivedMap receivedMap) {
        CityMap newMap = receivedMap.getCityMap();
        if(mapRepository.existsById(newMap.getCityId())) {
            newMap.clear();
            mapRepository.delete(newMap);
        }
        mapRepository.save(newMap);
        for(Wall wall: receivedMap.getWalls()) {
            newMap.addWall(wall);
            wallRepository.save(wall);
        }
        for(Checkpoint checkpoint: receivedMap.getCheckpoints()) {
            newMap.addCheckpoint(checkpoint);
            checkpointRepository.save(checkpoint);
        }
    }

    public void storeTaxi(String taxiId, String city, Coordinate position) {
        if(mapRepository.findById(city).isEmpty())
            throw new MappingException("no city named '" + city + "' exists");
        CityMap cityMap = mapRepository.findById(city).get();
        Taxi taxi = new Taxi(taxiId, position, cityMap);
        if(taxiRepository.findById(taxiId).isPresent())
            taxiRepository.delete(taxi);
        taxiRepository.save(taxi);
    }
}

