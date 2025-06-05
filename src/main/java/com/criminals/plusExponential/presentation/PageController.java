package com.criminals.plusExponential.presentation;

import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.NotFoundException;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.infrastructure.config.security.CustomUserDetails;
import com.criminals.plusExponential.infrastructure.persistence.MatchedPathRepository;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import com.criminals.plusExponential.infrastructure.redis.RedisPgTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final RedisPgTokenRepository redisPgTokenRepository;
    private final MatchedPathRepository matchedPathRepository;
    private final PrivateMatchedPathRepository privateMatchedPathRepository;


    // 홈화면 -> 택시기사, 승객 따로 랜더링
    @GetMapping("/")
    public String home(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails.getUser().getRole() == Role.PASSENGER) {
            return "home";
        } else {
            return "driver";
        }
    }

    // 결제완료 후 승객 화면
    @GetMapping("/waiting")
    public String matched(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(value = "pg_token") String pgToken) {
        redisPgTokenRepository.publishPaymentToken(customUserDetails.getUserId(), pgToken);

        return "waiting";
    }

    // 매칭후 승객 화면
    @GetMapping("/match-passenger/{privateMatchedPathId}")
    public String matchPassenger(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("privateMatchedPathId") Long privateMatchedPathId) {
        PrivateMatchedPath pm = privateMatchedPathRepository.findById(privateMatchedPathId).orElseThrow(() -> new NotFoundException(ErrorCode.NotFoundException));
        model.addAttribute("distance", pm.getDistance());
        model.addAttribute("duration", pm.getDuration());
        model.addAttribute("price", pm.getFare().getTotal());
        model.addAttribute("savedCost", pm.getSavedAmount());
        model.addAttribute("initLat", pm.getInitPoint().getLat());
        model.addAttribute("initLng", pm.getInitPoint().getLng());


        if(pm.getMatchedPath().getDriver() != null){
            model.addAttribute("driverId", pm.getMatchedPath().getDriver().getId());
        }


        return "match-passenger";
    }

    // 매칭후 택시기사 화면
    @GetMapping("/match-driver/{matchedPathId}")
    public String matched(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("matchedPathId") Long matchedPathId) {
        MatchedPath matchedPath = matchedPathRepository.findById(matchedPathId)
                .orElseThrow(() -> new IllegalArgumentException("해당 경로가 존재하지 않습니다: " + matchedPathId));

        model.addAttribute("initLat", matchedPath.getInitPoint().getLat());
        model.addAttribute("initLng", matchedPath.getInitPoint().getLng());
        model.addAttribute("firstWayPointLat", matchedPath.getFirstWayPoint().getLat());
        model.addAttribute("firstWayPointLng", matchedPath.getFirstWayPoint().getLng());
        model.addAttribute("secondWayPointLat", matchedPath.getSecondWayPoint().getLat());
        model.addAttribute("secondWayPointLng", matchedPath.getSecondWayPoint().getLng());
        model.addAttribute("destinationLat", matchedPath.getDestinationPoint().getLat());
        model.addAttribute("destinationLng", matchedPath.getDestinationPoint().getLng());


        return "match-driver";
    }
}
