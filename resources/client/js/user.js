function pageLoad(){



    fetch('/user/get', {method: 'get'}
    ).then(response => response.json()
    ).then(user => {

        if (user.hasOwnProperty('error')) {
            alert(user.error);
        } else {

            document.getElementById("email").value = Cookies.get("email");
            document.getElementById("firstname").value = user.firstname;
            document.getElementById("surname").value = user.surname;
            document.getElementById("postcode").value = user.postcode;

        }
    });


    let eventsHTML = `<table>` +
        '<tr>' +
        '<th>Id</th>' +
        '<th>Venue</th>' +
        '<th>Caterer</th>' +
        '<th>Entertainment</th>' +
        '<th>Date</th>' +
        '<th>Hours</th>' +
        '<th>Attendees</th>' +
        '<th class="last">Options</th>' +
        '</tr>';

    let email = Cookies.get("email");

    fetch('/event/list/' + email, {method: 'get'}
    ).then(response => response.json()
    ).then(events => {


        for (let event of events) {

            eventsHTML += `<tr>` +
                `<td>${event.eventID}</td>` +
                `<td>${event.venueID}</td>` +
                `<td>${event.catererID}</td>` +
                `<td>${event.entertainerID}</td>` +
                `<td>${event.date}</td>` +
                `<td>${event.hours}</td>` +
                `<td>${event.people}</td>` +
                `<td class="last">` +
                `<button class='editButton' data-id='${event.eventID}'>Edit</button>` +
                `<button class='deleteButton' data-id='${event.eventID}'>Delete</button>` +
                `</td>` +
                `</tr>`;

        }

        eventsHTML += '</table>';

        document.getElementById("events").innerHTML = eventsHTML;


        let editButtons = document.getElementsByClassName("editButton");
        for (let button of editButtons) {
            button.addEventListener("click", editEvent);
        }

        let deleteButtons = document.getElementsByClassName("deleteButton");
        for (let button of deleteButtons) {
            button.addEventListener("click", deleteEvent);
        }

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

    document.getElementById("saveButton").addEventListener("click", saveEditUser);
    document.getElementById("cancelButton").addEventListener("click", cancelEditUser);
    document.getElementById("deleteUser").addEventListener("click", deleteUser);
}

function checkLogin() {

    let email = Cookies.get("email");

    let logInHTML = '';

    if (email === undefined) {
        alert("Please login");
        window.location.href = 'login.html'

        logInHTML = "<a class='user' href='/client/login.html' style='padding: 0px'><li>Sign in</li></a>";

    } else {

        logInHTML = "<a class='user' href='/client/user.html' style='padding: 0px'><li>My Profile</li></a>" +
            "<a class='user' href='/client/login.html?logout'><li>Logout</li></a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}

function deleteEvent(event) {

    const ok = confirm("Are you sure?");

    if (ok === true) {

        let id = event.target.getAttribute("data-id");
        let formData = new FormData();
        formData.append("eventID", id);

        fetch('/event/delete', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    pageLoad();
                }
            }
        );
    }
}

function editEvent(event) {

    let id = event.target.getAttribute("data-id");
    window.location.href = 'event.html?eventID=' + id;
}


function saveEditUser(event) {
    event.preventDefault();

    if (document.getElementById("firstname").value.trim() === '') {
        alert("Please provide a first name.");
        return;
    }

    if (document.getElementById("surname").value.trim() === '') {
        alert("Please provide a last name.");
        return;
    }

    const form = document.getElementById("userForm");
    const formData = new FormData(form);

    fetch('/user/update', {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            alert("Details updated");
            pageLoad();
        }
    });
}

function cancelEditUser(event) {

    event.preventDefault();
    pageLoad()
}

function deleteUser() {

    const ok = confirm("Are you sure?");

    if (ok === true) {

        let email = Cookies.get("email");
        let formData = new FormData();
        formData.append("email", email);

        fetch('/user/delete', {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {

                if (responseData.hasOwnProperty('error')) {
                    alert(responseData.error);
                } else {
                    Cookies.remove("firstname");
                    Cookies.remove("email");
                    Cookies.remove("token");
                    alert("Account successfully deleted!");
                    window.location.href = 'index.html';
                }
            }
        );
    }

}