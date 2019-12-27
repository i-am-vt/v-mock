$(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function ($form) {
        // reg
        if($form.id === "regForm"){
            register();
            return;
        }
        // login
        login();
    }
});

function login() {
    $.modal.loading($("#btnSubmit").data("loading"));
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    $.ajax({
        type: "post",
        url: ctx + "login",
        data: {
            "username": username,
            "password": password,
            "validateCode": validateCode
        },
        success: function (r) {
            if (r.code == 0) {
                location.href = ctx + 'index';
            } else {
                $.modal.closeLoading();
                $('.imgcode').click();
                $(".code").val("");
                $.modal.msg(r.msg);
            }
        }
    });
}

function register() {
    $.modal.loading("正在为您注册，请稍后....");
    var username = $.common.trim($("input[name='regUsername']").val());
    var invitationCode = $.common.trim($("input[name='invitationCode']").val());
    var password = $.common.trim($("input[name='regPassword']").val());
    $.ajax({
        type: "post",
        url: ctx + "register",
        data: {
            "username": username,
            "password": password,
            "invitationCode": invitationCode
        },
        success: function (r) {
            if (r.code == 0) {
                alert("注册成功！");
                location.href = ctx + 'login';
            } else {
                $.modal.closeLoading();
                $.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    });
    // reg fome check
    $("#regForm").validate({
        rules: {
            username: {
                required: true
            },
            invitationCode: {
                required: true
            },
            password: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            invitationCode: {
                required: icon + "请输入您的邀请码",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}

var Auth = {
	vars: {
		lowin: document.querySelector('.lowin'),
		lowin_brand: document.querySelector('.lowin-brand'),
		lowin_wrapper: document.querySelector('.lowin-wrapper'),
		lowin_login: document.querySelector('.lowin-login'),
		lowin_wrapper_height: 0,
		login_link: document.querySelector('.login-link'),
		login_btn: document.querySelector('.login-btn'),
		register_link: document.querySelector('.register-link'),
		password_group: document.querySelector('.password-group'),
		password_group_height: 0,
		lowin_register: document.querySelector('.lowin-register'),
		lowin_footer: document.querySelector('.lowin-footer'),
		box: document.getElementsByClassName('lowin-box'),
		option: {}
	},
	register(e) {
		Auth.vars.lowin_login.className += ' lowin-animated';
		setTimeout(() => {
			Auth.vars.lowin_login.style.display = 'none';
		}, 500);
		Auth.vars.lowin_register.style.display = 'block';
		Auth.vars.lowin_register.className += ' lowin-animated-flip';

		Auth.setHeight(Auth.vars.lowin_register.offsetHeight + Auth.vars.lowin_footer.offsetHeight);

		e.preventDefault();
	},
	login(e) {
		Auth.vars.lowin_register.classList.remove('lowin-animated-flip');
		Auth.vars.lowin_register.className += ' lowin-animated-flipback';
		Auth.vars.lowin_login.style.display = 'block';
		Auth.vars.lowin_login.classList.remove('lowin-animated');
		Auth.vars.lowin_login.className += ' lowin-animatedback';
		setTimeout(() => {
			Auth.vars.lowin_register.style.display = 'none';
		}, 500);

		setTimeout(() => {
			Auth.vars.lowin_register.classList.remove('lowin-animated-flipback');
			Auth.vars.lowin_login.classList.remove('lowin-animatedback');
		},1000);

		Auth.setHeight(Auth.vars.lowin_login.offsetHeight + Auth.vars.lowin_footer.offsetHeight);

		e.preventDefault();
	},
	setHeight(height) {
		Auth.vars.lowin_wrapper.style.minHeight = height + 'px';
	},
	brand() {
		Auth.vars.lowin_brand.classList += ' lowin-animated';
		setTimeout(() => {
			Auth.vars.lowin_brand.classList.remove('lowin-animated');
		}, 1000);
	},
	init(option) {
		Auth.setHeight(Auth.vars.box[0].offsetHeight + Auth.vars.lowin_footer.offsetHeight);

		Auth.vars.password_group.style.height = Auth.vars.password_group.offsetHeight + 'px';
		Auth.vars.password_group_height = Auth.vars.password_group.offsetHeight;
		Auth.vars.lowin_wrapper_height = Auth.vars.lowin_wrapper.offsetHeight;

		Auth.vars.option = option;
		//Auth.vars.lowin_login.querySelector('form').setAttribute('action', option.login_url);

		var len = Auth.vars.box.length - 1;

		for(var i = 0; i <= len; i++) {
			if(i !== 0) {
				Auth.vars.box[i].className += ' lowin-flip';
			}
		}

		Auth.vars.register_link.addEventListener("click", (e) => {
			Auth.brand();
			Auth.register(e);
		});

		Auth.vars.login_link.addEventListener("click", (e) => {
			Auth.brand();
			Auth.login(e);
		});
	}
}