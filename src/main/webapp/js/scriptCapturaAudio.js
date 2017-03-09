var start = $('#start');
var stop = $('#stop');

start.click(captureUserMedia);
stop.click(detenerGranacion);

var mediaConstraints = {
	audio: true
};

function captureUserMedia() {
	navigator.mediaDevices.getUserMedia(mediaConstraints).then(onMediaSuccess).catch(onMediaError);
}

var mediaRecorder;
var blobFile;
function onMediaSuccess(stream) {
    mediaRecorder = new MediaStreamRecorder(stream);
    mediaRecorder.stream = stream;
    mediaRecorder.recorderType = StereoAudioRecorder;
    mediaRecorder.mimeType = 'audio/wav';
    
    // don't force any mimeType; use above "recorderType" instead.
    // mediaRecorder.mimeType = 'audio/webm'; // audio/ogg or audio/wav or audio/webm

    mediaRecorder.audioChannels = 1
    mediaRecorder.ondataavailable = function(blob) {
    	//en esta parte se puedo coger el archivo blob
    	blobFile = blob;
    };

    var timeInterval = 60 * 1000;

    // get blob after specific time interval
    mediaRecorder.start(timeInterval);
}

function onMediaError(e) {
    console.error('media error', e);
}

function detenerGranacion(){
	mediaRecorder.stop();
    mediaRecorder.stream.stop();
}

function uploadFile(){
	var formDat = new FormData();
	
	formDat.append("userfile",blobFile);
	
	$.ajax({
        type: 'POST',
        url: 'ControladorServlet',
        data: formDat,
        processData: false,
        contentType: false
    }).done(function(data) {
        console.log(data);
    });
}