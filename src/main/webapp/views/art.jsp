<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<meta charset="utf-8" />
		<meta name="viewport"
			content="width=device-width, initial-scale=1, shrink-to-fit=no" />
		<title>Kevin's Art</title>
		<!-- Links to CSS and styling packages -->
		<!--  <link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/sandstone/bootstrap.min.css" rel="stylesheet" integrity="sha384-G3Fme2BM4boCE9tHx9zHvcxaQoAkksPQa/8oyn1Dzqv7gdcXChereUsXGx6LtbqA" crossorigin="anonymous"> -->
		<link rel="stylesheet" href="webjars/bootstrap/4.0.0/css/bootstrap.min.css"/>
		<link href="css/art.css" rel="stylesheet" />
		<link href="css/signature-behind.css" rel="stylesheet" />
		<script src="webjars/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	</head>
	<body>
			<div
			  style="min-height:20vh;max-height:30vh;max-width:73vw"
			  class="container signature-behind mt-5 justify-content-center">
			    <h1 class="display-2 text-center"><b>Kevin Flory</b></h1>
			</div>
	
		<div 
			id="thumbnailAndControlsPane" 
			class="container-fluid">
			<div 
				id="thumbnailControls"
				class="container text-center">
						<c:choose>
							<c:when test="${ startingFrom > 0 }">
								<button 
									id="lastPaneButton"
									class="btn btn-primary"  
									onclick="location.assign('/art?startingFrom=${startingFrom - pageSize}')"
									>Last ${ pageSize }</button>
							</c:when>
							<c:otherwise>
								<button 
									id="lastPaneButton" 
									class="btn btn-primary"
									onclick="location.assign('/art?startingFrom=0')"
									disabled="true">Last ${ pageSize }</button>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${ startingFrom + pageSize < lastIndex }">
								<button 
									id="lastPaneButton"
									class="btn btn-primary"  
									onclick="location.assign('/art?startingFrom=${startingFrom + pageSize}')"
									>Next ${ pageSize }</button>
							</c:when>
							<c:otherwise>
								<button 
									id="lastPaneButton" 
									class="btn btn-primary"
									onclick="location.assign('/art?startingFrom=${startingFrom}')"
									disabled="true">Next ${ pageSize }</button>
							</c:otherwise>
						</c:choose>
			</div>
		</div>
	
	
		<div id="thumbnailPane" class="container-fluid" style="min-height: 70vh">
			<c:forEach var="row" varStatus="rowStatus" items="${imagePaths}">
				<c:set var="rowNum" value="${rowStatus.getCount()}" />
				<div class="row" id="row${rowNum}">
					<c:forEach var="col" varStatus="colStatus" items="${row}">
						<c:set var="colNum" value="${colStatus.getCount()}" />
						<div id="row${rowNum}col${colNum}Cell" class="col text-center"
							style="max-height: 30vh; min-height: 30vh">
								<button
									id="row${rowNum}col${colNum}Button"
									style="background-image: url('${ col.relativeResourcePath }'); width: 100%; height: 100%"
									class="btn btn-secondary center-background-image" 
									onclick="location.assign('/${ col.relativeResourcePath }')"/>
						</div>
					</c:forEach>
				</div>
			</c:forEach>
		</div>
	
		<jsp:include page="fragments/builtWith.jsp" />
	</body>
</html>