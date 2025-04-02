const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);
stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    // 이 시점에서 서버의 handleSessionConnected 메소드가 실행됨

    /**
     * 드라이버에게 배차 수락 여부
     */
    stompClient.subscribe('/user/queue/driver', async function(message) {
        const data = JSON.parse(message.body);
        const matchedPath = data.matchedPath;
        const availableTime = data.availableTime * 60;

        const currentPosition = await getCurrentPosition();
        const duration = await getResponse({origin:convertToOriginString(currentPosition),destination:convertToOriginString(matchedPath.initPoint)});

        console.log(duration)
        if(duration<availableTime){
            showModal(matchedPath)
        }
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

/**
 * 현재위치가져오기
 * @returns 현재좌표
 */
async function getCurrentPosition(){
    if (!navigator.geolocation) {
        throw new Error('Geolocation이 이 브라우저에서 지원되지 않습니다.');
    }

    return new Promise((resolve, reject) => {
        navigator.geolocation.getCurrentPosition(
            function (position) {
                resolve({lat:position.coords.latitude, lng:position.coords.longitude});
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

/**
 * 카카오 길찾기 API호출
 * @return 소요시간
 */
const getResponse = async (params) => {
    const baseUrl = "https://apis-navi.kakaomobility.com/v1/directions";

    const url = `${baseUrl}?origin=${params.origin}&destination=${params.destination}`;

    try {
        const response = await fetch(url, {
            method: "GET",
            headers: {
                "Authorization": `KakaoAK ${REST_API_KEY}` // HTML에서 전달받은 API_KEY 사용
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const responseJson =await response.json();
        console.log(responseJson)

        return responseJson.routes[0].summary.duration;
    } catch (error) {
        console.error("Error fetching directions:", error.message);
    }
};

function convertToOriginString(query) {
    if (!query.lat || !query.lng) {
        throw new Error("Invalid input: 'lat' and 'lng' are required.");
    }
    return `${query.lng},${query.lat}`;
}

/**
 * 택시기사 수락 모델창 띄우기
 * @param data
 */
function showModal(data) {
    // 모달 요소 가져오기
    const modal = document.getElementById("confirmationModal");

    // 데이터 반영 (예제 데이터 사용)
    document.getElementById("modalInitPoint").innerText = `출발지: 위도 ${data.initPoint.lat}, 경도 ${data.initPoint.lng}`;
    document.getElementById("modalDestinationPoint").innerText = `목적지: 위도 ${data.destinationPoint.lat}, 경도 ${data.destinationPoint.lng}`;
    document.getElementById("modalFare").innerText = `요금: ${data.fare}`;
    document.getElementById("modalDistance").innerText = `거리: ${data.distance}`;
    document.getElementById("modalDuration").innerText = `소요 시간: ${data.duration}`;

    // 모달 표시
    modal.style.display = "flex";

    // 배차 수락
    document.getElementById("acceptButton").onclick = () => {
        modal.style.display = "none"; // 모달 닫기
        handleAccept(data.id); // 수락 처리 함수 호출
    };

    // 배차 거절
    document.getElementById("cancelButton").onclick = () => {
        modal.style.display = "none"; // 모달 닫기
    };

    async function handleAccept(matchedPathId) {
        const baseUrl = window.location.origin + '/driver/accept/' + matchedPathId
        console.log(baseUrl);

        try {
            const response = await fetch(baseUrl, {
                method: "GET",
            });
            const data = await response.json();

            if (!response.ok) {
                alert(data.message.message);
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            window.location.href = window.location.origin + '/matching-driver'
        } catch (error) {
            console.error("Error fetching directions:", error.message);
        }
    }
}







