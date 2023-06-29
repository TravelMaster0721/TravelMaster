let myModal = document.getElementById("deleteBtn");
let exampleModal = new bootstrap.Modal(document.getElementById('exampleModal'));

myModal.addEventListener("click", function(e){
  exampleModal.show();
});

myModal.addEventListener('shown.bs.modal', function () {
  myInput.focus()
})

function insert() {
	location.href = "/TM/spotList/insert";
}

function allSpot() {
	location.href = "/TM/allSpot";
}

function update(id) {
	location.href = `/TM/spotList/update?id=${id}`;
}
function deleteNo(id) {
	location.href = `/TM/spotList/delete?id=${id}`;
	
}

$(document).ready(function() {
	$('#queryResult').DataTable();
});	