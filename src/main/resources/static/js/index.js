"use strict";

//<div class="card" style="width: 18rem;">
let artCard=`
<div class="card statue-behind" style="width: 18rem;">
  <div class="card-body">
    <h5 class="card-title">Art</h5>
    <p class="card-text">I'm an amateur watercolorist. See some of my work using the button below.</p>
    <a href="/art" class="btn btn-primary">See All Art</a>
  </div>
</div>`

let artButtonExpanded = `
<h2 class="display-4 text-center">
	<b>Art</b>
</h2>
<p>Click here to see my work in watercolor, oil, and graphite.</p>
<p><i><b>Warning</b>: Content may include human nudes.</i></p>`

//Replace clicked element with a Bootstrap card.
function swapArtCard() {
	document.getElementById("artButton").outerHTML = artCard;
}

function swapArtButtonContent() {
	document.getElementById("artButton").innerHTML = artButtonExpanded;
}

/*
 * Courtesy of https://stackoverflow.com/questions/8454510/open-url-in-same-window-and-in-same-tab .
 */
function fireClickEvent(element) {
    var evt = new window.MouseEvent('click', {
        view: window,
        bubbles: true,
        cancelable: true
    });

    element.dispatchEvent(evt);
}