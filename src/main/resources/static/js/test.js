function getInputData(form) {
    let result = "";
    let inputs = form.getElementsByTagName("input");
    for (let inputIndex = 0; inputIndex < inputs.length; inputIndex++) {
        result += inputs[inputIndex].name;
        result += '=';
        result += inputs[inputIndex].value;
        if (inputIndex < inputs.length - 1) {
            result += '&';
        }
    }
    return result;
}

let gcontainer;

function insertHtmlData(str) {
    let container = document.createElement('container');
    gcontainer = container;
    container.innerHTML = str;
    document.getElementsByTagName('head')[0].remove();
    document.getElementsByTagName('body')[0].remove();
    document.firstElementChild.appendChild(container.getElementsByTagName('head')[0]);
    document.firstElementChild.appendChild(container.getElementsByTagName('body')[0]);
}

let gdata;
let forms = document.getElementsByClassName("form-extended");
let button;
for (let formIndex = 0; formIndex < forms.length; formIndex++) {
    let buttons = forms[formIndex].getElementsByClassName("form-submit-button-extended");
    button = buttons[0];
    for (let buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
        buttons[buttonIndex].addEventListener("click", function(event) {
            fetch('http://localhost:8080/test', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: getInputData(forms[formIndex]),
            })
            .then(response => response.text())
            .then(data => {
                gdata = data;
                insertHtmlData(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
        })
    }
}