function pageLoad() {

    let venueHTML = '';

    fetch('/venue/list', {method: 'get'}
    ).then(response => response.json()
    ).then(venues => {

        for (let venue of venues) {

            venueHTML += `<a href="/client/venue.html?id=${venue.id}"><div class="venueOuterDiv">` +
                `<div class="venImage"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" width="100%" height="200px"></div>` +
                `<div class="venueInnerDiv">${venue.name}</div>` +
                `<div class="city">${venue.city}</div>` +
                `<div class="capacity">${venue.capacity}</div>` +
            `</div></a>`;

        }

        venueHTML += '';

        document.getElementById("listDiv").innerHTML = venueHTML;

        checkLogin();
    });

    // Modal
    var modal = document.getElementById("myModal");
    var btn = document.getElementById("myBtn");
    var span = document.getElementsByClassName("close")[0];

    btn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }
}

function checkLogin() {

    let name = Cookies.get("firstname");

    let logInHTML = '';

    if (name === undefined) {

        logInHTML = "<a class='user' href='/client/login.html' style='padding: 0px'><li>Sign in</li></a>";

    } else {

        logInHTML = "<a class='user' href='#' style='padding: 0px'><li>My Profile</li></a>" +
            "<a class='user' href='/client/login.html?logout'><li>Logout</li></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

