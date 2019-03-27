function termsAndCondition() {
	var conditions = document.getElementById("termsAndConditionInput");
	var pre = document.createElement('pre');
	// custom style.
	pre.style.maxHeight = "400px";
	pre.style.margin = "0";
	pre.style.padding = "24px";
	pre.style.whiteSpace = "pre-wrap";
	pre.style.textAlign = "justify";
	pre.appendChild(document.createTextNode($('#conditions').text()));
	// show as confirm
	alertify.confirm(pre, function() {
		alertify.success('Acepto');
		conditions.checked = true;
	}, function() {
		alertify.error('No acepto');
		conditions.checked = false;
	}).set({
		labels : {
			ok : 'Accept',
			cancel : 'Decline'
		},
		padding : false
	});
}