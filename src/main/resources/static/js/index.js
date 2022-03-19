
const url = "http://localhost:8080/api"

function content() {
    const htmls = `<div class="hero__item set-bg" data-setbg="../static/img/banner.jpg">
                        <div class="hero__text">
                            <span>Hoa quả sạch</span>
                            <h2>Cam kết <br>100% Organic</h2>
                            <p>Giao hàng miễn phí trên toàn quốc</p>
                            <a href="#" class="primary-btn">Xem thêm</a>
                        </div>
                    </div>
                    <section id="content-featured" class="featured spad"></section>`
    $('.context').html(htmls);
}

//preloader index
document.addEventListener('DOMContentLoaded', () => {
    content();
    getCategories()
    preload();
})


function handleCart() {
    $.ajax({
        type: 'GET',
        url: `${url}/cart`,
        success:(data) => {
            const htmls = `
             <ul>
                <li><a href="#"><i class="fa fa-heart"></i> <span>${data}</span></a></li>
                <li><a href="#"><i class="fa fa-shopping-bag"></i> <span>${data}</span></a></li>
             </ul>
                 <div class="header__cart__price">Tổng tiền: <span>${data}</span></div>
            `
            $('.header__cart').html(htmls);
        }
    })
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

function handleCategory(id){
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
    });}

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


const handleImg = () =>  {
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')')
    })
}



function displayProducts(product) {
    return `
    <div class="col-lg-3 col-md-4 col-sm-6">
        <div class="featured__item">
            <div class="featured__item__pic set-bg" data-setbg="../static/img/${product.image}">
                <ul class="featured__item__pic__hover">
                    <li><a href="#"><i class="fa fa-heart"></i></a></li>
                    <li><a href="#"><i class="fa fa-retweet"></i></a></li>
                    <li><a href="#"><i class="fa fa-shopping-cart"></i></a></li>
                </ul>
            </div>
            <div class="featured__item__text">
                <h5><a href="#">${product.name}</a></h5>
                <h5>$ ${product.price}</h5>
            </div>
        </div>
     </div>`
}