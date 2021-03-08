"use strict";

//<div class="card" style="width: 18rem;">
let artCard=`
<div class="card statue-behind" style="width: 18rem;">
  <div class="card-body">
    <h5 class="card-title">Art</h5>
    <p class="card-text">I'm an amateur watercolorist. See some of my work using the button below.</p>
    <a href="/art.html" class="btn btn-primary">See All Art</a>
  </div>
</div>`

//Replace clicked element with a Bootstrap card.
function swapArtCard() {
	document.getElementById("artButton").outerHTML = artCard;
}