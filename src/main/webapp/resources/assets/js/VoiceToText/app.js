var recording = false;

var audio_context = null;
var stream = null;

var encoder = null;
var ws = null;
var input = null;
var node = null;
var samplerate = 44100;
var autoSelectSamplerate = true;
var samplerates = [ 8000, 11025, 12000, 16000, 22050, 24000, 32000, 44100,
		48000 ];
var compression = 1;
var compressions = [ 0, 1, 2, 3, 4, 5, 6, 7, 8 ];

var recordButtonStyle = "red-btn";
var flacdata = {
	bps : 16,
	channels : 1,
	compression : 1
};
// var flacdata.bps = 16;
// var flacdata.channels = 1;
// var flacdata.compression = 5;
var wav_format = false;
var outfilename_flac = "output.flac";
var outfilename_wav = "output.wav";

// ASR-related settings (using Google Cloud Speech service)
var languages = [ 'af-ZA', 'am-ET', 'hy-AM', 'az-AZ', 'id-ID', 'ms-MY',
		'bn-BD', 'bn-IN', 'ca-ES', 'cs-CZ', 'da-DK', 'de-DE', 'en-AU', 'en-CA',
		'en-GH', 'en-GB', 'en-IN', 'en-IE', 'en-KE', 'en-NZ', 'en-NG', 'en-PH',
		'en-ZA', 'en-TZ', 'en-US', 'es-AR', 'es-BO', 'es-CL', 'es-CO', 'es-CR',
		'es-EC', 'es-SV', 'es-ES', 'es-US', 'es-GT', 'es-HN', 'es-MX', 'es-NI',
		'es-PA', 'es-PY', 'es-PE', 'es-PR', 'es-DO', 'es-UY', 'es-VE', 'eu-ES',
		'fil-PH', 'fr-CA', 'fr-FR', 'gl-ES', 'ka-GE', 'gu-IN', 'hr-HR',
		'zu-ZA', 'is-IS', 'it-IT', 'jv-ID', 'kn-IN', 'km-KH', 'lo-LA', 'lv-LV',
		'lt-LT', 'hu-HU', 'ml-IN', 'mr-IN', 'nl-NL', 'ne-NP', 'nb-NO', 'pl-PL',
		'pt-BR', 'pt-PT', 'ro-RO', 'si-LK', 'sk-SK', 'sl-SI', 'su-ID', 'sw-TZ',
		'sw-KE', 'fi-FI', 'sv-SE', 'ta-IN', 'ta-SG', 'ta-LK', 'ta-MY', 'te-IN',
		'vi-VN', 'tr-TR', 'ur-PK', 'ur-IN', 'el-GR', 'bg-BG', 'ru-RU', 'sr-RS',
		'uk-UA', 'he-IL', 'ar-IL', 'ar-JO', 'ar-AE', 'ar-BH', 'ar-DZ', 'ar-SA',
		'ar-IQ', 'ar-KW', 'ar-MA', 'ar-TN', 'ar-OM', 'ar-PS', 'ar-QA', 'ar-LB',
		'ar-EG', 'fa-IR', 'hi-IN', 'th-TH', 'ko-KR', 'cmn-Hant-TW',
		'yue-Hant-HK', 'ja-JP', 'cmn-Hans-HK', 'cmn-Hans-CN' ];

var __language = /\blanguage=([^&]*)/.exec(document.location.search);// <-
																		// for
																		// testing:
																		// set
																		// pre-selected
																		// language
																		// code
																		// via
																		// search-param
																		// in
																		// URL:
																		// ...?language=<language
																		// code>
var language = __language ? __language[1] : 'en-US';

var result_mode = "file";// values: "asr" | "file" | TODO: "asr&file"
var asr_result = {
	text : ""
};
var _asr_alternatives = 1;

// your API key from Google Console for Google Cloud Speech service (secret!!!)
// for more details on how to obtain an API key see e.g.
// WARNING: for security reasons, it's recommended to use service API auth
// instead of an app key
// ... in any case: only use this for test, NEVER publish your secret key!

// var __key = /\bkey=([^&]*)/.exec(document.location.search);//<- for testing:
// set app key via search-param in URL: ...?key=<API key>
// var _google_api_key = __key? __key[1] :
// 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx';

