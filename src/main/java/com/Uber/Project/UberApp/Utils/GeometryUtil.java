package com.Uber.Project.UberApp.Utils;

import com.Uber.Project.UberApp.DTO.PointDTO;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtil {
    public static Point createPoint(PointDTO pointDTO) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate=new Coordinate(pointDTO.getCoordinates()[0],
                pointDTO.getCoordinates()[1]);

        return geometryFactory.createPoint(coordinate);
    }
}
