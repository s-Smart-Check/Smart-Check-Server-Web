var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/images', function (message) {
            var base64Image = message.body;
            showImage(base64Image);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function saveAttend(){
    $.ajax({
        url: "/attendances",
        type: "POST",
        success: function(response) {
            console.log(response);
            // 성공적으로 출석 처리되었을 때 추가 동작을 수행할 수 있습니다.
        },
        error: function(xhr, status, error) {
            console.error(error);
            // 출석 처리 중에 오류가 발생했을 때 처리할 내용을 여기에 작성합니다.
        }
    });
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showImage(base64Image) {
    // 이미지 엘리먼트 생성
    var imageElement = document.createElement('img');

    // Base64 이미지 데이터 설정
    imageElement.src = 'data:image/png;base64,' + base64Image;

    // 이미지를 표시할 컨테이너 엘리먼트
    var imageContainer = document.getElementById('image-container');

    // 기존에 이미지가 있다면 삭제
    if (imageContainer.firstChild) {
        imageContainer.removeChild(imageContainer.firstChild);
    }

    // 이미지 컨테이너에 이미지 엘리먼트 추가
    imageContainer.appendChild(imageElement);
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});