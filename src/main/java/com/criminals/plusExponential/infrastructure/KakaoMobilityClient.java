package com.criminals.plusExponential.infrastructure;
import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.LoadSearchFailException;
import com.criminals.plusExponential.common.exception.customex.TooCloseBetweenInitAndDestination;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.Map;


@Component
public class KakaoMobilityClient {

    private final RestTemplate restTemplate;

    @Value("${REST_API_KEY}")
    private String restApiKey;

    public KakaoMobilityClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public Map<String, Object> getResponse(UnmatchedPathDto unmatchedPathDto) {
        String url = String.format(
                "https://apis-navi.kakaomobility.com/v1/directions?" +
                        "origin=%f,%f&destination=%f,%f" +
                        "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false" +
                        "&alternatives=false&road_details=false",
                unmatchedPathDto.getInitPoint().getLng(), unmatchedPathDto.getInitPoint().getLat(), unmatchedPathDto.getDestinationPoint().getLng(), unmatchedPathDto.getDestinationPoint().getLat()
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

    //외부 api와 통신이 잘 되는지 확인용
    //확인 결과 잘 됨
//    public String getSummaryTest(Double initLng, Double initLat, Double destinationLng, Double destinationLat) {
//        String url = String.format(
//                "https://apis-navi.kakaomobility.com/v1/directions?" +
//                        "origin=%f,%f&destination=%f,%f" +
//                        "&waypoints=&priority=RECOMMEND&car_fuel=GASOLINE&car_hipass=false" +
//                        "&alternatives=false&road_details=false",
//                initLng, initLat, destinationLng, destinationLat
//        );
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "KakaoAK " + restApiKey);
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//            return response.getBody();  // return raw JSON
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to fetch directions: " + e.getMessage(), e);
//        }
//    }

    }