var _google_api_key = "AIzaSyCfdPrIvwxCRLCDUmhayn6iwbl38hGP_c4";

// var __authMethod = /\bauth=([^&]*)/.exec(document.location.search);//<- for
// testing: set auth-method via search-param in URL: ...?auth=<authentification
// method>
// var auth = __authMethod? __authMethod[1] : null;//values: "apiKey" |
// "serviceKey" (DEFAULT: "apiKey")

var auth = "apiKey";

// do not changes these: this "detects" if a key for the Google Speech API is
// set or not
// (and updates page accordingly, i.e. enable/disable check-box for sending
// audio to ASR service):
var __def_key = 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx';
var isNotASRCapable = _google_api_key === __def_key;

$(document).ready(function() {

});

function startRecordingVoiceToText(e) {
	if (recording) {
		return;
	}
	$('.textarea-control').html("");
	console.log('start recording');// DEBUG
	encoder = new Worker('../resources/assets/js/VoiceToText/encoder.js');
	encoder.onmessage = function(e) {
		if (e.data.cmd == 'end') { // $(e) or $(e.currentTaget)
			var resultMode = 'asr';
			if (resultMode === 'asr') {
				sendASRRequest(e.data.buf);
			} else {
				console.log('Unknown mode for processing STOP RECORDING event: "'
								+ resultMode + '"!');
			}
			encoder.terminate();
			encoder = null;

		} else {
			console.log('Unknown event from encoder (WebWorker): "'
					+ e.data.cmd + '"!');
		}
	}

	if (navigator.webkitGetUserMedia)
		navigator.webkitGetUserMedia({
			video : false,
			audio : true
		}, gotUserMedia, userMediaFailed);
	else if (navigator.mozGetUserMedia)
		navigator.mozGetUserMedia({
			video : false,
			audio : true
		}, gotUserMedia, userMediaFailed);
	else
		navigator.getUserMedia({
			video : false,
			audio : true
		}, gotUserMedia, userMediaFailed);
}

function recordaswave(isUseWavFormat) {
	wav_format = isUseWavFormat;
}

function setResultMode() {
	result_mode = 'asr';
	// ? 'asr' : 'file';
}

function compressionOnChange() {
	compression = $("#compressionDdl").val();

	// alert(compression);
}

function userMediaFailed(code) {
	console.log('grabbing microphone failed: ' + code);
}

function gotUserMedia(localMediaStream) {
	recording = true;
	recordButtonStyle = '';
	console.log('success grabbing microphone');
	stream = localMediaStream;

	// var audio_context;
	if (typeof webkitAudioContext !== 'undefined') {
		audio_context = new webkitAudioContext;
	} else if (typeof AudioContext !== 'undefined') {
		audio_context = new AudioContext;
	} else {
		console
				.error('JavaScript execution environment (Browser) does not support AudioContext interface.');
		console
				.log('Could not start recording audio:\n Web Audio is not supported by your browser!');
		return;
	}
	// $scope.audio_context = audio_context;
	input = audio_context.createMediaStreamSource(stream);

	if (input.context.createJavaScriptNode)
		node = input.context.createJavaScriptNode(4096, 1, 1);
	else if (input.context.createScriptProcessor)
		node = input.context.createScriptProcessor(4096, 1, 1);
	else
		console
				.error('Could not create audio node for JavaScript based Audio Processing.');

	var sampleRate = audio_context.sampleRate;
	console.log('audioContext.sampleRate: checkSample rate== ' + sampleRate);// DEBUG==

	if (autoSelectSamplerate) {// angular code @@
		samplerate = sampleRate;
	}

	/*console.log('initializing encoder with:');// DEBUG
	console.log(' bits-per-sample = ' + flacdata.bps);// DEBUG
	console.log(' channels        = ' + flacdata.channels);// DEBUG
	console.log(' sample rate     = ' + samplerate);// DEBUG
	console.log(' compression     = ' + compression);// DEBUG
*/	encoder.postMessage({
		cmd : 'init',
		config : {
			samplerate : samplerate,
			bps : flacdata.bps,
			channels : flacdata.channels,
			compression : compression
		}
	});

	node.onaudioprocess = function(e) {
		if (!recording)
			return;
		// see also:
		// http://typedarray.org/from-microphone-to-wav-with-getusermedia-and-web-audio/
		var channelLeft = e.inputBuffer.getChannelData(0);
		// var channelRight = e.inputBuffer.getChannelData(1);
		encoder.postMessage({
			cmd : 'encode',
			buf : channelLeft
		});
	};

	input.connect(node);
	node.connect(audio_context.destination);

	function apply() {
		asr_result.text = "Waiting for Recognition Result...";
	}
}

