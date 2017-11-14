var connection;
$(function(){
    connection = new
    WebSocket('ws://localhost:8080/llws');

    connection.onopen = function () {
        console.log('Connected...');
    };
    connection.onmessage = function(event){
        console.log('>>>>> ' + event.data);
        var json = JSON.parse(event.data);
        $("#output").append("<div></div>")
            .addClass("col-lg-8").append(
                "<span><strong>"
            + json.user
            + "</strong>: <em>"
            + json.message
            + "</em></span><br/>");
    };
    connection.onclose = function(event){
        $("#output").append("CONNECTION: CLOSED");
    };

});
function sendMessage(){
    var message = {}
    // message["user"] = $("#user").val();
    message["message"] = $("#message").val();
    connection.send(JSON.stringify(message));
};