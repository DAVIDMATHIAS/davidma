
var connection = {};
var username = "";

var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var paint;

var imagewidth = 400;
var imageheight = 400;

function connect() {
    if (window.WebSocket != undefined || !connection) {
        if (connection.readyState === undefined || connection.readyState > 1) {
            connection = new WebSocket('ws://localhost:8080/ws/WSServlet');
            connection.binaryType = "arraybuffer";
            connection.onopen = onopen;
            connection.onmessage = onmessage;
            connection.onclose = onclose;
            connection.onerror = onerror;
        }
    }
}

function sendmessage() {
    var msg = $("#msgTxt").val();
    if (msg.length > 0) {
        connection.send(msg);
        console.log("Message send");
    }
}


function onopen(event) {
    console.log("opened");
}

var bytearray = null;
var imageData = null;
var actualData = null;
var dataURL = null;
function onmessage(event) {
    // console.log(event);


    if (event.data instanceof ArrayBuffer) {
        actualData = event.data;
        if (actualData) {
            var img = document.getElementById("img1");
            var jpeg = new Blob([actualData], { type: "image/jpeg" });
            var url = window.URL.createObjectURL(jpeg);
            img.src = url;
            console.log(url);
        } else {
            console.log("Nothing received");
        }

    } else {
        $("#mainChatTab").append("<br>" + event.data);
    }

}

function onclose(event) {
    console.log(event);
}

function onerror(event) {
    console.log("ERROR " + event);

}
var data = null;
var newblob = null;

function onload() {
    console.log("Onload");
    connect();
    navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia;
    var canvas = $("#canvas");
    var ctx = canvas.get()[0].getContext('2d');
    if (navigator.getUserMedia) {
        navigator.getUserMedia({ audio: true, video: true }, function (stream) {
            var video = $("#live").get()[0];
            if (navigator.webkitGetUserMedia) {
                console.log("Streaming");
                video.src = window.webkitURL.createObjectURL(stream);
            } else {
                video.src = stream; // Opera
            }
            timer = setInterval(
            function () {
                ctx.drawImage(video, 0, 0, 400, 400);
                data = canvas.get()[0].toDataURL('image/jpeg', 1.0);
                newblob = dataURItoBlob(data);
                connection.send(newblob);
            }, 250);
            gStream = stream;
        }, onFailSoHard);
    } else {
        video.src = 'somevideo.webm'; // fallback.
        console.log("No Camera");
    }
    $("#btnSend").on("click", sendmessage);

    function onFailSoHard() {
        console.log("Streaming failed");
    }

    $("#menuList>li>a").on("click", tabSelectHandler);

}

function selectTab(userName) {
    $("#menuList > li").each(function (index, item) {
        var a = $(item).children()[0];
        if ($(a).html() == userName) {
            $(item).attr("class", "active");
            //  console.log(a.href);
            window.location = a.href;
            return;
        } else {
            $(item).removeClass("active");
        }
    });
}
function addUser(userName) {
    $("<li>").html(userName).on("click", pingUser).appendTo($("#online"));
}
function pingUser() {
    var userName = $(this).html();
    addTab(userName);
}
function tabSelectHandler() {
    var userName = $(this).html();
    selectTab(userName);
}
function addTab(userName) {
    var exists = false;
    $("#menuList > li").each(function (index, item) {
        var a = $(item).children()[0];
        if ($(a).html() == userName) {
            selectTab(userName);
            exists = true;
            console.log("Tab already exists");
            return;
        }else{
            console.log($(a).html()+" is not user");
        }

    });
    if (!exists) {
        $("<div>").attr("id", userName).html(userName).appendTo($(".tabInner")[0]);
        var link = $("<a>").attr("href", "#" + userName).html(userName).on("click", function () { selectTab(userName); });
        var menu = $("<li>").appendTo($("#menuList"));
        link.appendTo(menu);
        selectTab(userName);
        console.log("added");
    }
}
function closeconnection() {
    connection.close();
}

function dataURItoBlob(dataURI) {
    var binary = atob(dataURI.split(',')[1]);
    var array = [];
    for (var i = 0; i < binary.length; i++) {
        array.push(binary.charCodeAt(i));
    }
    return new Blob([new Uint8Array(array)], { type: 'image/jpeg' });
}



