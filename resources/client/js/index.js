function pageLoad() {

    let venueHTML = '';

    fetch('/venue/list', {method: 'get'}
    ).then(response => response.json()
    ).then(venues => {

        for (let venue of venues) {

            venueHTML += `<a href="/client/venue.html?id=${venue.id}"><div class="venueOuterDiv">` +
                `<div class="venueImage"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" width="100%" height="200px"></div>` +
                `<div class="venueInnerDiv">${venue.name}</div>` +
                `<div class="city">${venue.city}</div>` +
                `<div class="capacity">${venue.capacity}</div>` +
            `</div></a>`;

        }

        venueHTML += '';

        document.getElementById("listDiv").innerHTML = venueHTML;

        checkLogin();

    });

}

function checkLogin() {

    let name = Cookies.get("firstname");

    let logInHTML = '';

    if (name === undefined) {

        logInHTML = "<a class='signIn' href='/client/login.html'><li>Sign in</li></a>";

    } else {

        logInHTML = name + " <a class='signIn' href='/client/login.html?logout'><li>Logout</li></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}
