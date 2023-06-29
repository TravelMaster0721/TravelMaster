function insert() {
	location.href = "/TM/spotList/insert";
}

function allSpot() {
	location.href = "/TM/allSpot";
}

function update(id) {
	location.href = `/TM/spotList/update?id=${id}`;
}

function deleteNo(spotNo) {
	console.log(spotNo);
	document.querySelector("#deleteTarget").value = spotNo;
	document.querySelector("#ConfirmMsg").innerHTML = `確定刪除(id=${spotNo})?刪除後無法更改!`;
	$("#exampleModal").modal("show");
	$("#exampleModal").css("z-index", "1500");
}


function doDeleteNo() {
	let deleteTarget = document.querySelector("#deleteTarget").value;
	location.href = `/TM/spotList/delete?spotNo=${deleteTarget}`;
}

var myModal;
var exampleModal;
$(document).ready(function() {
	$('#queryResult').DataTable();
	myModal = document.getElementById("deleteBtn");
	exampleModal = new bootstrap.Modal(document.getElementById('exampleModal'));

});	