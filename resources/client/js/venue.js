
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
    ).then(venues => {

            venueHTML += `<div class="venueOuterDiv">` +
                `<div class="venueImage"><img src="/client/img/${venue.image}" alt="Picture of ${venue.name}" width="100%" height="200px"></div>` +
                `<div class="venueInnerDiv">${venue.name}</div>` +
                `<div class="city">${venue.city}</div>` +
                `<div class="capacity">${venue.capacity}</div>` +
                `</div>`;


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