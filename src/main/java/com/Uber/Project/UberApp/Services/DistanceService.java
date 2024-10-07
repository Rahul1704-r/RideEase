package com.Uber.Project.UberApp.Services;

import org.locationtech.jts.geom.Point;

public interface DistanceService {

    double getDistance(Point src, Point dest);

    
}
