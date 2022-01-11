<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>stados :: products</title>
<link href="/stadosweb/style/default.css" rel="stylesheet" type="text/css" />
<!--<script language="JavaScript" SRC="/stadosweb/script/controller.js"></script>--> 
</head>
<body>
		<h3 style="margin-top:10px;">Menu details...</h3>
		<div id="addForm" style="margin-top:75px;">
			<form method="post" action="/stadosweb/service/webapp/$serviceCall">
				<fieldset>
					<label for="title">title:<input type="text" name="title" class="input0" value="$!menu.title" /></label><br />
					<label for="description">description:<input type="text" name="description" class="input0" value="$!menu.description" /></label><br />
					##<label for="custom">custom:<input type="text" name="custom" class="input0" value="$!menu.menucustom" /></label><br /><br />
					<input type="hidden" name="mid" value="$!menu.id" />
					<div style="border:1px solid #fff;">
						<input style="margin-left:50px;" type="radio" name="group1" value="active">active<br />
						<input style="margin-left:50px;" type="radio" name="group1" value="inactive">inactive<br /><br />
					</div>
					<input type="submit" class="button0" value="submit" onclick="window.opener.closeMenuForm()"/>
				</fieldset>
			</form>
		</div>

</body>
</html>
