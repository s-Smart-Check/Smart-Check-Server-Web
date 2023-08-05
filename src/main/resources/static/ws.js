var stompClient = null;
var breakTimeOn = false;
var timeoutId = null;
var toggleTime = 10 * 60 * 1000; // 10분을 밀리초로 변환하여 지정
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
        stompClient.subscribe('/topic/attendance', function (message) {
            var jsonDataList = JSON.parse(message.body);

            for (var i = 0; i < jsonDataList.length; i++) {
                var studentNum = jsonDataList[i].studentNum;
                var attendanceStatus = jsonDataList[i].attendance;
                updateAttendStatus(studentNum, attendanceStatus);


            }

        });
        send("startClass");
    });
}

function disconnect() {
    send("stopClass");
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function updateAttendStatus(studentNum, attendanceStatus){
    $('.student-container').each(function() {
        var studentIdElement = $(this).find('.student-id');
        var attendanceElement = $(this).find('.student-attendance');

        if (studentIdElement.text() === studentNum) {
            if(attendanceElement.text() !== attendanceStatus){
                attendanceElement.text(attendanceStatus);

                if (attendanceStatus === '출석') {
                    attendanceElement.css('color', 'green');
                    attStudentNum++;
                    abStudentNum--;
                }

                if(attendanceStatus === '결석') {
                    attendanceElement.css('color', 'red');
                    attStudentNum--;
                    abStudentNum++;
                }
                attRate = Math.floor(attStudentNum/sElements.length*100);


                $('#attNum').text(attStudentNum + " 명");
                $('#abNum').text(abStudentNum + " 명");
                $('#attRateDiv').text(attRate+"%");
                bar.style.width = `${attRate}%`;
            }

        }

    });
}

function saveAttend() {
    $.ajax({
        url: "/attendances",
        type: "POST",
        data: JSON.stringify({ classId: 1 }), // 데이터를 JSON 형식으로 변환
        contentType: "application/json;charset=UTF-8", // Content-Type을 JSON으로 설정
        success: function (response) {
            console.log(response);
            // 성공적으로 출석 처리되었을 때 추가 동작을 수행할 수 있습니다.
        },
        error: function (xhr, status, error) {
            console.error(error);
            // 출석 처리 중에 오류가 발생했을 때 처리할 내용을 여기에 작성합니다.
        }
    });
}

function send(control) {
    let endPoint = "/app/" + control;
    stompClient.send(endPoint, {}, JSON.stringify({'classId': 1}));
}

function modifyAttendance(jsonData){

}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() {
        connect();
        (this).className = "card bg-secondary text-white shadow";
        document.getElementById("disconnect").className = "card bg-danger text-white shadow"
        document.getElementById("BreakTime").className = "card bg-warning text-white shadow"
    });
    $( "#disconnect" ).click(function() {
        disconnect();
        saveAttend();
        document.getElementById("connect").className = "card bg-info text-white shadow"
        document.getElementById("disconnect").className = "card bg-secondary text-white shadow"
        document.getElementById("BreakTime").className = "card bg-secondary text-white shadow"
    });

    $("#BreakTime").click(function() {
        toggleBreakTime();
    });

    function toggleBreakTime() {
        if (!breakTimeOn) {
            // 쉬는시간 on
            disconnect();
            document.getElementById("BreakTime").className = "card bg-secondary text-white shadow";
            breakTimeOn = true;
            $("#BreakTime").prop("disabled", true);
            $("#connect").prop("disabled", true);

            timeoutId = setTimeout(function() {
                toggleBreakTime();
            }, toggleTime);
        } else {
            // 쉬는 시간 off
            connect();
            document.getElementById("BreakTime").className = "card bg-warning text-white shadow";
            breakTimeOn = false;
            $("#BreakTime").prop("disabled", false);
            clearTimeout(timeoutId);
        }

    }

});