<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: State of the Art Digital Ordering System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script> 
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
			<span class="pageTitle">users</span>
			<span class="pageCounter">#if($users.size() > 0) $users.size() #else none #end to work with</span>
			<span style="float:right;margin-right:15px"><a href="javascript:openUserCreate()">add user</a></span>
		</div>
		<div id="stados-context">
						
			<div class="table">
				#if($users.size() > 0) 
			    <div class="th">
			     <div class="td">user</div>
			     <div class="td">name</div>
			     <div class="td">permission</div>
			     <div style="clear: both;"></div>		
			    </div>
			    #end
			    #foreach($user in $users)
				<div class="tr">
					<div class="td">$!user.username</div>
					<div class="td">$!user.lastname, $!user.firstname</div>
					<div class="td">$!user.type</div>
					##<div class="td">$!user.created</div>
					<div class="td"><a href="javascript:openUserEdit($!user.id)">edit user</a></div>
					<div class="td"><a href="">disable user</a></div>
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
