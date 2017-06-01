var stompClient = null;

function setConnected(connected) {
    document.getElementById("connect").disabled = connected;
    document.getElementById("disconnect").disabled = !connected;
    if (connected) {
        document.getElementById("conversation").style.display='';
    }
    else {
        document.getElementById("conversation").style.display='none';
    }
    document.getElementById("greetings").innerHTML = '';
}


function makeConnect() {
    console.log("connect().....");
    var socket = new SockJS('/socket/sample');
    var s = window.location.toString();
    var accessToken = s.substring(s.indexOf("?") + 1);
    console.log("will connect with access-token:" + accessToken);
    stompClient = Stomp.over(socket);
    stompClient.connect({"access-token": accessToken}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame + "\r\naccess-token=" + accessToken);
        stompClient.subscribe('/queue/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/user/queue/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}


function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    var s = document.getElementById("name").value;
    if(s.substring(0,3) == "all"){
        stompClient.send("/app/helloall", {}, JSON.stringify({'name': '<all>' + s + ""}));
    }else{
        stompClient.send("/app/hello", {}, JSON.stringify({'name': s }));
    }
}

function showGreeting(message) {
    document.getElementById("greetings").innerHTML += "<tr><td>" + message + "</td></tr>";
}

