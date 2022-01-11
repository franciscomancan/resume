<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="refresh" content="5" />
<title>stados :: Production order queue</title>
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<style type="text/css" media="all">
.table {
	font-size: 24px;
}
.invoiceItem {
	margin-left:14px;
	color: white;
}
.guid {
	font-weight: 900;
	color: white;
}
.table .td {
	border-right: solid 1px white;
	border-bottom: solid 1px white;
	padding-bottom: 35px;
	height: 400px;
}
</style> 
</head>
<body>
		
	<div class="table">
		<!--
		<div class="th">
			<div class="td"></div>
			<div class="td"></div>
			<div class="td"></div>
			<div class="td"></div>
			<div class="td"></div>
			<div style="clear: both;"></div>
	    </div>
	    -->
		<div class="tr">
		#if($invoices.size() < 1)
			<h1>There are no 'open' orders on queue...</h1>
		#else
			#foreach($invoice in $invoices)
					<div class="td" style="width="200px"><div class="guid">$invoice.id -> $invoice.createdDate</div>
					#foreach($invoiceProduct in $invoice.invoiceProducts) <div class="invoiceItem">$invoiceProduct.quantity $invoiceProduct.product.title #if($invoiceProduct.detail != '') : $!invoiceProduct.detail#end</div>#end</div>
				#### start a new row
				#if($velocityCount % 5 == 0)
				<div style="clear: both;"></div>
				</div>
				<div class="tr">
				#end		
			#end
		#end
		<div style="clear: both;"></div>
		</div>
	</div>

</body>
</html>
