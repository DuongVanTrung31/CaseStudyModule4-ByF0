function uploadFile() {
    let form = document.forms[1];
    let formData = new FormData(form);
    console.log(form)
    console.log(formData)
    $.ajax({
        contentType : false,
        processData : false,
        type: "POST",
        data: formData,
        url: "http://localhost:8080/api/products/upload",
        success: function (data) {
            localStorage.setItem("a", data)
        }
    });
}

function uploadFileFull() {
    let name = $('#name').val();
    let price = $('#price').val();
    let quantity = $('#quantity').val();
    let weight = $('#weight').val();
    let description = $('#description').val();
    let category = $('#category').val();
    let newProduct = {
        name: name,
        price: price,
        quantity: quantity,
        weight: weight,
        description: description,
        category: {
            id: category,
        }
    };
    let formData = new FormData();
    formData.append("file", $('#file')[0].files[0])
    formData.append("product", new Blob([JSON.stringify(newProduct)], {type : 'application/json'}))
    console.log(formData.get("file"))
    console.log(formData.get("product"))
    $.ajax({
        contentType : false,
        processData : false,
        mimeType: "multipart/form-data",
        type: "POST",
        data: formData,
        url: "http://localhost:8080/api/products/upload1",
        success: function (data) {
            getProduct()
        }

    });
    event.preventDefault();
}