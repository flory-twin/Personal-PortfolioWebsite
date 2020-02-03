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
			<header class="background-img-signature-fill-linebox">
				<!-- NTS: FInd some way around the awkward manual spacer insertion required to bring the contained elements large enough to match the size ration of the background image. -->
				<div class="text-center align-middle my-5 py-5">
					<!-- Poor man's small caps -->
					<!-- TODO: Extract shared JavaScript function to create this code based on input of an arbitrary string and a total percentage of screen width. -->
						<span class="text-uppercase" style="font-size:9vw;">K</span>
						<span class="text-uppercase" style="font-size:8vw;">evin&nbsp;&nbsp;</span>
						<span class="text-uppercase" style="font-size:9vw;">F</span>
						<span class="text-uppercase" style="font-size:8vw;">lory</span>
				</div>
			</header>
			
			<div class="row">
				<div class="col-sm-2"></div>
				<div class="col-sm-4">
					<div class="mr-5">
						<div class="jumbotron center-block">
							<img src="../Code-I.png" class="mw-100"/>
							<div class="center-block">
								<span class="text-uppercase" style="font-size:4vw;">Code</span>
							</div>
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="ml-5">
						<div class="jumbotron">
							<img src="../700px-Renaisscylon.jpg" class="mw-100 mh-80"/>
							
								<p class="text-uppercase center-block" style="font-size:4vw;">Art</p>
							
						</div>
					</div>
				</div>
				<div class="col-sm-2"></div>
			</div>

		
		<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
		<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
	</body>
	
	
</html>