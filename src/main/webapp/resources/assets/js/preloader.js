function startPreloader() {
$("#divPreloader").css("display", "block");
$("#divPreloaderStatus").fadeIn();
//$("#divPreloader").delay(1000).fadeIn("slow");
}

function stopPreloader() {
$("#divPreloader").css("display", "none");
//$("#divPreloaderStatus").fadeOut();
//$("#divPreloader").delay(100).fadeOut("slow");
}

/*$('#divPreloader').on('keydown', function(e){ 
  
    e.preventDefault();
 
});*/