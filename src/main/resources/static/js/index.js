const url = "http://localhost:8080/api"
const token = localStorage.getItem("token");
const user = JSON.parse(localStorage.getItem("user"))

//INIT WEB
document.addEventListener('DOMContentLoaded', () => {
    home();
})

// PAGES HOME
function home() {
    title();
    getCategories();
    preload();
    handleCartUpdate();
    handleImg()
    event.preventDefault();
}

//  LOAD-IMG
const handleImg = () => {
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')')
    })
}

//  PUSHING-TO-CART --> AJAX-CART
const handlePushCart = (id, qty = 1) => {
    $.ajax({
        headers: {
            "Accept": 'application/json',
            "Content-Type": "application/json"
        },
        type: "POST",
        url: `${url}/cart/${user.id}/${id}/${qty}`,
        success: () => handleCartUpdate(),
        error: () => console.log("lỗi")
    })
    event.preventDefault();
}

//  BANNER-HEADING
function title() {
    const htmls = `<div class="hero__item set-bg" data-setbg="../static/img/banner.jpg">
                        <div class="hero__text">
                            <span>Hoa quả sạch</span>
                            <h2>Cam kết <br>100% Organic</h2>
                            <p>Giao hàng miễn phí trên toàn quốc</p>
                            <a href=""  onclick="store()" class="primary-btn">Xem thêm</a>
                        </div>
                    </div>
                    <section id="content-featured" class="featured spad"></section>`
    $('.context').html(htmls);
}

//  UPDATE-CART-ICON --> AJAX-CART
function handleCartUpdate() {
    $.ajax({
        type: 'GET',
        url: `${url}/cart/${user.id}`,
        success: (data) => {
            const htmls = `
             <ul>
<!--                    yêu thích-->
                <li><a href=""><i class="fa fa-heart"></i> <span></span></a></li>
<!--                giỏ hàng-->
                <li><a href=""  onclick="cart()"><i class="fa fa-shopping-bag"></i> <span>${data.length}</span></a></li>
             </ul>
                 <div class="header__cart__price">Tổng tiền: <span>
                   ${data.reduce((total, p) => {
                return total + p.quantity * p.product.price
            }, 0)} $</span></div>
                
            `
            $('.header__cart').html(htmls);
        }
    })
    event.preventDefault();
}

//  HOME-CONTENT --> AJAX-GET-CATE
function getCategories() {
    $.ajax({
        type: 'GET',
        url: `${url}/categories`,
        success: (data) => {
            const values = data.map(c => {
                return `<li data-toggle="${c.id}" class="cate-${c.id}" onclick="handleCategory(${c.id})">${c.name}</li>`
            }).join("")
            const htmls =
                `
                <div class="row">
                        <div class="col-lg-12">
                            <div class="section-title">
                                <h2>Sản phẩm nổi bật</h2>
                            </div>
                        <div class="featured__controls">
                        <ul>
                        <li class="all active" onclick="preload()">Tất cả</li>
                        ${values}
                        </ul>
                        </div>
                   </div>
                </div>
                <div id="preloader"></div>`
            $('#content-featured').html(htmls)
        }
    });
}

//  LOAD-HOME-CONTENT-BY-CATE --> AJAX-PRODUCTS-CATE_ID
function handleCategory(id) {
    $.ajax({
        type: 'GET',
        url: `${url}/products/category/${id}`,
        success: (data) => {
            $('li.active').removeClass('active')
            $(`.cate-${id}`).addClass("active")
            const htmls = data.map(p => displayProducts(p)).join("")
            $('#preloader').html(`<div class="row featured__filter">${htmls}</div>`)
            handleImg()
        }
    });
}

//  LOAD-STORE-CONTENT-BY-CATE --> AJAX-PRODUCTS-CATE_ID
function storeCategory(id) {
    $.ajax({
        type: 'GET',
        url: `${url}/products/category/${id}`,
        success: (data) => {
            const htmls = data.map(p => displayProducts(p)).join("")
            breadcrumbStore("Store","product");
            contentStore();
            $('#show-store').html(htmls)
            handleImg()
        }
    });
    // event.preventDefault()
}

//  LOAD-HOME-CONTENT-INIT --> AJAX-PRODUCTS
function preload() {
    $.ajax({
        type: 'GET',
        url: `${url}/products`,
        success: (data) => {
            $('li.active').removeClass('active')
            $('.all').addClass("active")
            const htmls = data.map(p => displayProducts(p)).join("")
            $('#preloader').html(`<div class='row featured__filter'>${htmls}</div>`)
            //set ảnh tạm thời
            handleImg();
        }
    })

}

