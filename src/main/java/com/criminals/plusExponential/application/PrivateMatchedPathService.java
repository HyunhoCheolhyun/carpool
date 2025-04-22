package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.domain.embeddable.Fare;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import org.springframework.stereotype.Service;

@Service
public class PrivateMatchedPathService {

    private final PrivateMatchedPathRepository privateMatchedPathRepository;


    public PrivateMatchedPathService(PrivateMatchedPathRepository privateMatchedPathRepository) {
        this.privateMatchedPathRepository = privateMatchedPathRepository;
    }

    public void createPrivateMatchedPath(MatchedPath matchedPath, UnmatchedPathDto newRequest, UnmatchedPathDto partner) {

        PrivateMatchedPath aPrivateMatchedPath = new PrivateMatchedPath();
        PrivateMatchedPath bPrivateMatchedPath = new PrivateMatchedPath();
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
}
