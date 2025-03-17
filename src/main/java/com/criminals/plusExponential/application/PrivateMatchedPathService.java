package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
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

    public void createPrivateMatchedPath(UnmatchedPathDto newRequest, UnmatchedPathDto partner, MatchedPath matchedPath) {
        int type = matchedPath.getType();

        PrivateMatchedPath a = new PrivateMatchedPath();
        a.setUser(newRequest.getUser());
        a.setMatchedPath(matchedPath);

        PrivateMatchedPath b = new PrivateMatchedPath();
        b.setUser(partner.getUser());
        b.setMatchedPath(matchedPath);

        switch (type) {
            case 0:
                a.setInitPoint(newRequest.getInitPoint());
                a.setFirstWayPoint(partner.getInitPoint());
                a.setDestinationPoint(newRequest.getDestinationPoint());

        }
    }
}
