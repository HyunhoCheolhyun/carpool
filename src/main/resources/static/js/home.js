const originAddress= document.getElementById('originAddress')
const destinationAddress = document.getElementById('destinationAddress')
const placesListBox = document.getElementById('placesList')
const paginationBox = document.getElementById('pagination')
const matchingButton = document.getElementById('matching')
const createRouteButton = document.getElementById('create-route')
const buffering = document.querySelector('.buffering')
const socket = new SockJS('/ws-stomp');
const stompClient = Stomp.over(socket);
const container = document.getElementById('map');
var map;
var desMarker
var originMarker
let previousOriginInfo = null
let previousDesInfo = null


let unmatchedPathDto = {
    initPoint: {
        lat: null,
        lng: null
    },
    destinationPoint: {
        lat: null,
        lng: null
    },
};

window.onload = function() {
    initMap();
    initSocket();
};

/**
 * 경로 설정
 */
createRouteButton.addEventListener('click', async function () {
    createRouteButton.style.backgroundColor = 'rgba(128, 128, 128, 0.5)'

    const response = await fetch('/unmatched-path', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(unmatchedPathDto)
    })

    if(!response.ok){
        const data =  await response.json()
        alert(data.message.message)
    }
    else{
        alert("✅ 경로 설정 완료!")
    }
})

/**
 * 매칭 하기
 */
matchingButton.addEventListener('click', async function () {
    matchingButton.disabled = true
    buffering.style.display = 'inline'
    matchingButton.style.backgroundColor = 'rgba(128, 128, 128, 0.5)'

    const response = await fetch('/unmatched-path/init-matching', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })

    if(!response.ok){
        const data =  await response.json()
        alert(data.message.message)
    }
    else{
        alert("✅ 매칭완료!")
    }


    buffering.style.display = "none"
    matchingButton.disabled = false
})



/**
 * 출발지 설정
 */
function setInitPoint(lat,lng){
    unmatchedPathDto.initPoint.lat = lat;
    unmatchedPathDto.initPoint.lng = lng;
    alert("출발지 설정완료")
}



/**
 * 도착지 설정
 */
function setDestinationPoint(lat,lng){
    unmatchedPathDto.destinationPoint.lat = lat;
    unmatchedPathDto.destinationPoint.lng = lng;
    alert("도착지 설정완료")
}

/**
 * 우클릭으로 도착지 설정
 */
let isProcessing = false
let debounceTimer



//위 displayMarker에서 지도 지도 중심을 바꾸는 map.setCenter만 제외
function displayMarker2(locPosition, message) {
    removeMarkerForOrigin()
    if (previousOriginInfo) {
        previousOriginInfo.close()
    }
    // 마커를 생성합니다
    var marker = new kakao.maps.Marker({
        map: map,
        position: locPosition,
    })

    markersForOrigin.push(marker)

    var iwContent = message, // 인포윈도우에 표시할 내용
        iwRemoveable = true

    // 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable,
    })

    // 인포윈도우를 마커위에 표시합니다
    infowindow.open(map, marker)

    previousOriginInfo = infowindow
}




/**
 * 출발지, 목적지 검색
 */
originAddress.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        const value = originAddress.value.trim()
        if(value){
            searchPlace(value,"origin");
        }
    }
});

destinationAddress.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        const value = destinationAddress.value.trim()
        if(value){
            searchPlace(value,"destination");
        }
         // 도착지 인풋에서 엔터
    }
});


