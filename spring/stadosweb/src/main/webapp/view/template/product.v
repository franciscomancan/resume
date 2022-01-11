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
	width: 23%;
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
			<span class="pageTitle">products</span>
			<span class="pageCounter">#if($products.size() > 0) $products.size() #else none #end to serve</span>
		</div>
		<div id="stados-context">
			
			<div style="width:250px;float:left;margin-left:75px;">
				<form method="post" action="/stadosweb/service/webapp/createProduct">
						<fieldset>
						#if($categories.size() > 0)
							<label for="title">title:<input type="text" name="title" class="input0" value="" /></label><br />
						#end
						<label for="category">
							#if($categories.size() < 1)
								No existing categories<br /><a href="./listCategories">(create at least one)</a>
							#else
							category:
								<select name="cid">								
								#foreach($category in $categories)
									<option value="$category.id">$category.title</option>
								#end
								</select><br />						
							##<label for="custom">custom:<input type="text" name="custom" class="input0" value="" /></label><br />
							<label for="price">price:<input type="text" name="price" class="input0" value="" /></label><br />
							#end
						</label>
						#if($categories.size() > 0)
							<input type="submit" class="button0" value="add product" />
						#end
						</fieldset>
				</form>
			</div>
			
			<div style="height:150px">
				<a href="">add ordering option</a><span id="optionTxt" style="padding-left:25px;visibility:hidden"><input type="text" /></span>
				<div id="optionsHdr" style="padding-top:25px;">options:</div>
				<div id="optionsList" style="padding-top:10px;color:orange;font-size:18px;">
					bloody, medium, medium well, well, bloody, medium, medium well, well, bloody, medium, medium well, well, bloody, medium, medium well, well
				</div>
			</div>
						
			<div class="table">
				#if($products.size() > 0) 
			    <div class="th">
			    	<div class="td" style="width:8%">#</div>
					<div class="td">title</div>
				    <div class="td">category</div>
				    <div class="td">price</div>
				    <div style="clear: both;"></div>		
			    </div>
			    #end
			    #foreach($product in $products)
				<div class="tr">
					<div class="td" style="width:8%">$velocityCount</div>
					<div class="td">$!product.title</div>
				    <div class="td">
				    	<select name="newCategory$velocityCount" onchange="javascript:addProductCategory(this,$product.id)">
							#foreach($category in $categories) ##logic to select approp. category for each displayed product (to render 'selected')									
							<option value="$category.id" #if($product.category != 'null') #if($product.category.id == $category.id) selected	#end #end>$category.title</option>
							#end
						</select>
				    </div>
				    ##<div class="td"><a href="javascript:deleteProduct($product.id)">delete</a></div>
				    ##<span class="item">$!product.productcustom</span><br />
				    <span class="td">$!product.pennies</span><br />
					<div style="clear: both;"></div>
		    	</div>
				#end
		  	</div>		  	
			
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
