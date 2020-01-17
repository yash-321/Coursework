
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

    alert ("you are looking at venue " + id);

    let venueHTML = '';

    fetch('/venue/get/' + id, {method: 'get'}
    ).then(response => response.json()
    ).then(venue => {

            venueHTML += `<h1 class="title">${venue.name}</h1>` +
                `<div class="venueImage"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" width="100%" height="500px"></div>` +
                `<div class="location">${venue.address}, ${venue.city}, ${venue.postcode}</div>` +
                `<div class="description">${venue.description}</div> <div class="deats">${venue.capacity} ${venue.priceHr}</div>`


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