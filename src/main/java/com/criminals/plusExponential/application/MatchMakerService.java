package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.infrastructure.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MatchMakerService extends PathService{

    private final List<UnmatchedPathDto> waitingList = new ArrayList<>();
    private final Map<UnmatchedPathDto, UnmatchedPathDto> matchingTable = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition partnerAvailable = lock.newCondition();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public MatchMakerService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository) {
        super(km, unmatchedPathRepository);
    }

    public Future<UnmatchedPathDto> requestMatchAsync(UnmatchedPathDto newRequest) {
        lock.lock();
        try {
            waitingList.add(newRequest);
            partnerAvailable.signalAll();
        } finally {
            lock.unlock();
        }

        return executor.submit(() -> findPartner(newRequest));
    }

    private UnmatchedPathDto findPartner(UnmatchedPathDto newRequest) throws InterruptedException{
        lock.lock();

        try {
            if (matchingTable.containsKey(newRequest)) return matchingTable.get(newRequest);

            while (true) {
                List<UnmatchedPathDto> waitingListExceptMe = new ArrayList<>(waitingList);
                waitingListExceptMe.remove(newRequest);

                if (waitingListExceptMe.isEmpty()) {

                    //lock을 반납하고 sleep
                    partnerAvailable.await();

                    if (matchingTable.containsKey(newRequest)) return matchingTable.get(newRequest);
                    continue;
                }



                UnmatchedPathDto partner = null;
                int shortestDestTime = Integer.MAX_VALUE;

                for (UnmatchedPathDto candidate : waitingListExceptMe) {

                    //출발지 끼리 이동시간
                    int durationBetweenInits = getDuration(newRequest.getInitPoint(), candidate.getInitPoint());

                    if (durationBetweenInits <= 600) {

                        int durationBetweenDests = getDuration(newRequest.getDestinationPoint(), candidate.getDestinationPoint());

                        if (durationBetweenDests < shortestDestTime) {
                            shortestDestTime = durationBetweenDests;
                            partner = candidate;
                        }
                    }
                }

                if (partner != null) {
                    matchingTable.put(newRequest, partner);
                    matchingTable.put(partner, newRequest);

                    waitingList.remove(newRequest);
                    waitingList.remove(partner);

                    partnerAvailable.signalAll();

                    return partner;
                } else {
                    partnerAvailable.await();

                    if(matchingTable.containsKey(newRequest)) return matchingTable.get(newRequest);
                }

            }

        } finally {
            lock.unlock();
        }
    }



}
