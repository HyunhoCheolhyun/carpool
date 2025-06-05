const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);
let intervalId;

document.addEventListener("DOMContentLoaded", function () {
    kakao.maps.load(function () {
        const container = document.getElementById('map');
        const options = {
            center: new kakao.maps.LatLng(initLat, initLng),
            level: 10
        };
        const map = new kakao.maps.Map(container, options);

        const locations = [
            {
                title: '출발지',
                lat: initLat,
                lng: initLng
            },
            {
                title: '제1 경유지',
                lat: firstWayPointLat,
                lng: firstWayPointLng
            },
            {
                title: '제2 경유지',
                lat: secondWayPointLat,
                lng: secondWayPointLng
            },
            {
                title: '도착지',
                lat: destinationLat,
                lng: destinationLng
            }
        ];


        const path = [];

        locations.forEach(loc => {
            const position = new kakao.maps.LatLng(loc.lat, loc.lng);
            path.push(position); // 경로 좌표로 저장

            const marker = new kakao.maps.Marker({
                map: map,
                position: position
            });

            const infowindow = new kakao.maps.InfoWindow({
                content: `<div style="padding:5px;">${loc.title}</div>`
            });

            infowindow.open(map, marker);
        });

        // 🟨 경로선 추가
        const polyline = new kakao.maps.Polyline({
            path: path, // 선을 구성하는 좌표배열
            strokeWeight: 4, // 선 두께
            strokeColor: '#007bff', // 선 색상
            strokeOpacity: 0.8, // 선 투명도
            strokeStyle: 'solid' // 선 스타일 (solid, dashed 등)
        });

        polyline.setMap(map);
    });
});


stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    // 이 시점에서 서버의 handleSessionConnected 메소드가 실행됨

    /**
     * 서버에 반복적으로 택시기사 위치 전송
     */
    intervalId = setInterval(async () => {
        const pos = await getCurrentPosition();

        stompClient.send("/app/location", {},
            JSON.stringify(pos)
        );
    }, 7000);

});

// 에러 처리
socket.onerror = (event) => {
    console.error('Error occurred:', event);
};

// 연결 종료 처리
socket.onclose = () => {
    console.log('Connection closed');
};

/**
 * 현재위치가져오기
 * @returns 현재좌표
 */
async function getCurrentPosition() {
    if (!navigator.geolocation) {
        throw new Error('Geolocation이 이 브라우저에서 지원되지 않습니다.');
    }

    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                resolve({lat: position.coords.latitude, lng: position.coords.longitude});
            },
            function (error) {
                reject(new Error('위치 정보를 가져오는 데 실패했습니다: ' + error.message));
            },
            {
                enableHighAccuracy: true, // 높은 정확도 요청
                timeout: 5000,           // 최대 5초 대기
                maximumAge: 0            // 캐시된 위치 사용하지 않음
            }
        );
    });
}