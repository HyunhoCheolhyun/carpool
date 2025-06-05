let map;
let driverMarker;
let intervalId;
const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);
const container = document.getElementById('map');

window.onload = initMap;



function initMap() {
    kakao.maps.load(async function () {
        // 지도 생성
        var options = {
            center: new kakao.maps.LatLng(initLat, initLng),
            level: 3
        };
        map = new kakao.maps.Map(container, options);

        // 마커 이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(
            'https://cdn-icons-png.flaticon.com/128/1768/1768113.png',
            new kakao.maps.Size(30, 30),
            {offset: new kakao.maps.Point(27, 69)}, // 마커 이미지의 옵션, 마커의 좌표와 일치시킬 이미지 안의 좌표 설정
        )

        var markerPosition = new kakao.maps.LatLng(initLat,initLng)

        // 출발지 마커 표시
        var marker = new kakao.maps.Marker({
            position: markerPosition,
            image: markerImage, // 마커 이미지 설정
        })

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map)
    });
}

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);

    // 택시기사 위치정보 요청
    intervalId = setInterval(async () => {
        stompClient.send("/app/location/request", {}, JSON.stringify({ driverId: driverId }));

    }, 3000);

    // 택시기사 위치정보 수신
    stompClient.subscribe("/topic/location/"+driverId, function(message) {
        const coord = JSON.parse(message.body);

        console.log("위치" + coord.lat + coord.lng);
        const newPosition = new kakao.maps.LatLng(coord.lat, coord.lng);
        const driverMarkerImage = new kakao.maps.MarkerImage(
            'https://cdn-icons-png.flaticon.com/128/75/75780.png',
            new kakao.maps.Size(30, 30), // 마커 이미지의 크기,
            {offset: new kakao.maps.Point(27, 69)}
        )

        driverMarker = new kakao.maps.Marker({
            position: newPosition,
            image: driverMarkerImage, // 마커 이미지 설정
        })
        driverMarker.setMap(map)
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




