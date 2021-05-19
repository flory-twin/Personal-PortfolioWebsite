<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
	    <meta charset="utf-8"/>
	    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
		<title>Kevin Flory&apos;s Portfolio: Home</title>
		<link rel="stylesheet" href="webjars/bootstrap/4.0.0/css/bootstrap.min.css"/>
		<link href="css/index.css" rel="stylesheet"/>
		<link href="css/signature-behind.css" rel="stylesheet"/>
	</head>
	
	<body>
		<main>

			<div class="container-fluid mt-5 mb-3">
				<!-- TODO: Encapsulate as tag; see https://stackoverflow.com/questions/23604033/is-including-other-jsp-via-the-spring-mvc-framework-a-good-idea and https://docs.oracle.com/javaee/5/tutorial/doc/bnama.html. -->
				<div
				  style="min-height:25vh;max-height:30vh;max-width:73vw"
				  class="container signature-behind mt-5 justify-content-center">
				    <h1 class="display-2 text-center"><b>Kevin Flory</b></h1>
				</div>
				
				<div 
				  style="min-height:40vh"
				  class="container  mt-3 mb-5 pb-5 justify-content-center">
					<div class="row">
						<div class="col-sm"></div>
						<div class="col-lg">
							<button 
								id="professionalButton"
								class="btn eclipse-behind mr-2 pt-1" 
								style="min-width:27vw;min-height:39vh" 
								onclick="location.assign('/professional')">
	    							<h2 class="display-4 text-center"><b>Professional</b></h2>
							</button>
						</div>
						<div class="col-lg">
							<!-- TODO: Add onmouseover with 'status message' (blurb about what the button does) -->
							<button 
								id="artButton"
								class="btn upperpeninsula-behind ml-2 pt-1" 
								style="min-width:27vw;min-height:39vh" 
								onclick="location.assign('/art')">
	    							<h2 class="display-4 text-center"><b>Art</b></h2>
							</button>
						</div>
						<div class="col-sm"></div>
					</div>
				</div>
			</div>
			
			<jsp:include page="fragments/builtWith.jsp"/>
		</main>
		
		<script src="js/index.js"/>
		<script src="js/header.js"/>
		<script src="js/workingOnSiteAlert.js"/>
		<script src="webjars/jquery/3.5.1/jquery.slim.min.js"/>
		<script src="webjars/popper.js/1.11.1/dist/popper.min.js"/>
		<script src="webjars/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	</body>
</html>