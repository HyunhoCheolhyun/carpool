const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    // 이 시점에서 서버의 handleSessionConnected 메소드가 실행됨

    stompClient.subscribe('/user/queue/messages', function(message) {
        console.log('Received: ' + message.body);
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