<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: products</title>
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script> 
</head>
<body>			
		<h3 style="margin-top:10px;">Add one of the following products...</h3>
		#foreach($product in $products)
			<div style="padding-top:20px;padding-left:20px;padding-right:20px">
			    <span class="orderProductItem"><a href="javascript:opener.addOrderProduct($!iid,$!product.id);">$!product.title</a></span><br />
			    category: <span class="item">$!product.category.title</span><br />
    		</div>
		#end

</body>
</html>
