<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: categories</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script> 
</head>
<body>			
		<h3 style="margin-top:10px;">Add one of the following product categories...</h3>
		#foreach($category in $categories)
			<div style="padding-top:20px;padding-left:20px;padding-right:20px">
			    <span class="orderProductItem"><a href="javascript:opener.addCategory($mid,$category.id);">$!category.title</a></span><br />
			    description: <span class="item">$!category.description</span>
    		</div>
		#end

</body>
</html>
