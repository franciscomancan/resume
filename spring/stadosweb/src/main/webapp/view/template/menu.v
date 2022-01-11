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
			<span class="pageTitle">menus</span>
			<span class="pageCounter">#if($menus.size() > 0) $menus.size() #else none #end to present</span>
			<span style="float:right;margin-right:15px"><a href="javascript:openMenuCreate()">add menu</a></span>
		</div>
		<div id="stados-context">
						
			<div id="listForm">
			#foreach($menu in $menus)
				<div style="position:relative;top:25px;left:300px;">
					<a style="text-decoration:none" href="javascript:openMenuEdit($menu.id)">edit menu</a><br />
					<a style="text-decoration:none" href="javascript:openCategoryList($!menu.id);">add category</a><br />
	    		</div>
				<div>
				    title: <span class="item">$!menu.title</span><br />
				    description: <span class="item">$!menu.description</span><br />
				    ##custom: <span class="item">$!menu.menucustom</span><br />
				    active: <span class="item">$!menu.active</span><br />
				    ##created: <span class="item">$!menu.created</span><br /><br />
				    associated w/$menu.categories.size() categories:<br />
				    #foreach($category in $menu.categories)
				    	<span class="subitem">$category.title</span><br />
				    #end
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
