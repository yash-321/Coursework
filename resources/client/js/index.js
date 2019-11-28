function pageLoad() {

    let venueHTML = '';

    fetch('/venue/list', {method: 'get'}
    ).then(response => response.json()
    ).then(venues => {

        for (let venue of venues) {

            venueHTML += `<div class="venueOuterDiv" href="#">` +
                `<div class="venueInnerDiv"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" height="100px"></div>` +
                `<div class="venueInnerDiv">${venue.name}</div>` +
            `</div>`;

        }

        venueHTML += '';

        document.getElementById("listDiv").innerHTML = venueHTML;

    });

}