function pageLoad() {

    let venueHTML = '';

    fetch('/venue/list', {method: 'get'}
    ).then(response => response.json()
    ).then(venues => {

        for (let venue of venues) {

            venueHTML += `<div class="venueOuterDiv" href="#">` +
                `<div class="venueImage"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" width="100%" height="200px"></div>` +
                `<div class="venueInnerDiv">${venue.name}</div>` +
                `<div class="city">${venue.city}</div>` +
                `<div class="capacity">${venue.capacity}</div>` +
            `</div>`;

        }

        venueHTML += '';

        document.getElementById("listDiv").innerHTML = venueHTML;

    });

}