function searchPlace(searchWord,type) {
    var markers = []
    console.log(searchWord)
    var geocoder = new kakao.maps.services.Geocoder()
    geocoder.addressSearch(
        JSON.stringify(searchWord),
        function (result, status) {
            // 정상적으로 검색이 완료됐으면
            if (status === kakao.maps.services.Status.OK) {
                var coords = new kakao.maps.LatLng(result[0].y, result[0].x)

                // 결과값으로 받은 위치를 마커로 표시합니다
                var marker = new kakao.maps.Marker({
                    map: map,
                    position: coords,
                })

                // 인포윈도우로 장소에 대한 설명을 표시합니다
                var infowindow = new kakao.maps.InfoWindow({
                    content:
                        '<div style="width:150px;text-align:center;padding:6px 0;">출발지</div>',
                })
                infowindow.open(map, marker)

                // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
                map.setCenter(coords)
                const originPoint = {
                    lat: result[0].y,
                    lng: result[0].x,
                }
            } else {
                var ps = new kakao.maps.services.Places()

                var infowindow = new kakao.maps.InfoWindow({ zIndex: 1 })

                ps.keywordSearch(searchWord, placesSearchCB2)


                // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
                function placesSearchCB2(data, status, pagination) {
                    if (status === kakao.maps.services.Status.OK) {
                        displayPlaces2(data)
                        displayPagination2(pagination)
                    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                        alert('검색 결과가 존재하지 않습니다.')
                    } else if (status === kakao.maps.services.Status.ERROR) {
                        alert('검색 결과 중 오류가 발생했습니다.')
                    }
                }


                // 검색 결과 목록과 마커를 표출하는 함수입니다
                function displayPlaces2(places) {
                    var listEl = document.getElementById('placesList'),
                        menuEl = document.getElementById('menu_wrap'),
                        fragment = document.createDocumentFragment(),
                        bounds = new kakao.maps.LatLngBounds(),
                        listStr = ''

                    // 검색 결과 목록에 추가된 항목들을 제거합니다
                    removeAllChildNods(listEl)

                    // 지도에 표시되고 있는 마커를 제거합니다
                    removeMarker()

                    for (var i = 0; i < places.length; i++) {
                        // 마커를 생성하고 지도에 표시합니다
                        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                            marker = addMarker(placePosition, i),
                            itemEl = getListItem2(i, places[i],{lat: places[i].y, lng : places[i].x}) // 검색 결과 항목 Element를 생성합니다

                        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                        // LatLngBounds 객체에 좌표를 추가합니다
                        bounds.extend(placePosition)

                        // 마커와 검색결과 항목에 mouseover 했을때
                        // 해당 장소에 인포윈도우에 장소명을 표시합니다
                        // mouseout 했을 때는 인포윈도우를 닫습니다
                        ;(function (marker, title) {
                            kakao.maps.event.addListener(marker, 'mouseover', function () {
                                displayInfowindow(marker, title)
                            })

                            kakao.maps.event.addListener(marker, 'mouseout', function () {
                                infowindow.close()
                            })

                            itemEl.onmouseover = function () {
                                displayInfowindow(marker, title)
                            }

                            itemEl.onmouseout = function () {
                                infowindow.close()
                            }
                        })(marker, places[i].place_name)

                        fragment.appendChild(itemEl)
                    }

                    // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
                    listEl.appendChild(fragment)
                    menuEl.scrollTop = 0

                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                    map.setBounds(bounds)
                }

                // 검색결과 항목을 Element로 반환하는 함수입니다
                function getListItem2(index, places ,position) {
                    var el = document.createElement('li'),
                        itemStr =
                            '<span class="markerbg marker_' +
                            (index + 1) +
                            '"></span>' +
                            '<div class="info">' +
                            '   <h5>' +
                            places.place_name +
                            '</h5>'

                    if (places.road_address_name && type === "origin") {
                        itemStr +=
                            '    <span class="road">' +
                            places.road_address_name +
                            '</span>' +
                            '   <button id = "startButton">출발</button>' +
                            '   <span class="jibun gray">' +
                            places.address_name +
                            '</span>'
                    }
                    else if (places.road_address_name && type === "destination") {
                        itemStr +=
                            '    <span class="road">' +
                            places.road_address_name +
                            '</span>' +
                            '   <button id = "startButton">도착</button>' +
                            '   <span class="jibun gray">' +
                            places.address_name +
                            '</span>'
                    }
                    else {
                        itemStr += '    <span>' + places.address_name + '</span>'
                    }

                    itemStr +=
                        '  <span class="tel">' + places.phone + '</span>' + '</div>'

                    el.innerHTML = itemStr
                    el.className = 'item'

                    var startButton = el.querySelector('#startButton')
                    if (startButton) {
                        if (type === "origin") {
                            startButton.addEventListener('click', (event) => handleButtonClick2(event,position));
                        } else if (type === "destination") {
                            startButton.addEventListener('click', (event) => handleButtonClick(event,position));
                        }
                    }
                    return el
                }

                // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
                function addMarker(position, idx, title) {
                    var imageSrc =
                            'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
                        imageSize = new kakao.maps.Size(36, 37), // 마커 이미지의 크기
                        imgOptions = {
                            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
                            spriteOrigin: new kakao.maps.Point(0, idx * 46 + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                            offset: new kakao.maps.Point(13, 37), // 마커 좌표에 일치시킬 이미지 내에서의 좌표
                        },
                        markerImage = new kakao.maps.MarkerImage(
                            imageSrc,
                            imageSize,
                            imgOptions,
                        ),
                        marker = new kakao.maps.Marker({
                            position: position, // 마커의 위치
                            image: markerImage,
                            clickable: true,
                        })

                    marker.setMap(map) // 지도 위에 마커를 표출합니다
                    markers.push(marker) // 배열에 생성된 마커를 추가합니다

                    return marker
                }

                // 지도 위에 표시되고 있는 마커를 모두 제거합니다
                function removeMarker() {
                    for (var i = 0; i < markers.length; i++) {
                        markers[i].setMap(null)
                    }
                    markers = []
                }

                // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
                function displayPagination2(pagination) {
                    var paginationEl = document.getElementById('pagination'),
                        fragment = document.createDocumentFragment(),
                        i

                    // 기존에 추가된 페이지번호를 삭제합니다
                    while (paginationEl.hasChildNodes()) {
                        paginationEl.removeChild(paginationEl.lastChild)
                    }

                    for (i = 1; i <= pagination.last; i++) {
                        var el = document.createElement('a')
                        el.href = '#'
                        el.innerHTML = i

                        if (i === pagination.current) {
                            el.className = 'on'
                        } else {
                            el.onclick = (function (i) {
                                return function () {
                                    pagination.gotoPage(i)
                                }
                            })(i)
                        }

                        fragment.appendChild(el)
                    }
                    paginationEl.appendChild(fragment)
                }

                // 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
                // 인포윈도우에 장소명을 표시합니다
                function displayInfowindow(marker, title) {
                    var content =
                        '<div style="padding:5px;z-index:1;">' + title + '</div>'

                    infowindow.setContent(content)
                    infowindow.open(map, marker)
                }

                // 검색결과 목록의 자식 Element를 제거하는 함수입니다
                function removeAllChildNods(el) {
                    while (el.hasChildNodes()) {
                        el.removeChild(el.lastChild)
                    }
                }
            }
        },
    )


}

function initSocket(){
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
}


function initMap(){
    kakao.maps.load(async function() {
        // 지도 생성

        const currentPositon = await getCurrentPosition();
        var options = {
            center: new kakao.maps.LatLng(currentPositon.lat, currentPositon.lng),
            level: 3
        };
        map = new kakao.maps.Map(container, options);

        // 마커 생성
        // 마커 이미지의 주소입니다
        var imageSrc =
            'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png' // Kakao 내비게이션 스타일의 아이콘 URL로 변경
        var imageSize = new kakao.maps.Size(64, 69) // 마커 이미지의 크기
        var imageOption = { offset: new kakao.maps.Point(27, 69) } // 마커 이미지의 옵션, 마커의 좌표와 일치시킬 이미지 안의 좌표 설정

        // 마커 이미지를 생성합니다
        var markerImage = new kakao.maps.MarkerImage(
            imageSrc,
            imageSize,
            imageOption,
        )

        // 마커가 표시될 위치입니다
        var markerPosition = new kakao.maps.LatLng(currentPositon.lat, currentPositon.lng)

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            position: markerPosition,
            image: markerImage, // 마커 이미지 설정
        })

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map)

        kakao.maps.event.addListener(map, 'rightclick', function (mouseEvent) {
            if (isProcessing) {
                return
            }

            isProcessing = true
            clearTimeout(debounceTimer)

            var latlng = mouseEvent.latLng
            var message = '목적지'

            setDestinationPoint(latlng.getLat(),latlng.getLng())
            displayMarker2(latlng,message)

            destinationAddress.placeholder = '✅ 목적지 설정 완료!'
            destinationAddress.value = '✅ 목적지 설정 완료!'

            debounceTimer = setTimeout(() => {
                isProcessing = false
            }, 500)
        })

        /**
         * 클릭으로 출발지 설정
         */
        kakao.maps.event.addListener(map, 'dblclick', function (mouseEvent) {
            var latlng = mouseEvent.latLng
            var message = '출발지'

            setInitPoint(latlng.getLat(), latlng.getLng())
            displayMarker(latlng,message)

            originAddress.placeholder = '✅ 출발지 설정 완료!'
            originAddress.value = '✅ 출발지 설정 완료!'
        })
    });
}

