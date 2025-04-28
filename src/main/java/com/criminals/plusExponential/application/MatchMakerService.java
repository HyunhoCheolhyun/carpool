package com.criminals.plusExponential.application;

import com.criminals.plusExponential.application.dto.UnmatchedPathDto;
import com.criminals.plusExponential.infrastructure.kakao.KakaoMobilityClient;
import com.criminals.plusExponential.infrastructure.persistence.UnmatchedPathRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class MatchMakerService extends PathService{
    private final List<UnmatchedPathDto> waitingList = new ArrayList<>();
    private final Map<UnmatchedPathDto, UnmatchedPathDto> matchingTable = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition partnerAvailable = lock.newCondition();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private final MatchedPathService matchedPathService;

    public MatchMakerService(KakaoMobilityClient km, UnmatchedPathRepository unmatchedPathRepository, MatchedPathService matchedPathService) {
        super(km, unmatchedPathRepository);
        this.matchedPathService = matchedPathService;
    }

    public void receiveAndSendMessage(UnmatchedPathDto newRequest) throws ExecutionException, InterruptedException {
        UnmatchedPathDto partner = initMatching(newRequest);
        matchedPathService.receiveMessage(newRequest, partner);
    }

    public UnmatchedPathDto initMatching(UnmatchedPathDto newRequest) throws ExecutionException, InterruptedException {
        Future<UnmatchedPathDto> unmatchedPathDtoFuture = requestMatchAsync(newRequest);

        UnmatchedPathDto partner = unmatchedPathDtoFuture.get();
        return partner;
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
                    int durationBetweenInits = getSummary(newRequest.getInitPoint(), candidate.getInitPoint()).duration;

                    if (durationBetweenInits <= 600) {

                        int durationBetweenDests = getSummary(newRequest.getDestinationPoint(), candidate.getDestinationPoint()).duration;

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
