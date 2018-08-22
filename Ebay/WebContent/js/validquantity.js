function validQuantity(){
	
	var valid = document.getElementById("valid");
	var qty = document.getElementById("quantity").value;
	
	valid.innerHTML="";
	
	if(qty<=0){
		valid.innerHTML = "<span style='color:red'>Insert quantity 1 or more</span>";
		return false;
	}
	
	return true;
}