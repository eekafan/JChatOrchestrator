       $(document).ready(function() {

            // Namespace here needs to match the one used in server.py
            servletMapping = '/chat';
            // Connect via Flask SocketIO
            var socket = io.connect(location.protocol + '//' + document.domain + ':' + location.port + servletMapping);

            // Display message from the WOS bot
            socket.on('my_response', function(msg) {
                displayMessage(msg.data, 'Bot');
                displayImage(msg.image);
            });

            // Send message to the WOS bot
            $('form#emit').submit(function(event) {
                socket.emit('my_event', {data: $('#emit_data').val()});
                // Display sent from user on the right side
                displayMessage($('#emit_data').val(), 'NotWatsonBot');
                $("#emit")[0].reset();
                return false;
            });
         });