function displayMarker(locPosition, message) {
    if(originMarker){
        originMarker.setMap(null)
    }
    if (previousOriginInfo) {
        previousOriginInfo.close()
    }

    originMarker = new kakao.maps.Marker({
        map: map,
        position: locPosition,
    })

    var iwContent = message, // 인포윈도우에 표시할 내용
        iwRemoveable = true

    // 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable,
    })

    // 인포윈도우를 마커위에 표시합니다
    infowindow.open(map, originMarker)

    previousOriginInfo = infowindow
}

function displayMarker2(locPosition, message) {
    if(desMarker){
        desMarker.setMap(null)
    }
    if (previousDesInfo) {
        previousDesInfo.close()
    }

    desMarker = new kakao.maps.Marker({
        map: map,
        position: locPosition,
    })

    var iwContent = message, // 인포윈도우에 표시할 내용
        iwRemoveable = true

    // 인포윈도우를 생성합니다
    var infowindow = new kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable,
    })

    // 인포윈도우를 마커위에 표시합니다
    infowindow.open(map, desMarker)

    previousDesInfo = infowindow
}




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

function handleButtonClick(event,position) {
    // 클릭된 버튼의 부모 요소인 리스트 아이템을 찾습니다
    var listItem = event.target.closest('.item')
    // 리스트 아이템에서 주소 정보를 찾습니다
    var addressSpan = listItem.querySelector('.road')
    var destinationAddressInput = document.getElementById('destinationAddress')
    placesListBox.innerHTML = ''
    paginationBox.innerText = ''
    // 주소 정보가 있는 경우에만 처리합니다
    if (addressSpan) {
        // 주소 정보를 가져와서 인풋박스에 설정합니다
        destinationAddressInput.value = addressSpan.textContent
    }
    if(position.lat && position.lng){
        setDestinationPoint(position.lat,position.lng)
    }
}

function handleButtonClick2(event,position) {
    // 클릭된 버튼의 부모 요소인 리스트 아이템을 찾습니다
    var listItem = event.target.closest('.item')
    // 리스트 아이템에서 주소 정보를 찾습니다
    var addressSpan = listItem.querySelector('.road')
    var originAddressInput = document.getElementById('originAddress')
    placesListBox.innerHTML = ''
    paginationBox.innerText = ''
    // 주소 정보가 있는 경우에만 처리합니다
    if (addressSpan) {
        // 주소 정보를 가져와서 인풋박스에 설정합니다
        originAddressInput.value = addressSpan.textContent
    }
    if(position.lat && position.lng){
        setInitPoint(position.lat,position.lng)
    }
}
