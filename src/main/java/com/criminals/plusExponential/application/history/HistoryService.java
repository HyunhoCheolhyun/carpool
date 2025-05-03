package com.criminals.plusExponential.application.history;





import com.criminals.plusExponential.application.dto.history.PassengerHistoryResponseDto;
import com.criminals.plusExponential.domain.entity.PrivateMatchedPath;
import com.criminals.plusExponential.domain.entity.User;
import com.criminals.plusExponential.infrastructure.persistence.PrivateMatchedPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PrivateMatchedPathRepository privateMatchedPathRepository;


    public List<PassengerHistoryResponseDto> getPassengerHistory(User user) {

        List<PassengerHistoryResponseDto> historyDtoList = new ArrayList<>();
        List<PrivateMatchedPath> privateMatchedPaths = privateMatchedPathRepository.findByUserOrderByCreatedAtDesc(user);

        for (PrivateMatchedPath privateMatchedPath : privateMatchedPaths) {
            LocalDateTime createdAt = privateMatchedPath.getCreatedAt();
            int price = privateMatchedPath.getFare().getTotal();
            int savedAmount = privateMatchedPath.getSavedAmount();
            int duration = privateMatchedPath.getDuration();
            int distance = privateMatchedPath.getDistance();

            historyDtoList.add(new PassengerHistoryResponseDto(createdAt, price, savedAmount, duration, distance));
        }

        return historyDtoList;
    }

}
