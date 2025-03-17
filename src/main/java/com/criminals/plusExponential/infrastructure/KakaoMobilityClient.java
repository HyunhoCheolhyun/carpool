package com.criminals.plusExponential.infrastructure;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.LoadSearchFailException;
import com.criminals.plusExponential.common.exception.customex.TooCloseBetweenInitAndDestination;
import com.criminals.plusExponential.domain.embeddable.Coordinate;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class KakaoMobilityClient {

    private final RestTemplate restTemplate;

    @Value("${REST_API_KEY}")
    private String restApiKey;

    public KakaoMobilityClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Map<String, Object> getResponse(Coordinate point1, Coordinate point2) {
        String url = String.format(
                "https://apis-navi.kakaomobility.com/v1/directions?" +
                        "origin=%f,%f&destination=%f,%f" +
                        "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false" +
                        "&alternatives=false&road_details=false",
                point1.getLng(), point1.getLat(), point2.getLng(), point2.getLat()
        );


        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + restApiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // JSON 응답을 파싱하기 위해 Jackson ObjectMapper 사용
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode routes = root.path("routes");

            if (routes.isArray() && routes.size() > 0) {
                // routes 배열의 첫 번째 요소를 Map으로 변환
                JsonNode firstRoute = routes.get(0);

                if (firstRoute.has("result_code") && firstRoute.get("result_code").asInt() == 104) {
                    throw new TooCloseBetweenInitAndDestination(ErrorCode.TooCloseBetweenInitAndDestination);
                }

                if (firstRoute.has("result_code") && firstRoute.get("result_code").asInt() != 0) {
                    throw new LoadSearchFailException(ErrorCode.LoadSearchFail);
                }

                return mapper.convertValue(firstRoute, new TypeReference<Map<String, Object>>() {});
            } else {
                throw new RuntimeException("No routes found in the response.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch directions: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getResponse(Coordinate point1, Coordinate point2, Coordinate point3, Coordinate point4) {
        String url = "https://apis-navi.kakaomobility.com/v1/waypoints/directions";


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + restApiKey);

        // JSON 요청 본문 생성 (출발지, 도착지, 경유지 목록 및 기타 파라미터)
        Map<String, Object> requestBody = new HashMap<>();

        // 출발지 (point1) - 예제에서는 angle 값을 고정(270)으로 사용
        Map<String, Object> origin = new HashMap<>();
        origin.put("x", String.valueOf(point1.getLng()));
        origin.put("y", String.valueOf(point1.getLat()));
        origin.put("angle", 270);
        requestBody.put("origin", origin);

        // 도착지 (point4)
        Map<String, Object> destination = new HashMap<>();
        destination.put("x", String.valueOf(point4.getLng()));
        destination.put("y", String.valueOf(point4.getLat()));
        requestBody.put("destination", destination);

        // 경유지: 여기서는 point2와 point3를 각각 경유지로 사용 (경유지 이름은 임의 지정)
        List<Map<String, Object>> waypoints = new ArrayList<>();

        Map<String, Object> waypoint1 = new HashMap<>();
        waypoint1.put("name", "waypoint1");
        waypoint1.put("x", point2.getLng());
        waypoint1.put("y", point2.getLat());
        waypoints.add(waypoint1);

        Map<String, Object> waypoint2 = new HashMap<>();
        waypoint2.put("name", "waypoint2");
        waypoint2.put("x", point3.getLng());
        waypoint2.put("y", point3.getLat());
        waypoints.add(waypoint2);

        requestBody.put("waypoints", waypoints);

        // 기타 고정 파라미터
        requestBody.put("priority", "RECOMMEND");
        requestBody.put("car_fuel", "GASOLINE");
        requestBody.put("car_hipass", false);
        requestBody.put("alternatives", false);
        requestBody.put("road_details", false);
        requestBody.put("summary", false);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 응답 JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode routes = root.path("routes");

            if (routes.isArray() && routes.size() > 0) {
                JsonNode firstRoute = routes.get(0);

                if (firstRoute.has("result_code") && firstRoute.get("result_code").asInt() == 104) {
                    throw new TooCloseBetweenInitAndDestination(ErrorCode.TooCloseBetweenInitAndDestination);
                }

                if (firstRoute.has("result_code") && firstRoute.get("result_code").asInt() != 0) {
                    throw new LoadSearchFailException(ErrorCode.LoadSearchFail);
                }

                return mapper.convertValue(firstRoute, new TypeReference<Map<String, Object>>() {});
            } else {
                throw new RuntimeException("No routes found in the response.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch directions: " + e.getMessage(), e);
        }
    }



    }


