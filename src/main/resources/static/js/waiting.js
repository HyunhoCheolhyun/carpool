const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);
const container = document.getElementById('map');

stompClient.connect({}, function(frame) {

    console.log('Connected: ' + frame);
    // 승객 두명 모두 결제완료
    stompClient.subscribe('/user/queue/payment-completion', function(message) {
        console.log('matchedPath: ' + message.body);

        window.location.href = window.location.origin + '/match-passenger/' + message.body;
    });

});

// 에러 처리
socket.onerror = (event) => {
    console.error('Error occurred:', event);
};

// 연결 종료 처리
socket.onclose = () => {
    console.log('Connection closed');
};




