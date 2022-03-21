let index = 0; //biến chứa id khi edit

//hàm hiển thị form tạo mới product data
function displayFormCreate() {
    document.getElementById("form1").reset()
    document.getElementById("form1").hidden = false;
    document.getElementById("form-button").onclick = function () {
        addNewProduct();
    }
    getCategory();
}

//hàm tạo mới product data
function addNewProduct() {
    //lấy dữ liệu
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
    // gọi ajax
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        data: JSON.stringify(newProduct),
        //tên API
        url: "http://localhost:8080/api/products",
        //xử lý khi thành công
        success: function () {
            getProduct();
        }

    });
    //chặn sự kiện mặc định của thẻ
    event.preventDefault();
}

//hàm lấy list category
function getCategory() {
    $.ajax({
        type: "GET",
        //tên API
        url: `http://localhost:8080/api/categories`,
        //xử lý khi thành công
        success: function (data) {
            let content = `<select id="category">`
            for (let i = 0; i < data.length; i++) {
                content += displayCategory(data[i]);
            }
            content += '</select>'
            document.getElementById('div-category').innerHTML = content;
        }
    });
}

//hàm hiển thị select list category
function displayCategory(category) {
    return `<option id="${category.id}" value="${category.id}">${category.name}</option>`;
}

//hàm hiển thị thông tin edit product
function editProduct(id) {
    $.ajax({
        type: "GET",
        //tên API
        url: `http://localhost:8080/api/products/${id}`,
        success: function (data) {
            $('#name').val(data.name);
            $('#price').val(data.price);
            $('#quantity').val(data.quantity);
            $('#weight').val(data.weight);
            $('#description').val(data.description);
            index = data.id;
            document.getElementById("form1").hidden = false;
            document.getElementById("form-button").onclick = function () {
                editProductPost()
            };
            getCategory();
            // getProductByPage(0)
        }
    });
}

//hàm cập nhật thông tin product data
function editProductPost() {
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
    $.ajax({
        contentType : false,
        processData : false,
        type: "PUT",
        data: JSON.stringify(newProduct),
        url: `http://localhost:8080/api/products/${index}`,
        success: function () {
            getProductByPage(0)
        }
    });
    event.preventDefault();
}

//hàm xóa 1 product data theo id
function deleteProduct(id) {
    $.ajax({
        type: "DELETE",
        url: `http://localhost:8080/api/products/${id}`,
        success: function () {
            getProductByPage(0)
        }
    });
}

//hàm hiển thị table product data
function displayProduct(product) {
    return `<tr><td>${product.name}</td><td>${product.price}</td><td>${product.quantity}</td><td>${product.weight}</td>
            <td>${product.description}</td><td><img src="../static/img/${product.image}" alt="Lỗi"></td><td>${product.category.name}</td>
            <td><button class="btn btn-danger" onclick="deleteProduct(${product.id})">Delete</button></td>
            <td><button class="btn btn-warning" onclick="editProduct(${product.id})">Edit</button></td></tr>`;
}

function displayProductHeader() {
    return `<tr><th>ProductName</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Weight</th>
            <th>Description</th>
            <th>Image</th>
            <th>Category</th>
            <th colspan="2">Action</th></tr>`
}

//hàm lấy list product
function getProduct() {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/products`,
        success: function (data) {
            // hiển thị danh sách
            let content = displayProductHeader();
            for (let i = 0; i < data.length; i++) {
                content += displayProduct(data[i]);
            }
            document.getElementById("productList").innerHTML = content;
            // document.getElementById("form").hidden = true;
        }
    });
}

//hàm lấy list product theo page
function getProductByPage(page) {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/products/page?page=${page}`,
        success: function (data) {
            let array = data.content
            let content = displayProductHeader();
            for (let i = 0; i < array.length; i++) {
                content += displayProduct(array[i]);
            }
            document.getElementById("productList").innerHTML = content;
            document.getElementById("displayPage").innerHTML = displayPage(data)
            // document.getElementById("form1").hidden = true
            document.getElementById("btnProductList").hidden = true;
            document.getElementById("displayPage").hidden = false;
            //điều kiện bỏ nút previous
            if (data.pageable.pageNumber === 0) {
                document.getElementById("backup").hidden = true
            }
            //điều kiện bỏ nút next
            if (data.pageable.pageNumber + 1 === data.totalPages) {
                document.getElementById("next").hidden = true
            }
        }
    });
}

//hàm hiển thị phần chuyển page
function displayPage(data){
    return `<button class="btn btn-primary" id="backup" onclick="isPrevious(${data.pageable.pageNumber})">Previous</button>
    <span>${data.pageable.pageNumber+1} | ${data.totalPages}</span>
    <button class="btn btn-primary" id="next" onclick="isNext(${data.pageable.pageNumber})">Next</button>`
}

//hàm lùi page
function isPrevious(pageNumber) {
    getProductByPage(pageNumber-1)
}

//hàm tiến page
function isNext(pageNumber) {
    getProductByPage(pageNumber+1)
}

//hàm tìm kiếm list product theo name gần đúng
function searchProduct() {
    let search = document.getElementById("search").value;
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/products/search?search=${search}`,
        success: function (data) {
            let content = displayProductHeader();
            for (let i = 0; i < data.length; i++) {
                content += displayProduct(data[i]);
            }
            document.getElementById('productList').innerHTML = content;
            document.getElementById("searchForm").reset()
        }
    });
    event.preventDefault();
}
function displayOrdersHeader() {
    return `<tr><th>Full Name</th>
            <th>Address</th>
            <th>Created At</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Note</th>
            <th>Status</th>
            </tr>`
}
function getOrdersByPage() {
    $.ajax({
        type: "GET",
        url: `http://localhost:8080/api/orders`,
        success: function (data) {
            let content = displayOrdersHeader();
            for (let i = 0; i < data.length; i++) {
                content += displayOrder(data[i]);
            }
            document.getElementById("productList").innerHTML = content;
            document.getElementById("displayPage").hidden = true;
            document.getElementById("form1").hidden = true;
            document.getElementById("btnProductList").hidden = false;
        }
    });
}
function displayOrder(order) {
    return `<tr><td>${order.fullName}</td><td>${order.address}</td><td>${order.createdAt}</td><td>${order.email}</td><td>${order.phone}</td>
            <td>${order.note}</td><td>${order.status}</td></tr>`;
}
document.getElementById("btnProductList").hidden = true;
getProductByPage(0)
getCategory()