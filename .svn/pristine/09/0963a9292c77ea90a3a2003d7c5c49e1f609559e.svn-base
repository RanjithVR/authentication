/*
License (MIT)

Copyright © 2013 Matt Diamond
Modified by Srinivas Tamada http://www.9lessons.info

*/
var choosenFileDuration,choosenFileCurrentTime;
(function(window){
var WORKER_PATH = '../resources/assets/js/voicePannel/recorderWorker.js';
var encoderWorker = new Worker('../resources/assets/js/voicePannel/mp3Worker.js');
//console.log(encoderWorker);
var Recorder = function(source, cfg){
var config = cfg || {};
var bufferLen = config.bufferLen || 4096;
this.context = source.context;
this.node = (this.context.createScriptProcessor ||
this.context.createJavaScriptNode).call(this.context,
bufferLen, 2, 2);
var worker = new Worker(config.workerPath || WORKER_PATH);
worker.postMessage({
command: 'init',
config: {
sampleRate: this.context.sampleRate
}
});
var recording = false,
currCallback;

this.node.onaudioprocess = function(e){
if (!recording) return;
worker.postMessage({
command: 'record',
buffer: [
e.inputBuffer.getChannelData(0),
//e.inputBuffer.getChannelData(1)
]
});
}

this.configure = function(cfg){
for (var prop in cfg){
if (cfg.hasOwnProperty(prop)){
config[prop] = cfg[prop];
}
}
}

this.record = function(){
recording = true;
}

this.stop = function(){
recording = false;
}

this.clear = function(){
worker.postMessage({ command: 'clear' });
}

this.getBuffer = function(cb) {
currCallback = cb || config.callback;
worker.postMessage({ command: 'getBuffer' })
}

this.exportWAV = function(cb, type){
currCallback = cb || config.callback;
type = type || config.type || 'audio/mp3';
if (!currCallback) throw new Error('Callback not set');
worker.postMessage({
command: 'exportWAV',
type: type
});
}

//Mp3 conversion
worker.onmessage = function(e){
var blob = e.data;
//console.log(blob);
//console.log("the blob " +  blob + " " + blob.size + " " + blob.type);

var arrayBuffer;
var fileReader = new FileReader();

fileReader.onload = function(){
arrayBuffer = this.result;
//console.log(arrayBuffer);
var buffer = new Uint8Array(arrayBuffer),
data = parseWav(buffer);

//console.log(data);
console.log("Converting to Mp3");
//log.innerHTML += "\n" + "Converting to Mp3";
$("#loadStatus").fadeIn("slow");
encoderWorker.postMessage({ cmd: 'init', config:{
mode : 3,
channels:1,
samplerate: data.sampleRate,
bitrate: data.bitsPerSample
}});

encoderWorker.postMessage({ cmd: 'encode', buf: Uint8ArrayToFloat32Array(data.samples) });
// encoderWorker.postMessage({ cmd: 'finish'});
encoderWorker.onmessage = function(e) {
if (e.data.cmd == 'data') {

console.log("Done converting to Mp3");
//log.innerHTML += "\n" + "Done converting to Mp3";
$("#loadStatus").fadeOut("slow");
/*var audio = new Audio();
audio.src = 'data:audio/mp3;base64,'+encode64(e.data.buf);
audio.play();*/

//console.log ("The Mp3 data " + e.data.buf);

//var mp3Blob = new Blob([new Uint8Array(e.data.buf)], {type: 'audio/mp3'});
//uploadAudio(mp3Blob);
//console.log(e.data.buf);
var audiosrc = 'data:audio/mp3;base64,'+encode64(e.data.buf);
var audioData=encode64(e.data.buf);
//console.log("data--::"+audiosrc);
if(typeof audioData==='undefined' || audioData===null || audioData===''){
}
else{
	uploadAudioBase64(audiosrc);
}
//console.log(audiosrc)



}
};
};

fileReader.readAsArrayBuffer(blob);

currCallback(blob);
}


function encode64(buffer) {
var binary = '',
bytes = new Uint8Array( buffer ),
len = bytes.byteLength/2;

for (var i = 0; i < len; i++) {
binary += String.fromCharCode( bytes[ i ] );
}
return window.btoa( binary );
}

function parseWav(wav) {
function readInt(i, bytes) {
var ret = 0,
shft = 0;

while (bytes) {
ret += wav[i] << shft;
shft += 8;
i++;
bytes--;
}
return ret;
}
if (readInt(20, 2) != 1) throw 'Invalid compression code, not PCM';
if (readInt(22, 2) != 1) throw 'Invalid number of channels, not 1';
return {
sampleRate: readInt(24, 4),
bitsPerSample: readInt(34, 2),
samples: wav.subarray(44)
};
}

function Uint8ArrayToFloat32Array(u8a){
var f32Buffer = new Float32Array(u8a.length);
for (var i = 0; i < u8a.length; i++) {
var value = u8a[i<<1] + (u8a[(i<<1)+1]<<8);
if (value >= 0x8000) value |= ~0x7FFF;
f32Buffer[i] = value / 0x8000;
}
return f32Buffer;
}
//Mp3 Upload
function uploadAudio(mp3Data){
var reader = new FileReader();
reader.onload = function(event){
var fd = new FormData();
var mp3Name = encodeURIComponent('audio_recording_' + new Date().getTime() + '.mp3');
//console.log("mp3name = " + mp3Name);
fd.append('fname', mp3Name);
fd.append('data', event.target.result);

};      
reader.readAsDataURL(mp3Data);
}

//Newly Added Fucntion
function uploadAudioBase64(audiosrc){

var ID = Number(new Date());
var A='<div class="audioContainer">'
+'<div class="audioProgress" id="audioProgress'+ID+'" style="width:0px"></div>'
+'<div class="audioControl audioPlayIcon" rel="play" id="'+ID+'"></div>'
+'<div class="audioBar"></div>'
+'<audio preload="auto" src="'+audiosrc+'" type="audio/mp3" name="convertSoundFile" class="recordedAudio" id="audio'+ID+'"><source></audio>'
+'</div>';
//+'<div class="audioTime" id="audioTime'+ID+'"></div>'
var B='<div class="stbody">'
//+'<div class="sttext"><div class="sttext_content">'
+A
+'</div>';//</div></div>';

console.log("bas64");

$('#hidenInputMp3').val(audiosrc);

$('#voiceSendButton').show();
stopPreloader();

/*console.log("secondCall::::"+audiosrc);
console.log("Ending....!!!!");*/

var a=Date.now();
console.log( msToTime(a));

function msToTime(duration) {
    var milliseconds = parseInt((duration%1000)/100)
        , seconds = parseInt((duration/1000)%60)
        , minutes = parseInt((duration/(1000*60))%60)
        , hours = parseInt((duration/(1000*60*60))%24);

    hours = (hours < 10) ? "0" + hours : hours;
    minutes = (minutes < 10) ? "0" + minutes : minutes;
    seconds = (seconds < 10) ? "0" + seconds : seconds;

    return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
}






$(".recordingslist").html(A);
var preview = document.querySelector('#audio'+ID);
preview.addEventListener("loadedmetadata", function(_event) {
	
//	console.log("theFirstTime+++++"+(preview.duration));
//	console.log("CurrentTime+++++"+(preview.currentTime));
	
	
	var duration=parseInt(preview.currentTime);
	var curTime=preview.currentTime,
	currentTime = parseInt(curTime),
	left = duration - currentTime,second, minute;
	second = (left % 60);
	minute = Math.floor(left / 60) % 60;
	second = second < 10 ? "0"+second : second;
	minute = minute < 10 ? "0"+minute : minute;
	
	//var curTime=0;//duration.split(',')[1];
	//alert(duration+' - '+curTime);
	//alert(minute+":"+second);
	durationTime=minute+":"+second;
	
	//$('.audioTime').html(durationTime);
	//TODO whatever
});
$('#btnUploadVoice').removeAttr('disabled');
$("#audioPreLoader").hide();
$("#audioPreLoaderback").hide();
//}
//});
}

source.connect(this.node);
this.node.connect(this.context.destination);    //this should not be necessary
};



window.Recorder = Recorder;

})(window);

