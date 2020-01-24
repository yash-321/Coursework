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

    let qs = getQueryStringParameters();
    let id = Number(qs["id"]);


    document.getElementById("saveButton").addEventListener("click", saveEditFruit);
    document.getElementById("cancelButton").addEventListener("click", cancelEditFruit);

}


function editFruit(event) {

    const id = event.target.getAttribute("data-id");

    if (id === null) {

        document.getElementById("editHeading").innerHTML = 'Add new fruit:';

        document.getElementById("fruitId").value = '';
        document.getElementById("fruitName").value = '';
        document.getElementById("fruitImage").value = '';
        document.getElementById("fruitColour").value = '';
        document.getElementById("fruitSize").value = '';

        document.getElementById("listDiv").style.display = 'none';
        document.getElementById("editDiv").style.display = 'block';

    } else {

        fetch('/fruit/get/' + id, {method: 'get'}
        ).then(response => response.json()
        ).then(fruit => {

            if (fruit.hasOwnProperty('error')) {
                alert(fruit.error);
            } else {

                document.getElementById("editHeading").innerHTML = 'Editing ' + fruit.name + ':';

                document.getElementById("fruitId").value = id;
                document.getElementById("fruitName").value = fruit.name;
                document.getElementById("fruitImage").value = fruit.image;
                document.getElementById("fruitColour").value = fruit.colour;
                document.getElementById("fruitSize").value = fruit.size;

                document.getElementById("listDiv").style.display = 'none';
                document.getElementById("editDiv").style.display = 'block';

            }

        });

    }

}

function saveEditFruit(event) {

    event.preventDefault();

    if (document.getElementById("fruitName").value.trim() === '') {
        alert("Please provide a fruit name.");
        return;
    }

    if (document.getElementById("fruitImage").value.trim() === '') {
        alert("Please provide a fruit image.");
        return;
    }

    if (document.getElementById("fruitColour").value.trim() === '') {
        alert("Please provide a fruit colour.");
        return;
    }

    if (document.getElementById("fruitSize").value.trim() === '') {
        alert("Please provide a fruit size.");
        return;
    }

    const id = document.getElementById("fruitId").value;
    const form = document.getElementById("fruitForm");
    const formData = new FormData(form);

    let apiPath = '';
    if (id === '') {
        apiPath = '/fruit/new';
    } else {
        apiPath = '/fruit/update';
    }

    fetch(apiPath, {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            document.getElementById("listDiv").style.display = 'block';
            document.getElementById("editDiv").style.display = 'none';
            pageLoad();
        }
    });

}

function cancelEditFruit(event) {

    event.preventDefault();

    window.location.href = '/client/index.html';

}

