function login() {
    let username = $('#username').val();
    let password = $('#password').val();
    if (username === "") {
        document.getElementById("error_login").innerHTML = "Tài khoản không được để trống";
        return false;
    }
    if (password === "") {
        document.getElementById("error_login").innerHTML = "Vui lòng điền mật khẩu !";
        return false;
    }
    let data = {
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
        data: JSON.stringify(data),
        success: function (data) {
            if (data.status === 202) {
                document.getElementById("error_login").innerHTML = "Tài khoản hoặc mật khẩu không đúng!"
                return false;
            } else {
                localStorage.setItem("token", data.token);
                localStorage.setItem("user", JSON.stringify(data));
                window.location.href = "home.html";
            }
        }
    });
    event.preventDefault();
}