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

    public Coordinate getLocation(String socketId) {
        GeoOperations<String, Object> geoOps = redisTemplate.opsForGeo();
        List<Point> points = geoOps.position(LOCATION_KEY, socketId);

        if (points != null && !points.isEmpty() && points.get(0) != null) {
            Point point = points.get(0);
            double lng = point.getX(); // 경도
            double lat = point.getY(); // 위도
            return new Coordinate(lat, lng); // Coordinate 객체 생성 (생성자에 맞게 수정)
        }
        return null; // 해당 socketId의 위치 정보가 없을 경우
    }

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