//  DISPLAY-PRODUCT
function displayProducts(product) {
    return `
    <div class="col-lg-3 col-md-4 col-sm-6">
        <div class="featured__item">
            <div class="featured__item__pic set-bg" data-setbg="../static/img/${product.image}">
                <ul class="featured__item__pic__hover">
                    <li><a href="#"><i class="fa fa-heart"></i></a></li>
                    <li><a><i class="fa fa-retweet"></i></a></li>
                    <li><a onclick="handlePushCart(${product.id})"><i class="fa fa-shopping-cart"></i></a></li>
                </ul>
            </div>
            <div class="featured__item__text">
                <h6><a onclick="detail(${product.id})">${product.name}</a></h6>
                <h5>$ ${product.price}</h5>
            </div>
        </div>
     </div>`
}

//  PAGES SHOPPING-CART
function cart() {
    event.preventDefault();
    breadcrumbStore("Giỏ hàng", "shoping-cart");
    shoppingCart();
    handleImg()
}

//  LOAD-BREADCRUMB-TITLE
const breadcrumbStore = (title,classname) => {
    let htmls = `<section class="breadcrumb-section set-bg" data-setbg="../static/img/breadcrumb.jpg">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <div class="breadcrumb__text">
                        <h2>${title}</h2>
                        <div class="breadcrumb__option">
                            <a href="" onclick="home()">Trang chủ</a>
                            <span>${title}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <section class="${classname} spad"></section>`
    $('.context').html(htmls);
}

//  LOAD-CART-CONTENT --> AJAX-CART
function shoppingCart() {
    $.ajax({
        type: 'GET',
        url: `${url}/cart/${user.id}`,
        success: (data) => {
            let tbody = data.map(item => displayCart(item)).join("")
            console.log(tbody);
            const htmls = `
                   <div class="container">
                       <div class="row">
                           <div class="col-lg-12">
                               <div class="shoping__cart__table">
                                   <table>
                                       <thead>
                                           <tr>
                                               <th class="shoping__product">Sản phẩm</th>
                                               <th>Giá</th>
                                               <th>Số lượng</th>
                                               <th>Tổng tiền</th>
                                               <th></th>
                                           </tr>
                                       </thead>
                                       <tbody>
                                           ${tbody}
                                       </tbody>
                                   </table>
                               </div>
                           </div>
                       </div>
                       <div class="row">
                            <div class="col-lg-6">
                                <div class="shoping__cart__btns">
                                    <a href="" onclick="home()" class="primary-btn cart-btn">Tiếp tục mua sắm</a>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="shoping__checkout">
                                   <h5>Giỏ hàng của bạn</h5>
                                       <ul>
                                           <li>Số lượng sản phẩm trong giỏ hàng<span>${data.length}</span></li>
                                           <li>Thành tiền 
                                           <span>$ ${data.reduce((total, p) => 
                                                        {return total + p.quantity * p.product.price}, 0)}
                                           </span>
                                           </li>
                                       </ul>
                                   <a href="" onclick="checkout()" class="primary-btn">Thanh toán giỏ hàng</a>
                                </div>
                            </div>
                       </div>`
            $('.shoping-cart').html(htmls);
        }
    })
}

//  DISPLAY-ITEM-CART
function displayCart(item) {
    return `
    <tr>
        <td class="shoping__cart__item">
            <img src="../static/img/${item.product.image}" alt="Lỗi ảnh">
                <h5 onclick="detail(${item.product.id})">${item.product.name}</h5>
        </td>
        <td class="shoping__cart__price">
            $ ${item.product.price}
        </td>
        <td class="shoping__cart__quantity">
            <div class="quantity">
                <div class="pro-qty">
                    <span class="dec qtybtn">-</span>
                    <input type="text" value="${item.quantity}">
                    <span class="inc qtybtn">+</span>
                </div>
            </div>
        </td>
        <td class="shoping__cart__total">
            $ ${item.product.price * item.quantity}
        </td>
        <td class="shoping__cart__item__close">
            <span class="icon_close" onclick="removeItem(${item.product.id})">&times;</span>
        </td>
    </tr`
}

//  REMOVE-ITEM-CART --> AJAX-CART
function removeItem(pid) {
    if(confirm("Bạn chắc chắn muốn xoá sản phẩm này ?")) {
        $.ajax({
            type: 'DELETE',
            url: `${url}/cart/${user.id}/${pid}`,
            success: () => {
                shoppingCart()
                handleCartUpdate()
            }
        })
        event.preventDefault()
    }
}

