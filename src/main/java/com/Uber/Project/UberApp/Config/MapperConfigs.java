package com.Uber.Project.UberApp.Config;

import com.Uber.Project.UberApp.DTO.PointDTO;
import com.Uber.Project.UberApp.Utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfigs {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(PointDTO.class, Point.class).setConverter(context->{
            PointDTO pointDTO = (PointDTO) context.getSource();
            return GeometryUtil.createPoint(pointDTO);
        });

        mapper.typeMap(Point.class, PointDTO.class).setConverter(context->{
            Point point = context.getSource();

            double coordinates[] = {
                    point.getX(),
                    point.getY()
            };
            return new PointDTO(coordinates);
        });
        return mapper;
    }

}
