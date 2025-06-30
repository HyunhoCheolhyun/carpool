package com.criminals.plusExponential.infrastructure.socket;

import com.criminals.plusExponential.application.dto.MatchedPathDto;
import com.criminals.plusExponential.application.dto.kakao.PaymentResponseDto;
import com.criminals.plusExponential.common.exception.customex.ErrorCode;
import com.criminals.plusExponential.common.exception.customex.SocketDisconnectedException;
import com.criminals.plusExponential.domain.entity.MatchedPath;
import com.criminals.plusExponential.domain.entity.Role;
import com.criminals.plusExponential.infrastructure.redis.RedisSocketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketPassengerService {
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisSocketRepository redisSocketRepository;

    /**
     * 파트너 매칭 완료
     * @param userId
     * @param matchedPath
     */
    public void sendMatchingCompleted(Long userId, MatchedPathDto matchedPath) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String socketId = Optional.ofNullable(redisSocketRepository.getSocketId(userId))
                .orElseThrow(() -> new SocketDisconnectedException(ErrorCode.SocketDisconnectedException));

        headerAccessor.setSessionId(socketId);
        headerAccessor.setLeaveMutable(true);


        // 클라이언트에게 소켓 전송
        messagingTemplate.convertAndSendToUser(
                socketId,
                "/queue/partner",
                matchedPath,
                headerAccessor.getMessageHeaders()
        );
    }


    /**
     * 결제완료
     */
    public void completePayment(Long userId, Long pmId){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String socketId = Optional.ofNullable(redisSocketRepository.getSocketId(userId))
                .orElseThrow(() -> new SocketDisconnectedException(ErrorCode.SocketDisconnectedException));

        headerAccessor.setSessionId(socketId);
        headerAccessor.setLeaveMutable(true);

        messagingTemplate.convertAndSendToUser(
                socketId,
                "/queue/payment-completion",
                pmId,
                headerAccessor.getMessageHeaders()
        );
    }

    /**
     * 결제URL 전송
     */
    public void sendPaymentURL(Long userId, PaymentResponseDto paymentResponseDto) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String socketId = Optional.ofNullable(redisSocketRepository.getSocketId(userId))
                .orElseThrow(() -> new SocketDisconnectedException(ErrorCode.SocketDisconnectedException));

        headerAccessor.setSessionId(socketId);
        headerAccessor.setLeaveMutable(true);

        messagingTemplate.convertAndSendToUser(
                socketId,
                "/queue/payment",
                paymentResponseDto.getNext_redirect_pc_url(),
                headerAccessor.getMessageHeaders()
        );
    }

    /**
     * 결제 실패
     */
    public void failPaymentURL(Long userId, PaymentResponseDto paymentResponseDto) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        String socketId = Optional.ofNullable(redisSocketRepository.getSocketId(userId))
                .orElseThrow(() -> new SocketDisconnectedException(ErrorCode.SocketDisconnectedException));

        headerAccessor.setSessionId(socketId);
        headerAccessor.setLeaveMutable(true);

        messagingTemplate.convertAndSendToUser(
                socketId,
                "/queue/payment-fail",
                paymentResponseDto,
                headerAccessor.getMessageHeaders()
        );
    }

}
