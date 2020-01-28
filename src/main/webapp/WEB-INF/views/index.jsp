<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 
<spring:url value="/static/bootswatch/bootswatch-darkly-theme.min.css" var="darklyCss" />
<spring:url value="/static/bootstrap/bootstrap-4.3.1-dist/js/bootstrap.bundle.js" var="bundleJs" />
<spring:url value="/static/bootstrap/bootstrap-4.3.1-dist/js/bootstrap.bundle.js" var="bundleJs" />
 -->
 
 
<!DOCTYPE html>
<html>
	<!-- ---------------------------------------------------------------------------------------- -->
	<!-- THIS PAGE REQUIRES PARAMETERS: -->
	<!-- ---------------------------------------------------------------------------------------- -->
	<head>
		<meta charset="UTF-8">
		<title>Kevin Flory's Portfolio: Home</title>
		<link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/sandstone/bootstrap.min.css" rel="stylesheet" integrity="sha384-G3Fme2BM4boCE9tHx9zHvcxaQoAkksPQa/8oyn1Dzqv7gdcXChereUsXGx6LtbqA" crossorigin="anonymous"/>
		<link href="css/background-image.css" rel="stylesheet" type="text/css" />
	</head>
	<!-- KDF note to self: Background: Set the background color to an underlay for the signature picture. -->
	<body class="background-color-signature-underlay">
		
		<!-- KDF note to self: Use Bootstrap, rather than native CSS, to make sure the entire signature shows up properly. 
		     Even though 'sticky' doesn't work in IE 10/11, it doesn't break much since this is largely a 1-screen page.-
		     Note that setting the width to 100% of the initial viewport will always just get the visible viewport,
		       even if the page contents contained within the div extend beyond the viewport. 
		     The image in question is 3812x1908. While I hate to code a hard dependency in this messy way,
		       setting min-vw in this way guarantees that I get the full image, not just a version 
		       clipped to the height of the contents. -->
		<div class="background-img-signature-fill mw-100 min-vw-50 align-middle sticky-top">
			<!-- TODO: Extract shared JavaScript function to create this code based on input of an arbitrary string and a total percentage of screen width. -->
			<div class="center-block">
				<div class="container text-center background-img-signature-fill ">
					<!-- Poor man's small caps -->
						<span class="text-uppercase" style="font-size:9vw;">K</span>
						<span class="text-uppercase" style="font-size:8vw;">evin&nbsp;&nbsp;</span>
						<span class="text-uppercase" style="font-size:9vw;">F</span>
						<span class="text-uppercase" style="font-size:8vw;">lory</span>
				</div>
			</div>
		
		</div>
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
	
	
</html>