//  PAGES STORE-PRODUCT
function store() {
    event.preventDefault();
    breadcrumbStore("Store","product");
    contentStore();
    loadStore();
    handleImg();
}

//  STORE-CONTENT
function contentStore () {
            const htmls = `
        <div class="container">
        <div class="row">
            <div class="col-lg-3 col-md-5">
                <div class="sidebar">
                    <div class="sidebar">
                        <div class="sidebar__item">
                            <h4>Loại sản phẩm</h4>
                            <ul>
                               <li onclick="storeCategory(1)">Hoa quả</li>
                               <li onclick="storeCategory(2)">Rau củ</li>
                               <li onclick="storeCategory(3)">Hạt khô</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-9 col-md-7">
                    <div class="filter__item">
                        <div class="row">
                            <div class="col-lg-4 col-md-5">
                                <div class="filter__sort">
                                    <span>Sort By</span>
                                    <select style="display: none;">
                                        <option value="0">Default</option>
                                        <option value="1">Default</option>
                                    </select><div class="nice-select" tabindex="0"><span class="current">Default</span><ul class="list"><li data-value="0" class="option selected">Default</li><li data-value="0" class="option">Default</li></ul></div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-4">
                                <div class="filter__found">
                                    <h6><span></span> Sản phẩm</h6>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-3">
                                <div class="filter__option">
                                    <span class="icon_grid-2x2"></span>
                                    <span class="icon_ul"></span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row" id="show-store">
                    
                    </div>
                    <div class="product__pagination">
                    </div>
                </div>
            </div>    
        </div>`
    $('.product').html(htmls)
}

//  LOAD-STORE-PAGEABLE --> AJAX-PRODUCTS-PAGEABLE
function loadStore(page = 0) {
    $.ajax({
        type: 'GET',
        url: `${url}/products/page?page=${page}`,
        success: (data) => {
            const htmls = data.content.map(p => displayProducts(p)).join("");
            let page = ""
            for (let i = 0; i < data.totalPages; i++) {
                page += `<a href="" onclick="loadStore(${i})">${i+1}</a>`
            }
            $('.product__pagination').html(page);
            $('#show-store').html(htmls);
            handleImg();
    }
    })
    event.preventDefault();
}

// SEARCH-FORM AJAX-PRODUCTS-SEARCH
function handleSearch() {
    let key = $('#keyword').val()
    $.ajax({
        type: 'GET',
        url: `${url}/products/search?search=${key}`,
        success: (data) => {
            const htmls = data.map(p => displayProducts(p)).join("");
            breadcrumbStore("Store","product");
            contentStore();
            $('#show-store').html(htmls);
            handleImg();
            document.querySelector("#form-search").reset();
        }
    })
    event.preventDefault();
}

// PAGES PRODUCT-DETAIL --> AJAX-PRODUCTS-DETAIL
function detail(id) {
    $.ajax({
        type: 'GET',
        url: `${url}/products/${id}`,
        success: (data) => {
            let check = data.quantity < 1 ? "Hết hàng" : "Còn hàng";
            const htmls = `
        <div class="container">
            <div class="row">
                <div class="col-lg-6 col-md-6">
                    <div class="product__details__pic">
                        <div class="product__details__pic__item">
                            <img class="product__details__pic__item--large" src="../static/img/${data.image}" alt="">
                        </div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6">
                    <div class="product__details__text">
                        <h3>${data.name}</h3>
                        <div class="product__details__rating">
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star"></i>
                            <i class="fa fa-star-half-o"></i>
                            <span>(18 reviews)</span>
                        </div>
                        <div class="product__details__price">$ ${data.price}</div>
                        <p>${data.description}</p>
                        <div class="product__details__quantity">
                            <div class="quantity">
                                <div class="pro-qty"><span class="dec qtybtn">-</span>
                                    <input type="text" value="1">
                                <span class="inc qtybtn">+</span></div>
                            </div>
                        </div>
                        <a href="" onclick="handlePushCart(${id})" class="primary-btn">Thêm vào giỏ hàng</a>
                        <a href="" class="heart-icon"><span class="icon_heart_alt"></span></a>
                        <ul>
                            <li><b>Tình trạng:</b> <span> ${check}</span></li>
                            <li><b>Giao hàng: </b> <span>01 ngày <samp> Miễn phí</samp></span></li>
                            <li><b>Trọng lượng:</b> <span> ${data.weight} kg</span></li>
                            <li><b>Share on</b>
                                <div class="share">
                                    <a href=""><i class="fa fa-facebook"></i></a>
                                    <a href=""><i class="fa fa-twitter"></i></a>
                                    <a href=""><i class="fa fa-instagram"></i></a>
                                    <a href=""><i class="fa fa-pinterest"></i></a>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-12">
                    <div class="product__details__tab">
                        <ul class="nav nav-tabs" role="tablist">
                            <li class="nav-item">
                                <a class="nav-link active" data-toggle="tab" href="#tabs-1" role="tab" aria-selected="true">Đánh giá</a>
                            </li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tabs-1" role="tabpanel">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>`
            breadcrumbStore("Chi tiết","product-details")
            handleImg();
            $('.product-details').html(htmls);
        }
    })
    event.preventDefault();
}

