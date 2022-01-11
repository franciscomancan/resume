var productList = {};
var categoryList = {};
var orderProductList = {};
var orderDetail;

var intro = 'Our mission is to take a wasted amount of time and give it purpose.  ' +
			'We will take hundreds of thousands of hours per year and make that time' +
			'beneficial instead of wasteful.  Increased productivity will lead to enhanced ' +
			'experiences and services.  We will change the world in adding time to a world where time is our greatest asset.';  

flagLogin = function() {
	logged = true;
}

nav = function(dest) {
	if(dest == 'home') {
		document.getElementById('stados-context').innerHTML = intro;			
	}
}

openCategoryList = function(mid) {
	categoryList = standardWin('/stadosweb/service/webapp/listCategories?ctx=true&mid=' + mid,'categoryList');
}

openProductList = function(cid, inclusive) {
	// put note for inclusive switch when remembered
	var inclusiveSwitch = (inclusive) ? '&inclusive' : '';
	productList = standardWin('/stadosweb/service/webapp/listProducts?ctx=category&cid=' + cid + inclusiveSwitch,'productList');
}

openOrderProductList = function(iid) {
	orderProductList = standardWin('/stadosweb/service/webapp/listProducts?ctx=order&iid=' + iid,'orderProductList');
}

openOrderDetail = function(oid) {
	if (navigator.userAgent.indexOf('Chrome/') > 0) {
	    if (orderDetail) {
	        orderDetail.close();
	        orderDetail = null;
	    }
	}
	
	if(!orderDetail || (orderDetail && orderDetail.closed))
		orderDetail = scrollWin('/stadosweb/service/webapp/viewOrder?oid=' + oid,'orderDetail',700,400);		
	else {
		//orderDetail.location = '/stadosweb/service/webapp/viewOrder?oid=' + oid;
		orderDetail = scrollWin('/stadosweb/service/webapp/viewOrder?oid=' + oid,'orderDetail',700,400);
	}
	orderDetail.focus();	//doesn't work the second time in chrome
}

openMenuCreate = function() {
	menuForm = standardWin('/stadosweb/service/webapp/createMenu','menuForm');
}
openMenuEdit = function(mid) {
	menuForm = standardWin('/stadosweb/service/webapp/editMenu?mid=' + mid,'menuForm');
}
openUserCreate = function() {
	userForm = standardWin('/stadosweb/service/session/createUser','userForm');
}
openUserEdit = function(uid) {
	userForm = standardWin('/stadosweb/service/session/editUser?uid=' + uid,'userForm');
}
standardWin = function(url, title) {
	return(window.open(url,title,'width=400,height=500,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=no'));
}
scrollWin = function(url, title, w, h) {
	return(window.open(url,title,'width=' + w + ',height=' + h + ',toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,copyhistory=no,resizable=no'));
}

bumpStatus = function(iid) {
	var yes = confirm('Are you sure (cannot be moved back)?');
	if(yes)
		sendRequest('/stadosweb/service/webapp/bumpStatus?iid=' + iid, reloadOrders);
}

addProductCategory = function(elmt,pid) {	/* add product to a category (originally allowing many-to-one) */
	//alert("productId=" + pid + " categoryId=" + elmt[elmt.selectedIndex].value);
	var cid = elmt[elmt.selectedIndex].value;
	//productList.close();
	sendRequest('/stadosweb/service/webapp/addProductCategory?cid=' + cid + '&pid=' + pid, reloadProducts);
}

deleteProduct = function(pid) {
	sendRequest('/stadosweb/service/webapp/deleteProduct?pid=' + pid, reloadProducts);
}

addCategory = function(mid,cid) {	/* add a category to a menu */
	categoryList.close();
	sendRequest('/stadosweb/service/webapp/addCategory?cid=' + cid + '&mid=' + mid, reloadMenus);
}

addOrderProduct = function(iid,pid) {	/* add product to an invoice/order */
	orderProductList.close();
	sendRequest('/stadosweb/service/webapp/addOrderProduct?iid=' + iid + '&pid=' + pid, reloadOrders);
}

auditOrder = function(oid,ipid,val) {
	sendRequest('/stadosweb/service/webapp/auditOrder?ipid=' + ipid + '&val=' + val, openOrderDetail(oid));
	orderDetail.close();
}

/* async call wrapper, utilizing chosen tech (mochiKit) */
sendRequest = function(request,cb) {	
	//var d = new Deferred();	
	d = doSimpleXMLHttpRequest(request);
	//if(cb)
		d.addCallback(cb);
	//d.addErrback(error);
}

/* page reloads, typically used after making async service calls, consolidate later */
reloadInvoice = function() {
	orderDetail.location.reload();
	orderDetail.focus();
}
reloadOrders = function() {
	document.location = '/stadosweb/service/webapp/listOrders';
}
reloadCategories = function() {
	document.location = '/stadosweb/service/webapp/listCategories';
}
reloadProducts = function() {
	document.location = '/stadosweb/service/webapp/listProducts';
}
reloadMenus = function() {
	document.location = '/stadosweb/service/webapp/listMenus';
}
reloadUsers = function() {
	document.location = '/stadosweb/service/webapp/listUsers';
}
closeMenuForm = function() {
	if(menuForm)
		menuForm.close();
	
	setTimeout("reloadMenus()",1000);
}
closeUserForm = function() {
	if(userForm)
		userForm.close();
	
	setTimeout("reloadUsers()",1000);
}