function stopRecordingVoiceToText() {
	if (!recording) {
		return;
	}
	$('.textarea-control').html("Waiting for Recognition Result...");
	recordButtonStyle = "red-btn";
	console.log('stop recording  :'+ new Date());
	var tracks = stream.getAudioTracks()
	for (var i = tracks.length - 1; i >= 0; --i) {
		tracks[i].stop();
	}
	recording = false;
	encoder.postMessage({
		cmd : 'finish'
	});
	input.disconnect();
	node.disconnect();
	input = node = null;
}

// create A-element for data BLOB and trigger download
function forceDownload(blob, filename) {
	var url = (window.URL || window.webkitURL).createObjectURL(blob);
	var link = window.document.createElement('a');
	link.href = url;
	link.download = filename || 'output.flac';
	// NOTE: FireFox requires a MouseEvent (in Chrome a simple Event would do
	// the trick)
	var click = document.createEvent("MouseEvent");
	click.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0, false,
			false, false, false, 0, null);
	link.dispatchEvent(click);
}
var num = 0;
function sendASRRequest(blob) {

	function ajaxSuccess() {
		var result = this.responseText;
		// console.log("AJAXSubmit - Success!"); //DEBUG
		// console.log(result);
		console.log('success  from  Google API  :'+ new Date());
		var obj = JSON.parse(result);
		 console.log("JSON OBJECT");
		 console.log(obj);

		$.each(obj, function(index, results) {
			$.each(results, function(subindex, alternative) {
			 console.log(alternative);

				$.each(alternative, function(subindex, alternative1) {
					$('.textarea-control').html("");
					var value = alternative1[0].transcript;
				
					$('.textarea-control').html(value);
					$('#textBoxMessage').html("");
					
					console.log('result appened  : '+ new Date());

				});

			});

		});

		try {
			result = JSON.parse(result);
			// format the result
			result = JSON.stringify(result, null, 2);

		} catch (exc) {
			console.warn('Could not parse result into JSON object: "' + result
					+ '"');
		}

		function apply() {
			asr_result.text = result;
			//console.log("Result Data");
			// console.log(result.alternatives);
		}
	}

	var data;
	var sample_rate = samplerate;
	var language = language;
	var key = _google_api_key;
	var alternatives = _asr_alternatives;
	var reader = new window.FileReader();
	reader.readAsDataURL(blob);
	
	
	
	reader.onloadend = function() {
		// only use base64-encoded data, i.e. remove meta-data from beginning:
		var audioData = reader.result.replace(/^data:audio\/flac;base64,/, '');
	//	console.log(audioData);
		data = {
			config : {
				encoding : "FLAC",
				sampleRateHertz : sample_rate,
				languageCode : "ja-JP",

			},
			audio : {
				content : audioData
			}
		};
		console.log('Data encoding completed  :'+ new Date());
		
		
		var oAjaxReq = new XMLHttpRequest();
		oAjaxReq.onload = ajaxSuccess;

		if (!auth) {
			auth = 'serviceKey';
		} else if (auth !== 'apiKey') {
			console.error('unknown authentification method: ', auth);
		}

		var params = auth === 'apiKey' ? '?key=' + key
				: (auth === 'serviceKey' ? '?access_token=' + key : '');
		oAjaxReq.open("post",
				"https://speech.googleapis.com/v1/speech:recognize" + params,
				true);
		oAjaxReq.setRequestHeader("Content-Type", "application/json");
		
		console.log('Before  send to Google API  :'+ new Date());
		oAjaxReq.send(JSON.stringify(data));
	};

	function apply() {
		asr_result.text = "Waiting for Recognition Result...";
	}

};