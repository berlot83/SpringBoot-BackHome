function activateQR(id, shortId) {
	var activateCheckbox = document.getElementById(id);
	var status = false;

	if (activateCheckbox.checked) {
		status = true;
	} else {
		status = false;
	}

	var xhr = new XMLHttpRequest();
	var url = "/activate-qr";
	xhr.open("POST", url, true);
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhr.send("shortId=" + shortId + "&status=" + status);
	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4 && xhr.status == 200) {
			if (xhr.responseText == "true") {
				alertify.success('QR Activado  (<strong>'+ shortId +'</strong>)');
			} else {
				alertify.error('QR Desactivado  (<strong>'+ shortId +'</strong>)');
			}
		}
	}
}

/* without use right now, because thymeleaf server side action used now */
function getStatusQR(shortId) {
	setTimeout(function() {

		var checkBox = document.getElementById("status" + shortId);
		var xhr = new XMLHttpRequest();
		var url = "/get-status?shortId=" + shortId;

		xhr.open("GET", url, true);
		xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
		xhr.send();

		xhr.onreadystatechange = function() {
			if (xhr.readyState == 4 && xhr.status == 200) {
				alert("Test " + xhr.responseText);
			}
		}
	}, 3000);
}
