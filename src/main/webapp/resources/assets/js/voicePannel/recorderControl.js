 /*
License (MIT)

Copyright © 2013 Matt Diamond
Modified by Srinivas Tamada http://www.9lessons.info


 
 
 function __log(e, data) {
    log.innerHTML += "\n" + e + " " + (data || '');
  }*/

  var audio_context;
  var recorder;

  function startUserMedia(stream) {
    var input = audio_context.createMediaStreamSource(stream);
    //__log('Media stream created.' );
	//__log("input sample rate " +input.context.sampleRate);
    
    input.connect(audio_context.destination);
    //__log('Input connected to audio context destination.');
    recorder = new Recorder(input);
	$("#recordButton").removeClass("recordOff").addClass("recordOn");
	$("#recordHelp").fadeOut("slow");
	   // __log('Recorder initialised.');
  }

  function startRecording(button) {
//	  console.log($(button));//.nextElementSibling);
    recorder && recorder.record();
    button.disabled = true;
    //button.nextElementSibling.disabled = false;
    //__log('Recording...');
  }
  
  function getRecording(button){
	  button.disabled = false;
  }

  function stopRecording(button) {
    recorder && recorder.stop();
    button.disabled = true;
    //button.previousElementSibling.disabled = false;
    //__log('Stopped recording.');
    
    // create WAV download link using audio data blob
    createDownloadLink();
    
    recorder.clear();
  }

  function createDownloadLink() {
    recorder && recorder.exportWAV(function(blob) {
    
    });
  }

  window.onload = function init() {
    try {
    	//alert('sucess');
      // webkit shim
      window.AudioContext = window.AudioContext || window.webkitAudioContext;
      navigator.getUserMedia = ( navigator.getUserMedia ||
                       navigator.webkitGetUserMedia ||
                       navigator.mozGetUserMedia ||
                       navigator.msGetUserMedia);
      window.URL = window.URL || window.webkitURL;
      
      audio_context = new AudioContext;
      //__log('Audio context set up.');
      //__log('navigator.getUserMedia ' + (navigator.getUserMedia ? 'available.' : 'not present!'));
	  
    } catch (e) {
    	//alert('error');
      alert('No web audio support in this browser!');
    }
    
    navigator.getUserMedia({audio: true}, startUserMedia, function(e) {
      //__log('No live audio input: ' + e);
		switch(e.name){
			case 'DevicesNotFoundError':{
				//alert('errorDeviceNot');
				$("#recordHelp").html('Please Connect Mic..');
				break;
			}
			case 'PermissionDeniedError':{
				//alert('errorPermissionNot');
				$("#recordHelp").html('Please allow Mic permission..');
				break;
			}
		}
//		console.log(e.name)
    });
  };