// REVIEWS --> AJAX-REVIEW-PRODUCT_ID
function handleReviews (id) {
    $.ajax({
        type: 'GET',
        url: `${url}/review/${id}`,
        success: (data) => {
            const htmls = `<div class="product__details__tab__desc">
                                    <h6>Products Infomation</h6>
                                    <p></p>
                                </div>`
        }
    })
}

// PAGES CHECKOUT
function checkout() {
    breadcrumbStore("Thanh toán","checkout")
    contentCheckout();
    handleImg();
    event.preventDefault()
}

// CONTENT-CHECKOUT --> AJAX-CART
function contentCheckout() {
    $.ajax({
        type: 'GET',
        url: `${url}/cart/${user.id}`,
        success: (data) => {
            let sum = data.reduce((total, p) => total + p.quantity * p.product.price, 0)
            const cart = data.map(item =>
                `<li>${item.product.name} x ${item.quantity}<span>$ ${item.product.price * item.quantity}</span></li>`
            ).join("")
            const htmls = `
                        <div class=container>
                            <div class="checkout__form">
                                <h4>Thông tin giao hàng</h4>
                                <form>
                                    <div class="row">
                                        <div class="col-lg-8 col-md-6">
                                            <div class="row">
                                                <div class="col-lg-12">
                                                    <div class="checkout__input">
                                                        <p>Họ và tên<span>*</span></p>
                                                        <input type="text" id="fullname" placeholder="Nhập họ và tên">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="checkout__input">
                                                <p>Địa chỉ nhận hàng<span>*</span></p>
                                                <input type="text" id="address" placeholder="Nhập địa chỉ" class="checkout__input__add">
                                            </div>
                                            <div class="row">
                                                <div class="col-lg-6">
                                                    <div class="checkout__input">
                                                        <p>Số điện thoại<span>*</span></p>
                                                        <input type="text" id="phone" placeholder="Nhập số điên thoại">
                                                    </div>
                                                </div>
                                                <div class="col-lg-6">
                                                    <div class="checkout__input">
                                                        <p>Email<span>*</span></p>
                                                        <input type="text" id="email" placeholder="Nhập email">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="checkout__input">
                                                <p>Ý kiến thêm<span>*</span></p>
                                                <input type="text" id="note" placeholder="Ghi chú thêm">
                                            </div>
                                        </div>
                                        <div class="col-lg-4 col-md-6">
                                            <div class="checkout__order">
                                                <h4>Hoá đơn của bạn</h4>
                                                <div class="checkout__order__products">Tên sản phẩm <span>Tổng tiền</span></div>
                                                <ul>
                                                    ${cart}
                                                </ul>
                                                <div class="checkout__order__subtotal">Ước tính <span>$ ${sum}</span></div>
                                                <div class="checkout__order__total">Thành tiền <span>$ ${sum}</span></div>
                                                <div class="checkout__input__checkbox">
                                                    <label for="payment">
                                                        Check Payment
                                                        <input type="checkbox" id="payment">
                                                        <span class="checkmark"></span>
                                                    </label>
                                                </div>
                                                <div class="checkout__input__checkbox">
                                                    <label for="paypal">
                                                        Paypal
                                                        <input type="checkbox" id="paypal">
                                                        <span class="checkmark"></span>
                                                    </label>
                                                </div>
                                                <button type="submit" onclick="handleCheckoutSubmit()" class="site-btn">Thanh toán</button>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>`
            $('.checkout').html(htmls)
        }
    })
    event.preventDefault()
}

