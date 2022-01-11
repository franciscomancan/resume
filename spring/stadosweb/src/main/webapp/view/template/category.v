<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: State of the Art Digital Ordering System</title>
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script>
<script language="JavaScript" SRC="/stadosweb/script/MochiKit.js"></script>
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
			<span class="pageTitle">categories</span>
			<span class="pageCounter">#if($categories.size() > 0) $categories.size() #else none #end to offer</span>
		</div>
		<div id="stados-context">
			
			<div id="addForm">
				<form method="post" action="/stadosweb/service/webapp/createCategory">
						<fieldset>
						<label for="title">title:<input type="text" name="title" class="input0" value="" /></label><br />
						<label for="description">description:<input type="text" name="description" class="input0" value="" /></label><br />						
						<input type="submit" class="button0" value="register category" />
						</fieldset>
				</form>
			</div>
			
			<div class="table">
				#if($categories.size() > 0) 
			    <div class="th">
			     <div class="td" style="width:100px">title</div>
			     <div class="td" style="width:250px">description</div>
			     <div style="clear: both;"></div>		
			    </div>
			    #end
			    #foreach($category in $categories)
				<div class="tr">
					<div class="td" style="width:100px;">$!category.title</div>
				    <div class="td" style="width:250px;">$!category.description</div>				    
				    <div class="td" style="width:150px;"><a href="javascript:openProductList($category.id,true)">view products</a> ($category.products.size())</div>				    
				    ##created: <span class="item">$!category.created</span><br /><br />
					<div style="clear: both;"></div>
		    	</div>
				#end
		  	</div>
		  	
		  	##created: <span class="item">$!category.created</span><br /><br />
		    ##associated w/$category.products.size() products:<br />
		    ##foreach($product in $category.products)
	    	##<span class="subitem">$product.title</span><br />
		    ##end			
			
		</div>
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
