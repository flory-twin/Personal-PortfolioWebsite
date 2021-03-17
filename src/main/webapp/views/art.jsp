<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
	<!-- ---------------------------------------------------------------------------------------- -->
	<!-- THIS PAGE REQUIRES PARAMETERS: -->
	<!-- ---------------------------------------------------------------------------------------- -->
	<head>
	    <!-- Required meta tags -->
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		

		<title>Kevin's Art</title>
		<!--  <link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/sandstone/bootstrap.min.css" rel="stylesheet" integrity="sha384-G3Fme2BM4boCE9tHx9zHvcxaQoAkksPQa/8oyn1Dzqv7gdcXChereUsXGx6LtbqA" crossorigin="anonymous"> -->
		<!-- <link href="webjars/bootstrap/4.6.0/css/bootstrap.min.css" rel="stylesheet"/>-->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
		<link href="css/index.css" rel="stylesheet"/>
	</head>
	

	<body>
		<main>
			<!--  Looking for Kevin's Grand Circus graduation project?  -->
			
			<div class="container signature-behind py-5">
				<h1 class="display-1 text-center justify-content-center">Kevin Flory</h1>
			</div>
			
			<div class="container mh-75">
				<c:forEach var="row" varStatus="rowStatus" items="${imagePaths}">
				<c:set var="rowNum" value="${rowStatus.getCount()}"/>
				<div class="row" id="row${rowNum}">
					<c:forEach var="col" varStatus="colStatus" items="${row}">
					<c:set var="colNum" value="${colStatus.getCount()}"/>
					<div id="row${rowNum}col${colNum}" class="col-sm m-2 h-25">
					  <div class="card-body">
				    	<img src="${col.relativeResourcePath}" style="max-height:100%;max-width:25%"/>
					  </div>
					</div>
					</c:forEach>
				</div>
				</c:forEach>
			</div>
			
			<%@include file="/views/fragments/builtWith.jspf" %>
		</main>
		
		<script src="custom.js"></script>
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" 
	      integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" 
	      crossorigin="anonymous"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
	</body>
	
	
</html>