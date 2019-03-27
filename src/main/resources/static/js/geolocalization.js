function getLocalization(){
	//  Html5 Geolocalization
    var shortId = document.getElementById("shortId").textContent;
	/* String format to dev better ending https://www.google.com.ar/maps/@-36.527072,-56.7017652,17z */
	
	if (navigator.geolocation) {
    	navigator.geolocation.getCurrentPosition(function(position) {
          var pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
          };
          console.log(pos);

          /* For mailing */
          var mail = document.getElementById("email").textContent;
          var latitude = pos.lat;
          var longitude = pos.lng;
          var dateTime = new Date();

          /* Send to endponit and JavaMail sends the mail */
          var xhr = new XMLHttpRequest();
          var url = "/sendCoordinatesToMail";
          
          xhr.open("POST", url, true);
          xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
          xhr.send("latitude="+latitude+"&longitude="+longitude+"&mail="+mail+"&dateTime="+dateTime+"&shortId="+shortId);
          /* For Mailing*/

        }, function() {
          handleLocationError(true, infoWindow, map.getCenter());
        });
      } else {
        // Browser doesn't support Geolocation
        handleLocationError(false, infoWindow, map.getCenter());
      }
 
}