const url = "http://localhost:8080/api"
const token = localStorage.getItem("token");
const user = JSON.parse(localStorage.getItem("user"))
function handleQty() {
    let proQty = $('.pro-qty');
    proQty.prepend('<span class="dec qtybtn">-</span>');
    proQty.append('<span class="inc qtybtn">+</span>');
    proQty.on('click', '.qtybtn', function () {
        var $button = $(this);
        var oldValue = $button.parent().find('input').val();
        if ($button.hasClass('inc')) {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 0) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 0;
            }
        }
        $button.parent().find('input').val(newVal);
    });
}

//preloader index
document.addEventListener('DOMContentLoaded', () => {
    home();
})

function home() {
    content();
    getCategories();
    preload();
    handleCartUpdate();
    handleImg()
    event.preventDefault();
}

const handleImg = () => {
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')')
    })
}

const handlePushCart = (id) => {
    $.ajax({
        headers: {
            "Accept": 'application/json',
            "Content-Type": "application/json"
        },
        type: "POST",
        url: `${url}/cart/${user.id}/${id}/1`,
        success: () => handleCartUpdate(),
        error: () => console.log("lỗi")
    })
    event.preventDefault();
}

function content() {
    const htmls = `<div class="hero__item set-bg" data-setbg="../static/img/banner.jpg">
                        <div class="hero__text">
                            <span>Hoa quả sạch</span>
                            <h2>Cam kết <br>100% Organic</h2>
                            <p>Giao hàng miễn phí trên toàn quốc</p>
                            <a onclick="store()" class="primary-btn">Xem thêm</a>
                        </div>
                    </div>
                    <section id="content-featured" class="featured spad"></section>`
    $('.context').html(htmls);
}

function handleCartUpdate() {
    $.ajax({
        type: 'GET',
        url: `${url}/cart/${user.id}`,
        success: (data) => {
            const htmls = `
             <ul>
<!--                    yêu thích-->
                <li><a href="#"><i class="fa fa-heart"></i> <span></span></a></li>
<!--                giỏ hàng-->
                <li><a onclick="cart()"><i class="fa fa-shopping-bag"></i> <span>${data.length}</span></a></li>
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
            handleImg()
        }
    })

}

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
                <h6><a>${product.name}</a></h6>
                <h5>$ ${product.price}</h5>
            </div>
        </div>
     </div>`
}

function cart() {
    event.preventDefault();
    breadcrumbStore("Giỏ hàng", "shoping-cart");
    shoppingCart();
    handleImg()
}

const breadcrumbStore = (title,classname) => {
    let htmls = `<section class="breadcrumb-section set-bg" data-setbg="../static/img/breadcrumb.jpg">
        <div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <div class="breadcrumb__text">
                        <h2>${title}</h2>
                        <div class="breadcrumb__option">
                            <a onclick="home()">Trang chủ</a>
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
                                <a onclick="home()" class="primary-btn cart-btn">Mua tiếp</a>
                                </div>
                            </div>
                            <div class="col-lg-6">
                                <div class="shoping__checkout">
                                   <h5>Giỏ hàng</h5>
                                       <ul>
                                           <li>Sản phẩm<span>${data.length}</span></li>
                                           <li>Tổng tiền
                                           <span>$ ${data.reduce((total, p) => 
                                                        {return total + p.quantity * p.product.price}, 0)}
                                           </span>
                                           </li>
                                       </ul>
                                   <a href="#" class="primary-btn">Thanh toán giỏ hàng</a>
                                </div>
                            </div>
                       </div>`
            $('.shoping-cart').html(htmls);
        }
    })
}

function displayCart(item) {
    return `
    <tr>
        <td class="shoping__cart__item">
            <img src="../static/img/${item.product.image}" alt="Lỗi ảnh">
                <h5>${item.product.name}</h5>
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

function removeItem(pid) {
    $.ajax({
        type: 'DELETE',
        url: `${url}/cart/${user.id}/${pid}`,
        success: () => shoppingCart()
    })
    event.preventDefault()
}

function store() {
    event.preventDefault();
    breadcrumbStore("Store","product");
    contentStore();
    handleImg();
}

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
                               <li>Hoa quả</li>
                               <li>Rau củ</li>
                               <li>Hạt khô</li>
                            </ul>
                        </div>
                        <div class="sidebar__item">
                            <h4>Giá cả</h4>
                            <div class="price-range-wrap">
                                <div class="price-range ui-slider ui-corner-all ui-slider-horizontal ui-widget ui-widget-content" data-min="10" data-max="999">
                                    <div class="ui-slider-range ui-corner-all ui-widget-header"></div>
                                    <span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 0%;"></span>
                                    <span tabindex="0" class="ui-slider-handle ui-corner-all ui-state-default" style="left: 100%;"></span>
                                <div class="ui-slider-range ui-corner-all ui-widget-header" style="left: 0%; width: 100%;"></div></div>
                                <div class="range-slider">
                                    <div class="price-input">
                                        <input type="text" id="minamount">
                                        <input type="text" id="maxamount">
                                    </div>
                                </div>
                            </div>
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
                                        <option value="0">Default</option>
                                    </select><div class="nice-select" tabindex="0"><span class="current">Default</span><ul class="list"><li data-value="0" class="option selected">Default</li><li data-value="0" class="option">Default</li></ul></div>
                                </div>
                            </div>
                            <div class="col-lg-4 col-md-4">
                                <div class="filter__found">
                                    <h6><span>16</span> Products found</h6>
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
                    <div class="row">
                       
                    </div>
                    <div class="product__pagination">
                        <a href="#">1</a>
                        <a href="#">2</a>
                        <a href="#">3</a>
                        <a href="#"><i class="fa fa-long-arrow-right"></i></a>
                    </div>
                </div>
            </div>    
        </div>`

    $('.product').html(htmls)
}
