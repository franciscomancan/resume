<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: State of the Art Digital Ordering System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script> 
<script language="JavaScript" SRC="/stadosweb/script/MochiKit.js"></script>
<style type="text/css">
.table .td {
	width: 15%;
}
</style>
</head>
<body>

#parse("header.v")

<!-- end header -->
<!-- start page -->
<div id="wrapper">
<div id="wrapper-btm">
<div id="page">	
	<!-- start content -->
	<div id="content">
		<div id="banner">&nbsp;</div>
		<div>
			<span class="pageTitle">orders</span>
			<span class="pageCounter">#if($total > 0) $total #else none #end on record</span>
			<span style="float:right;margin-right:15px;">
				<a href="/stadosweb/service/webapp/viewPending" title="orders currently in production">pending orders</a><br /><br />
				<a href="/stadosweb/service/webapp/viewServed" title="expedited but not closed">served orders</a><br /><br />
				<a href="/stadosweb/service/webapp/viewAllOrders" title="all">all orders</a>
			</span>
		</div>
		<div id="stados-context">
			
			<div id="addForm">
				<form method="post" action="/stadosweb/service/webapp/createOrder">
						<fieldset>
						<label for="location">location/table:<input type="text" name="location" class="input0" value="" /></label><br />
						<input type="submit" class="button0" value="create invoice" />
						</fieldset>
				</form>
			</div>
			
			<div class="table">
				#if($invoices.size() > 0)
			    <div class="th">
			    	<div class="td" style="width:10%"
			    	>invoice</div>
					<div class="td">server</div>
					<div class="td">location</div>
					<div class="td">status</div>
					<div class="td"></div>
					<div style="clear: both;"></div>		
			    </div>
			    #end
			    #foreach($invoice in $invoices)
				<div class="tr">
					<div class="td" style="width:10%">$!invoice.id</div>
					<div class="td">$!invoice.owner.username</div>
	    			<div class="td">$!invoice.location</div>
	    			## invoicecustom used as 'status'
	    			<div class="td">$!invoice.invoiceCustom 
	    				#if($invoice.invoiceCustom && $invoice.invoiceCustom != 'payed')
	    					<a href="javascript:bumpStatus($invoice.id);">(bump)</a>
	    				#end	    					
	    			</div>	    			
	    			<div class="td">
	    				<a style="text-decoration:none" href="javascript:openOrderProductList($invoice.id);">
	    				#if($!invoice.invoiceCustom != 'payed' && $!invoice.invoiceCustom != 'closed')
	    					add to order
	    				#else
	    					
	    				#end	
	    				</a>
	    				## unnecessary, commented out to lazy load &nbsp;&nbsp;($invoice.invoiceProducts.size() items)
	    			</div>
	    			
				    <div class="td"><a href="javascript:openOrderDetail($invoice.id);">view/audit</a></div>
					<div style="clear: both;"></div>
		    	</div>
				#end
		  	</div>
			
		</div>
	</div>
	<div class="usage" style="float:right;margin-right:20px;">
		#if(!$noless == 'true' && $total > 10)
			<a href="/stadosweb/service/webapp/listOrders?page=prev">Prev</a>
		#end
		#if(!$noless && !$nomore) | #end
		#if(!$nomore == 'true' && $total > 10)
			<a href="/stadosweb/service/webapp/listOrders?page=next">Next</a>
		#end
	</div>
	<!-- end content -->	
	<div style="clear: both;">&nbsp;</div>
</div>
<!-- end page -->
</div>
</div>
<!-- start footer -->
<div id="footer">
	<div id="footer-wrap">
	<p id="legal">&copy; 2008 - 2010 :: Concept Solutions LLC</p>
	</div>
</div>
<!-- end footer -->
</body>
</html>
