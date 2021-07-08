window.onload = function() {
let toastElList = [].slice.call(document.querySelectorAll('.toast'))
let toastList = toastElList.map(function (toastEl) {
return new bootstrap.Toast(toastEl, {"autohide" : false})
})
toastList.forEach(toast => {
  toast.show();
});
}