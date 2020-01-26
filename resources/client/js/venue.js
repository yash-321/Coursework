
/*-------------------------------------------------------
  A utility function to extract the query string parameters
  and return them as a map of key-value pairs
  ------------------------------------------------------*/
function getQueryStringParameters() {
    let params = [];
    let q = document.URL.split('?')[1];
    if (q !== undefined) {
        q = q.split('&');
        for (let i = 0; i < q.length; i++) {
            let bits = q[i].split('=');
            params[bits[0]] = bits[1];
        }
    }
    return params;
}
//-----------------------------------------------------------

function pageLoad() {

    let qs = getQueryStringParameters();
    let id = Number(qs["id"]);

    let venueHTML = '';

    fetch('/venue/get/' + id, {method: 'get'}
    ).then(response => response.json()
    ).then(venue => {

        document.getElementById("bookButton").valueOf()

            venueHTML += `<h1 class="venueName">${venue.name}</h1>` +
                `<div class="venueImage"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" width="100%"></div>` +
                `<div class="location">${venue.address}, ${venue.city}, ${venue.postcode}</div>` +
                `<div class="deats"><div class="description">${venue.description}</div> <div class="features">Capacity: ${venue.capacity} Price/hour: ${venue.priceHr}</div></div>`


        document.getElementById("listDetails").innerHTML = venueHTML;

        checkLogin();

    });


    const button = document.getElementById("bookButton");
    button.addEventListener("click", bookEvent);
    button.myParam = id;

}

function checkLogin() {

    let email = Cookies.get("email");

    let logInHTML = '';

    if (email === undefined) {

        logInHTML = "<a class='signIn' href='/client/login.html'><li>Sign in</li></a>";

    } else {

        logInHTML = "<a class='user' href='/client/user.html' style='padding: 0px'><li>My Profile</li></a>" +
            "<a class='user' href='/client/login.html?logout'><li>Logout</li></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

function bookEvent(event) {
    let id = event.currentTarget.myParam;
    window.location.href = 'event.html?venueID=' + id;
}
