<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: user form</title>
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<!--<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script>--> 
</head>
<body>
		<h3 style="margin-top:10px;">User details...</h3>
		<div id="addForm" style="margin-top:25px">
			<form method="post" action="/stadosweb/service/session/$serviceCall">
				<fieldset>
					<label for="user">username:<input type="text" name="user" class="input0" value="$!user.username" /></label><br /><br />
					<label for="pass1">password:<input type="text" name="pass1" class="input0" value="$!user.password" /></label><br />
					<label for="pass2">confirm password:<input type="password" name="pass2" class="input0" value="" /></label><br /><br />
					<label for="first">first name:<input type="text" name="first" class="input0" value="$!user.firstname" /></label><br />
					<label for="last">last name:<input type="text" name="last" class="input0" value="$!user.lastname" /></label><br />
					<label for="email">email:<input type="text" name="email" class="input0" value="$!user.email" /></label><br />
					<label for="description">description:<input type="text" name="description" class="input0" value="$!user.description" /></label><br /><br />
					##<label for="custom">custom:<input type="text" name="custom" class="input0" value="$!user.usercustom" /></label><br />
					<input type="hidden" name="uid" value="$user.id"/>
					<input type="hidden" name="internal" value="indeed"/>
					<select name="type">
						<option value="o">Observer - view production queues/menus</option>
						<option value="s">Server - allows ordering</option>
						<option value="m">Manager - allows void/comp operations</option>
						<option value="a">Super - all usage and auditing</option>
					</select><br /><br /><br />
					<input type="submit" class="button0" value="submit" onclick="window.opener.closeUserForm()"/>
				</fieldset>
			</form>
		</div>

</body>
</html>
