function pageLoad() {

    if(window.location.search === '?logout') {
        document.getElementById('containment').innerHTML = '<h1>Logging out, please wait...</h1>';
        logout();
    } else {
        const loginButton = document.getElementById("loginButton");
        const registerButton = document.getElementById("registerButton");
        const signUpButton = document.getElementById('signUp');
        const signInButton = document.getElementById('signIn');
        const containment = document.getElementById('containment');


        loginButton.addEventListener("click", login);
        registerButton.addEventListener("click", register);

        signInButton.addEventListener('click', () => {
            containment.classList.remove("right-panel-active");
        });

        signUpButton.addEventListener('click', () => {
            containment.classList.add("right-panel-active");
        });
    }
}

function login(event) {

    event.preventDefault();

    const form = document.getElementById("loginForm");
    const formData = new FormData(form);

    fetch("/user/login", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            Cookies.set("firstname", responseData.firstname);
            Cookies.set("token", responseData.token);

            window.location.href = '/client/index.html';
        }
    });
}

function logout() {

    fetch("/user/logout", {method: 'post'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.remove("firstname");
            Cookies.remove("token");

            window.location.href = '/client/index.html';

        }
    });

}

function register(event) {

    event.preventDefault()

    const form = document.getElementById("registerForm");
    const formData = new FormData(form);

    fetch("/user/new",{method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            alert("Register Complete! Login to continue!")

            window.location.href = 'client/login.html';

        }
    });
}