// CHECKOUT ORDER --> AJAX-CART-PAY
function handleCheckoutSubmit () {
    if(confirm("Hãy đồng ý để xác nhận thanh toán")) {
        let fullName = $('#fullname').val()
        let address = $('#address').val()
        let phone = $('#phone').val()
        let email = $('#email').val()
        let note = $('#note').val()
        const order = {
            fullName: fullName,
            address: address,
            email: email,
            phone: phone,
            note: note
        }
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: `${url}/pay/${user.id}`,
            data: JSON.stringify(order),
            success: (resp) => {
                alert("Đặt hàng thành công, kiểm tra email để biết thêm chi tiết")
                handleCartUpdate();
                checkout();
            }
        })
        event.preventDefault()
    }
}

// FORM LOGIN MODAL
function loginModal() {
    const htmls = `
    <div class="form__modal">
        <div class="form__header">
            <h3 class="form-title">Đăng nhập</h3>
        </div>
        <label class="form-text">Tên tài khoản :</label>
        <input type="text"
                class="form__input"
                placeholder="Nhập tên tài khoản"
                id="username" required
        >
        <label class="form-text">Mật khẩu :</label>
        <input type="password"
                class="form__input"
                placeholder="Nhập mật khẩu tài khoản"
                id="password" required
        >
        <span id="error_login"></span>
        <div class="form__checkbox">
            <input type="checkbox" />
            <small>Nhớ mật khẩu</small>
        </div>
        <button onclick="handleLogin()"
                class="btn-login"
        >Đăng nhập
        </button>
        <p>Bạn chưa có tài khoản ? <a onclick="signUpForm()">Đăng ký ngay !</a> </p>
    </div>`
    $('#login-modal').html(htmls);
    $('#login-modal').attr("hidden",false);
    event.preventDefault()
}

// AUTHORITY --> AJAX AUTH
function handleLogin() {
    let username = $('#username').val();
    let password = $('#password').val();
    let account = {
        username: username,
        password: password
    };
    $.ajax({
        url: `http://localhost:8080/api/auth/login`,
        type: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-type': 'application/json'
        },
        data: JSON.stringify(account),
        success: function (data) {
            console.log(data);
            if (data.status === 202) {
                document.getElementById("error_login").innerHTML = "Tài khoản hoặc mật khẩu không đúng!"
                return false;
            } else {
                if(data.role[0].authority == "ROLE_USER") {
                    $('#link-login').remove();
                    $('#login-modal').remove();
                    localStorage.setItem("token", data.token);
                    localStorage.setItem("user", JSON.stringify(data));
                    $('.header__top__right__auth').html(`<a style="color: #7fad39; font-weight: bold" >Chào mừng ${data.fullName}</a>`);
                } else {
                    window.location.href = "admin.html";
                }
            }
        }
    });
    event.preventDefault();
}

// SIGNUP FORM
function signUpForm() {
    const htmls = `
    <div class="form__modal">
        <div class="form__header">
            <h3 class="form-title">Đăng ký</h3>
        </div>
        <span id="error_login"></span>
        <label class="form-text">Tên tài khoản :</label>
        <input type="text"
                class="form__input"
                placeholder="Nhập tên tài khoản"
                id="username" required
        >
        <label class="form-text">Mật khẩu :</label>
        <input type="password"
                class="form__input"
                placeholder="Nhập mật khẩu tài khoản"
                id="password" required
        >
        <label class="form-text">Email :</label>
        <input type="email"
                class="form__input"
                placeholder="Nhập email đăng ký"
                id="email" required
        >
         <label class="form-text">Họ và tên :</label>
        <input type="text"
                class="form__input"
                placeholder="Nhập họ và tên đăng ký"
                id="fullname" required
        >
        <button onclick="handleSignUp()"
                class="btn-login"
        >Đăng ký
        </button>
    </div>`
    $('#login-modal').html(htmls);
}

// --> AJAX AUTH SIGNUP
function handleSignUp() {
    let username = $('#username').val();
    let password = $('#password').val();
    let email = $('#email').val();
    let fullName = $('#fullname').val();
    let roleName = "ROLE_USER";
    const signup = {
        username: username,
        password: password,
        email: email,
        fullName: fullName,
        roleName: roleName
    }
    $.ajax({
        url: `http://localhost:8080/api/auth/signup`,
        type: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-type': 'application/json'
        },
        data: JSON.stringify(signup),
        success: (data) => {
            if(data === 201) {
                document.getElementById("error_login").innerHTML = "Tài khoản đã tồn tại!"
            } else {
                alert("Đăng ký tài khoản thành công");
                $('.form__modal').remove();
                loginModal();
            }
        }
    })
    event.preventDefault()
}