function previewFile(item) {
	
	$('#errorMessgaeInMangeSound').html(" ");
		
		var iMaxFilesize = 3100000; // 3.1MB
		var file = $(item)[0].files[0];
		$('#voiceTitleTextBox').val("");
		$("#fileChooseAudioCntrl").css('display', 'none');
		$('#btnUploadVoice').attr('disabled',true);
		if (file) {
			var filename=document.querySelector('input[type=file]').files[0].name;
			var fileFormat=filename.substr(filename.lastIndexOf('.'));
			if (fileFormat == '.mp3' || fileFormat == '.wav' || fileFormat == '.m4a' || fileFormat=='.ogg' /*|| fileFormat=='.flac'*/) {
			var selectFileSize = $(item)[0].files[0].size;
//			console.log("size" + selectFileSize);
			if (selectFileSize > iMaxFilesize) {
				$('#errorMessgaeInMangeSound').html(
						"File is too big.Please upload file size below  3 MB");
				//stopPreloader();
				return false;
			} else {
				if ($('#voiceTitleTextBox').val() == "") {
					var filename = document.querySelector('input[type=file]').files[0].name;
					var fileFormat = filename.substr(filename.lastIndexOf('.'));
					var title=filename.replace(fileFormat, '');
					if(title.length >20){
						var cutFileName = title.substring(0,20);
						$('#voiceTitleTextBox').val(cutFileName);
					}else{
						$('#voiceTitleTextBox').val(title);
					}
					
				}

				var audioFile = $(item).closest('td').children('audio').attr(
						'id');
				//console.log(audioFile);
				var preview = document.querySelector('#' + audioFile);
//				console.log(preview);
				var file = document.querySelector('input[type=file]').files[0];
				var reader = new FileReader();

				reader.addEventListener("load", function() {
					preview.src = 'data:audio/mp3;base64,'
							+ reader.result.split(',')[1];
					$("#fileChooseAudioCntrl").css('display', 'block');
					$('#hidenInputMp3').val(reader.result);
					
//					console.log("inputValue::::::::"+'data:audio/mp3;base64,'+ reader.result.split(',')[1])
				}, false);

				if (file) {

					reader.readAsDataURL(file);
				}
				preview.addEventListener("loadedmetadata", function(_event) {
					choosenFileDuration = preview.duration;
					choosenFileCurrentTime = preview.currentTime
					//alert(choosenFileCurrentTime)
				
				});
				$('#btnUploadVoice').removeAttr('disabled');

			}
			}
			else{
				$('#errorMessgaeInMangeSound').html("Invalid File Choosen..Please select file of MP3,WAV,M4A,OGG");
				$('#btnUploadVoice').attr('disabled',true);
				}
		}
	//stopPreloader();
  


}
function getFileChooseDuration(){
	return choosenFileDuration + ',' + choosenFileCurrentTime;
}

