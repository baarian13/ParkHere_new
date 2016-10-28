package com.lazeebear.parkhere.DAOs.ReturnedObjects;

import java.util.List;

/**
 * Created by rjaso on 10/27/2016.
 */

public class SpotListDAO {

    List<SpotDAO> spots;

    public List<SpotDAO> getSpots() {
        return spots;
    }

    public void setSpots(List<SpotDAO> spots) {
        this.spots = spots;
    }
}
