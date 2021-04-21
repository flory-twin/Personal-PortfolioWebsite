<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
	    <meta charset="utf-8"/>
	    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>

		<title>Kevin Flory&apos;s Portfolio: Home</title>
		<!--  <link href="https://stackpath.bootstrapcdn.com/bootswatch/4.3.1/sandstone/bootstrap.min.css" rel="stylesheet" integrity="sha384-G3Fme2BM4boCE9tHx9zHvcxaQoAkksPQa/8oyn1Dzqv7gdcXChereUsXGx6LtbqA" crossorigin="anonymous"> -->
		<!-- <link href="webjars/bootstrap/4.6.0/css/bootstrap.min.css" rel="stylesheet"/>-->
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
		<link href="css/index.css" rel="stylesheet"/>
	</head>
	

	<body>
		<main>
			<!--  Looking for Kevin's Grand Circus graduation project?  -->
			<div class="container-fluid mt-5 mb-3">
				<!-- TODO: Encapsulate as tag; see https://stackoverflow.com/questions/23604033/is-including-other-jsp-via-the-spring-mvc-framework-a-good-idea and https://docs.oracle.com/javaee/5/tutorial/doc/bnama.html. -->
				<div 
					class="container signature-behind justify-content-center mt-5" 
					style="min-height:25vh;max-height:30vh;max-width:73vw">
					<h1 class="display-2 text-center"><b>Kevin Flory</b></h1>
				</div>
				
				<div class="container justify-content-center mt-3 mb-5 pb-5" style="min-height:40vh">
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
			
			<%@include file="/views/fragments/builtWith.jspf" %>
		</main>
		
		<script src="js/index.js"/>
		<script src="js/workingOnSiteAlert.js"/>
		<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" 
	      integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" 
	      crossorigin="anonymous"/>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" 
		  integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" 
		  crossorigin="anonymous"/>
		<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" 
		  integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" 
		  crossorigin="anonymous"/>
	</body>
</html>