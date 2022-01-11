<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<title>stados :: products</title>
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script>
<style type="text/css">
table{
    border-width: 0 0 0 0;
    border-spacing: 0;
    border-collapse: collapse;
}
td { font-size:14px; font-family:verdana; }
thead { font-size:16px; font-weight:800; font-family:verdana; }
.anchorOps { 
	font-size:16px; margin-left:auto; 
	margin-right:auto; text-align:center;
	padding-top:10px; 
}
</style> 
</head>
<body>			
		<h1 style="margin-top:20px;margin-left:20px;padding-bottom:40px;">
			Invoice Detail - $invoice.location - $invoice.invoiceCustom
		</h1>
		#if($invoice.invoiceProducts.size() > 0)
			<table width="80%" border="1" style="margin:auto;">
			<thead>
				<tr>
					<td>count</td>
					<td>item</td>
					<td>detail</td>
					<td>unit</td>
					<td>total</td>				
				</tr>
			</thead>
			#foreach($iProduct in $invoice.invoiceProducts)
				<tr>
				<td>$!iProduct.quantity</td> 
				<td>$!iProduct.product.title</td> 
				<td>$!iProduct.detail</td>
				<td>$!iProduct.product.pennies</td>
				#if($!iProduct.override)
					<td>$!iProduct.override</td>				
				#else
					<td>$!iProduct.price</td>
				#end
				#if(!($!iProduct.override))
				<td><a href="javascript:opener.auditOrder($invoice.id,$iProduct.id,'v');" style="padding-right:5px;padding-left:5px;font-weight:700;">void</a>/
					<a href="javascript:opener.auditOrder($invoice.id,$iProduct.id,'c');" style="font-weight:700;">comp</a>
				</td>
				#end
				</tr>
			#end
			#foreach($coupon in $invoice.coupons)
				<tr>
					<td>-</td>
					<td>coupon/credit</td>
					<td>$coupon.code</td>
					<td></td>
					<td>($coupon.credit)</td>
				</tr>
			#end
			</table>
		#end
		<div class="anchorOps">subtotal = $!invoice.subtotal</div> 
		<div class="anchorOps">tax = $!invoice.tax</div>
		<div class="anchorOps">due = $!invoice.due</div>
		<div class="anchorOps">tendered = $!invoice.tendered</div>
		<br /><br />
		<a href="javascript:window.close()" style="float:right; padding-right:100px;font-size:20px;">close</a>

</body>
<head>
<META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
</head>
</html>
