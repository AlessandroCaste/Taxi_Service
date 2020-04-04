package com.taxi.sa.parsing;

import com.taxi.sa.parsing.input.InputMap;
import com.taxi.sa.parsing.output.CityMap;
import com.taxi.sa.parsing.users.Taxi;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Verifies input data is sound
@Service
public class PersistanceService {

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

    public void storeCityMap(InputMap inputMap) {
   /*     String cityId = inputMap.getCityId();
        CityMap newMap = inputMap.getCityMap();
        Optional<CityMap> oldMap = mapRepository.findById(cityId);
        if(oldMap.isPresent()) {
            oldMap.get().clear();
            mapRepository.delete(oldMap.get());
        }
        mapRepository.save(newMap);
        for(Wall wall: inputMap.getWalls()) {
            newMap.addWall(wall);
            wallRepository.save(wall);
        }
        for(Checkpoint checkpoint: inputMap.getCheckpoints()) {
            newMap.addCheckpoint(checkpoint);
            checkpointRepository.save(checkpoint);
        }*/
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

