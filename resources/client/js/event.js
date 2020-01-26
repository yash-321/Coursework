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

function pageLoad() {

    checkLogin();

    let qs = getQueryStringParameters();
    let eventID = Number(qs["eventID"]);
    let venueID = Number(qs["venueID"])

    if (eventID != null) {

        fetch('/event/get/' + eventID, {method: 'get'}
        ).then(response => response.json()
        ).then(event => {

            if (event.hasOwnProperty('error')) {
                alert(event.error);
            } else {

                document.getElementById("email").value = Cookies.get("email");
                document.getElementById("eventID").value = eventID;
                document.getElementById("venueID").value = event.venueID;
                document.getElementById("catererID").value = event.catererID;
                document.getElementById("entertainerID").value = event.entertainerID;
                document.getElementById("date").value = event.date;
                document.getElementById("hours").value = event.hours;
                document.getElementById("people").value = event.people;
                document.getElementById("saveButton").innerText = 'Save';

            }
        });
    }

    if (venueID != null) {
        document.getElementById("email").value = Cookies.get("email")
        document.getElementById("venueID").value = venueID;
        document.getElementById("saveButton").innerText = 'Book';
    }


    document.getElementById("saveButton").addEventListener("click", saveEditEvent);
    document.getElementById("cancelButton").addEventListener("click", cancelEditEvent);

}

function checkLogin() {

    let email = Cookies.get("email");

    let logInHTML = '';

    if (email === undefined) {

        alert("Please login to book an event");
        window.location.href = 'login.html'

    } else {

        logInHTML = "<a class='user' href='/client/user.html' style='padding: 0px'><li>My Profile</li></a>" +
            "<a class='user' href='/client/login.html?logout'><li>Logout</li></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

function saveEditEvent(event) {

    event.preventDefault();

    if (document.getElementById("venueID").value.trim() === '') {
        alert("Please provide the venue ID.");
        return;
    }

    if (document.getElementById("date").value.trim() === '') {
        alert("Please provide a date.");
        return;
    }

    if (document.getElementById("hours").value.trim() === '') {
        alert("Please provide the number of hours the event will last.");
        return;
    }

    if (document.getElementById("people").value.trim() === '') {
        alert("Please provide the estimated attendance.");
        return;
    }

    const id = document.getElementById("eventID").value;
    const form = document.getElementById("eventForm");
    const formData = new FormData(form);

    let apiPath = '';
    if (id === '') {
        apiPath = '/event/new';
    } else {
        apiPath = '/event/update';
    }

    fetch(apiPath, {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            if (id === '') {
                alert("Event booked");
                window.location.href = 'user.html';
            } else {
                alert("Event updated");
                window.location.href = 'user.html';
            }
        }
    });
}


function cancelEditEvent(event) {
    event.preventDefault();
    history.go(-1);
}