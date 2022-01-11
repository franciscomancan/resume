<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: State of the Art Digital Ordering System</title>
<meta name="keywords" content="" />
<meta name="description" content="" />
<link href="style/default.css" rel="stylesheet" type="text/css" />
</head>
<body onload="document.forms[0].reset();document.getElementById('user').focus()">

<div id="logo-wrap">
<div id="logo">
	<h1><a href="#">stados</a></h1>
	<h2>State of the Art Digital Ordering System</h2>
</div>
</div>

<!-- end header -->
<!-- start page -->
<div id="wrapper">
<div id="wrapper-btm">
<div id="page">	
	<!-- start content -->
	<div id="content">
		<div id="banner">&nbsp;</div>
		<div class="post" style="">
			
			<h3 style="margin-left:200px;padding-bottom:15px;">Already have a stados id?</h3>
			<div class="loginForm">				
				<form method="post" action="service/session/login">					
						<fieldset>
						user:&nbsp;&nbsp;<input type="text" name="user" id="user" class="input0" value="" /><br />
						pass:&nbsp;&nbsp;<input type="password" name="pass" id="pass" class="input0" value="" /><br />
						<input type="submit" class="button0" value="sign in" />
						</fieldset>
				</form>
			</div>
			<h3 style="margin-left:200px;padding-top:20px">Or... sign up for an account</h3>
			<div class="registerForm">
				<form method="post" action="service/session/register">
					<input type="hidden" name="ctx" />
					<input type="submit" class="button0" value="register" />
				</form>		
			</div>
			
			<div class="meta">
				
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
