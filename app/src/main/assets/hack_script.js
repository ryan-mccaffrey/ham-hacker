function runHack() {
	console.log("running hack!");
	document.getElementById('dlslot_name_first').value = '%s';
	document.getElementById('dlslot_name_last').value = '%s';
	document.getElementById('dlslot_email').value = '%s';
	document.getElementById('dlslot_dob_month').value = '%s';
	document.getElementById('dlslot_dob_day').value = '%s';
	document.getElementById('dlslot_dob_year').value = '%s';
	document.getElementById('dlslot_zip').value = '%s';
	document.getElementById('dlslot_agree').checked = true;
	document.getElementById('dlslot_ticket_qty').value = 2;
}

allLinks = document.getElementsByClassName("enter-button enter-lottery-link");
console.log("Matching click targets: " + allLinks.length);

for (var i = 0; i < allLinks.length; i++) {
	obj = allLinks[i];
	obj.onclick = function() {
		runHack();
	};
}