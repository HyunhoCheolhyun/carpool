package com.criminals.plusExponential.infrastructure.redis;

import com.criminals.plusExponential.domain.embeddable.Coordinate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class RedisLocationRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String LOCATION_KEY = "LOCATION:";

    public void saveLocation(String socketId, Coordinate coordinate) {
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        geoOps.add(LOCATION_KEY, new Point(coordinate.getLng(), coordinate.getLat()), socketId);
    }

    // 반경 안에 드라이버 검색
    public List<String> findNearbyDrivers(Coordinate coordinate, double radiusKm) {
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        Circle within = new Circle(new Point(coordinate.getLng(), coordinate.getLat()),
                new Distance(radiusKm, Metrics.KILOMETERS));

        GeoResults<RedisGeoCommands.GeoLocation<Object>> results =
                geoOps.radius(LOCATION_KEY, within);


        return results.getContent().stream()
                .map(geoLocation -> (String) geoLocation.getContent().getName())
                .collect(Collectors.toList());
    }

    public void clearAll() {
        redisTemplate.delete(LOCATION_KEY);
    }
}
