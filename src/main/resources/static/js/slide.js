/*------------------
        Categories Dropdown
    --------------------*/

$('.hero__categories__all').on('click', function(){
    $('.hero__categories ul').slideToggle(400);
});

/*------------------
        Background Set
    --------------------*/

//Humberger Menu
$(".humberger__open").on('click', function () {
    $(".humberger__menu__wrapper").addClass("show__humberger__menu__wrapper");
    $(".humberger__menu__overlay").addClass("active");
    $("body").addClass("over_hid");
});

$(".humberger__menu__overlay").on('click', function () {
    $(".humberger__menu__wrapper").removeClass("show__humberger__menu__wrapper");
    $(".humberger__menu__overlay").removeClass("active");
    $("body").removeClass("over_hid");
});

/*------------------
    Navigation
--------------------*/
$(".mobile-menu").slicknav({
    prependTo: '#mobile-menu-wrap',
    allowParentLinks: true
});