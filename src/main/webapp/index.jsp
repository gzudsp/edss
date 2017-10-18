<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="testUpload" method="post" enctype="multipart/form-data">
	<table>
		<tr>
			<td>name:</td>
			<td><input type="text" name="name"></td>
		</tr>
		<tr>
			<td>description:</td>
			<td><input type="text" name="description"></td>
		</tr>
		<tr>
			<td>tokenId:</td>
			<td><input type="text" name="tokenId"></td>
		</tr>
		<tr>
			<td>file:</td>
			<td><input type="file" name="uploadFile"></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" name="提交"></td>
		</tr>
	</table>
	</form>
	<p>---------------------</p>
	<br><br>
</body>
</html>