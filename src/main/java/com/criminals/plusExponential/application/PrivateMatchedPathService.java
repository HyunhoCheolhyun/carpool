package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Fare;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivateMatchedPathService {

    private final PrivateMatchedPathRepository privateMatchedPathRepository;
    private final PathService pathService;

    public void createPrivateMatchedPath(MatchedPath matchedPath, UnmatchedPathDto newRequest, UnmatchedPathDto partner) {

        PrivateMatchedPath aPrivateMatchedPath = new PrivateMatchedPath();
        PrivateMatchedPath bPrivateMatchedPath = new PrivateMatchedPath();

        setPrivateMatchedPathToll(matchedPath, newRequest, partner, aPrivateMatchedPath, bPrivateMatchedPath);
        setPrivateMatchedPathPoints(matchedPath, newRequest, partner, aPrivateMatchedPath, bPrivateMatchedPath);
        setPrivateMatchedPathDistance(aPrivateMatchedPath, bPrivateMatchedPath);
        setPrivateMatchedPathFareTaxi(matchedPath, aPrivateMatchedPath, bPrivateMatchedPath);
        setPrivateMatchedPathDuration(matchedPath, aPrivateMatchedPath, bPrivateMatchedPath);


    }

    private void setPrivateMatchedPathDuration(MatchedPath matchedPath, PrivateMatchedPath a, PrivateMatchedPath b) {

        switch (matchedPath.getType()) {
            case 0:
                a.setDuration(pathService.getSummary(matchedPath.getInitPoint(), matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint()).duration);
                b.setDuration(pathService.getSummary(matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint(), matchedPath.getDestinationPoint()).duration);
            case 1:
                a.setDuration(matchedPath.getDuration());
                b.setDuration(pathService.getSummary(matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint()).duration);
            case 2:
                a.setDuration(pathService.getSummary(matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint(), matchedPath.getDestinationPoint()).duration);
                b.setDuration(pathService.getSummary(matchedPath.getInitPoint(), matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint()).duration);
            case 3:
                a.setDuration(pathService.getSummary(matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint()).duration);
                b.setDuration(matchedPath.getDuration());
        }
    }

    private void setPrivateMatchedPathDistance(PrivateMatchedPath a, PrivateMatchedPath b) {

        setPrivateMatchedPathDistanceDetail(a);
        setPrivateMatchedPathDistanceDetail(b);
    }

    private void setPrivateMatchedPathDistanceDetail(PrivateMatchedPath pm) {
        if (pm.getFirstWayPoint() == null) {
            pm.setDistance(pathService.getSummary(pm.getInitPoint(), pm.getDestinationPoint()).distance);
        } else {
            if (pm.getSecondWayPoint() == null) {
                PathService.Summary summary1 = pathService.getSummary(pm.getInitPoint(), pm.getFirstWayPoint());
                PathService.Summary summary2 = pathService.getSummary(pm.getFirstWayPoint(), pm.getDestinationPoint());
                pm.setDistance(summary1.distance + summary2.distance);
            } else {
                pm.setDistance(pathService.getSummary(pm.getInitPoint(), pm.getFirstWayPoint(), pm.getSecondWayPoint(), pm.getDestinationPoint()).distance);
            }
        }
    }

    private void setPrivateMatchedPathFareTaxi(MatchedPath matchedPath, PrivateMatchedPath a, PrivateMatchedPath b) {

        int totalTaxiFare = matchedPath.getFare().getTaxi();
        int totalDistance = matchedPath.getDistance();
        int coRideDistance = pathService.getSummary(matchedPath.getFirstWayPoint(), matchedPath.getSecondWayPoint()).distance;
        int aSoloDistance = a.getDistance() - coRideDistance;
        int bSoloDistance = b.getDistance() - coRideDistance;

        int aTaxiFare = (totalTaxiFare * aSoloDistance / totalDistance) + (totalTaxiFare * coRideDistance / totalDistance / 2);
        Double ra = ((double) totalTaxiFare * (double) aSoloDistance / (double) totalDistance) + ((double) totalTaxiFare * (double) coRideDistance / (double) totalDistance / 2);
        ra -= aTaxiFare;


        int bTaxiFare = (totalTaxiFare * bSoloDistance / totalDistance) + (totalTaxiFare * coRideDistance / totalDistance / 2);
        Double rb = ((double) totalTaxiFare * (double) bSoloDistance / (double) totalDistance) + ((double) totalTaxiFare * (double) coRideDistance / (double) totalDistance / 2);
        rb -= aTaxiFare;


        if (aTaxiFare + bTaxiFare < totalTaxiFare) {
            int difference = totalTaxiFare - (aTaxiFare + bTaxiFare);

            if (ra > rb) {
                a.getFare().setTaxi(aTaxiFare + difference);
                b.getFare().setTaxi(bTaxiFare);
            } else {
                a.getFare().setTaxi(aTaxiFare);
                b.getFare().setTaxi(bTaxiFare + difference);
            }


        }
    }







    private void setPrivateMatchedPathToll(MatchedPath matchedPath, UnmatchedPathDto newRequest, UnmatchedPathDto partner, PrivateMatchedPath aPrivateMatchedPath, PrivateMatchedPath bPrivateMatchedPath) {
        int matchedToll = matchedPath.getFare().getToll();

        int aToll = newRequest.getFare().getToll();
        int bToll = partner.getFare().getToll();

        if (matchedToll > 0) {
            int unmatchedTollSum = aToll + bToll;

            if (matchedToll > unmatchedTollSum) {
                int dToll = matchedToll - unmatchedTollSum;
                aToll += (dToll / 2);
                bToll += (dToll / 2);


                setToll(aPrivateMatchedPath, aToll);
                setToll(bPrivateMatchedPath, bToll);
            } else if (matchedToll == unmatchedTollSum) {
                setToll(aPrivateMatchedPath, aToll);
                setToll(bPrivateMatchedPath, bToll);
            } else if (matchedToll < unmatchedTollSum) {

                int dToll = unmatchedTollSum - matchedToll;
                aToll -= (dToll / 2);
                bToll -= (dToll / 2);

                setToll(aPrivateMatchedPath, aToll);
                setToll(bPrivateMatchedPath, bToll);
            }

        } else {
            setToll(aPrivateMatchedPath, aToll);
            setToll(bPrivateMatchedPath, bToll);
        }
    }

    private void setToll(PrivateMatchedPath privateMatchedPath, int toll) {
        Fare fare = privateMatchedPath.getFare();
        fare.setToll(toll);
        privateMatchedPath.setFare(fare);
    }

    private void setPrivateMatchedPathPoints(MatchedPath matchedPath, UnmatchedPathDto newRequest, UnmatchedPathDto partner, PrivateMatchedPath a, PrivateMatchedPath b) {

        a.setInitPoint(newRequest.getInitPoint());
        b.setInitPoint(partner.getInitPoint());
        switch (matchedPath.getType()) {
            //a출 b출 a도 b도
            case 0:
                a.setFirstWayPoint(partner.getInitPoint());
                a.setDestinationPoint(newRequest.getDestinationPoint());

                b.setFirstWayPoint(newRequest.getDestinationPoint());
                b.setDestinationPoint(partner.getDestinationPoint());
            //a출 b출 b도 a도
            case 1:

                a.setFirstWayPoint(partner.getInitPoint());
                a.setSecondWayPoint(partner.getDestinationPoint());
                a.setDestinationPoint(newRequest.getDestinationPoint());

                b.setDestinationPoint(partner.getDestinationPoint());
            // b출 a출 b도 a도
            case 2:
                a.setFirstWayPoint(partner.getDestinationPoint());
                a.setDestinationPoint(newRequest.getDestinationPoint());

                b.setFirstWayPoint(newRequest.getInitPoint());
                b.setDestinationPoint(partner.getDestinationPoint());
            //b출 a출 a도 b도
            case 3:
                a.setDestinationPoint(newRequest.getDestinationPoint());

                b.setFirstWayPoint(newRequest.getInitPoint());
                b.setSecondWayPoint(newRequest.getDestinationPoint());
                b.setDestinationPoint(partner.getDestinationPoint());

        }
    }
}
