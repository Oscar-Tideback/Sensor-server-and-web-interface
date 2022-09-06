let ws;
let host = document.location.host;
let pathname = document.location.pathname;
let first = true;

ws = new WebSocket("ws://" + host + pathname + "sensor/");

ws.onmessage = function(event) {
    if(first === true){
        let log = document.getElementById("log");
        let message = JSON.parse(event.data);
        for (let i = 0; i < 16; i++) {
            log.innerHTML += " " + message.allData[i]['timestamp'] + "  "
                + " Temperature:  "  + message.allData[i]['payload'] + "<br/>";
        }
        first = false;
    }
    let temperature_log = document.getElementById("temperature_log");
    //var humidity_log = document.getElementById("humidity_log");
    //console.log(event.data);
    let message = JSON.parse(event.data);
    temperature_log.innerHTML = "<h2>" + "Temp: " + message.content + "</h2>";
    //humidity_log.innerHTML = "<h1 align='center'>" + message.content + "</h1>";
};

function test(str){
    ws.send(str);
}