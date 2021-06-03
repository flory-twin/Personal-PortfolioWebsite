<!DOCTYPE html>
<html lang="en">
	<!-- ---------------------------------------------------------------------------------------- -->
	<!-- THIS PAGE REQUIRES PARAMETERS: -->
	<!-- ---------------------------------------------------------------------------------------- -->
	<head>
	    <!-- Required meta tags -->
	    <meta charset="utf-8">
	    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		<title>Kevin Flory: Professional Information</title>
		<link rel="stylesheet" href="webjars/bootstrap/4.0.0/css/bootstrap.min.css"/>
		<link href="css/signature-behind.css" rel="stylesheet"/>
		<script src="js/download.js"></script>
	</head>
	
	<body>

			<div class="container-fluid mt-5 mb-3">
				<div
				  style="min-height:25vh;max-height:30vh;max-width:73vw"
				  class="container signature-behind mt-5 justify-content-center">
				    <h1 class="display-2 text-center"><b>Kevin Flory</b></h1>
				</div>
				
				<div class="container justify-content-center mt-3 mb-5 pb-5" style="min-height:40vh">
					<div class="row">
						<div class="col mx-5 px-5">
							<!-- Per-paragraph: mission statement. -->
							<h2 class="text-center">Seeking Development Work</h2>
							<p><b>I need a change.</b> After 10 years doing QA, I wasn't satisfied with manual testing.</p>
							
							<p>As an artist, I already know there's enormous joy in creation--going from an idea, whether mine or someone else's, and choosing how to make it reality. The problem was, <b>testing isn't about creation.</b></p>

							<p>I want to create again, but I already know my best work is created <b>with others</b>. For instance, when I refreshed my skills at the Grand Circus coding bootcamp, I learned the most by facilitating a regular study group for out-of-classroom coworking; all of our code improved.</p>    
							
							<p>Now I'm looking for a good team. <b>What can I add to yours?</b></p>
						</div>
					</div>
					<div class="row">
						<div class="col text-center">
							<h2>For More Information</h2>
							<p>LinkedIn: <a href="https://www.linkedin.com/in/kevin-flory-86ab3324/">https://www.linkedin.com/in/kevin-flory-86ab3324/</a></p>
							<button 
								class="btn btn-primary"
								id="resumeDownloadButton"
								onclick="download.displayAlertAndDownload();"
									>Download Resume</button>
						</div>
					</div>
				</div>
			</div>
			
			<jsp:include page="fragments/builtWith.jsp"/>

	</body>
</html>