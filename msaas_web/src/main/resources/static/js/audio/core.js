/** core **/
(() => {
    H5.ajax("get", null, "/getHtmlTemp", function (res){
        self.temp_box = H5.toHtml(res);
    });
    H5("#nav-login-bar").on("click", login);
})();

function login(){
    // cloneNode 跟 document.importNode 有异曲同工之妙
    var loginNode = H5("#ntp-login-nav", self.temp_box).cloneNode(!0),
    //var loginNode = document.importNode(H5("#ntp-login-nav", self.temp_box), !0),
    m_login = H5(".lyct .n-log2 .u-main a.u-btn2-2", loginNode),
    reg = H5(".lyct .n-log2 .u-main a.u-btn2-1", loginNode),
    lia = H5(".u-alt li a", loginNode),
    checkbox = H5("#j-official-terms", loginNode),
    _insertWin = self.fn.insertWin("登录", loginNode);


    m_login.on("click", function(){
        !checkbox.checked ? H5.toast("请勾选同意《服务条款》,《隐私政策》,《儿童隐私政策》", 1, 3e3) : (_insertWin.destory(), self.fn.mobile_login());
    });
    reg.on("click", function(){
        !checkbox.checked ? H5.toast("请勾选同意《服务条款》,《隐私政策》,《儿童隐私政策》", 1, 3e3) : (_insertWin.destory(), self.fn.register());
    });
    lia.forEach(a=>{
        a.on("click", function (){
            return !checkbox.checked ? (H5.toast("请勾选同意《服务条款》,《隐私政策》,《儿童隐私政策》", 1, 3e3), !1) : (_insertWin.destory(), !0);
        });
    });
}

self.fn.mobile_login = function (){ // ntp-login-mobile
    var mobileLoginNode = H5("#ntp-login-mobile", self.temp_box).cloneNode(!0),
    wgt_input_input = H5("#wgt-phone-input", self.temp_box).cloneNode(!0),
    current = H5(".u-phonewrap .current", wgt_input_input),
    j_code = H5(".j-code", current),
    uphone = H5("#p", wgt_input_input),
    upassword = H5("#pw", mobileLoginNode),
    uloptions = H5("ul.j-list", wgt_input_input),
    j_mob = H5(".lyct .n-log2 .j-mob", mobileLoginNode),

    // 获取input值,并做数据校验
    submitLogin = H5(".f-mgt20 a.j-primary", mobileLoginNode),
    otherLogin = H5(".js-btmbar.n-loglink2 .f-fl", mobileLoginNode),
    freeReg = H5(".js-btmbar.n-loglink2 .f-fr", mobileLoginNode),
    bool = !0,
    _insertWin = self.fn.insertWin("手机号登录", mobileLoginNode);

    current.on("click", function(){
        var wgt_countrycode_item = H5("#wgt-countrycode-item", self.temp_box);
        bool && H5.ajax("post", null, "/queryPhoneAreaCode", function(res){
            JSON.parse(res).data.forEach((o, i)=>{
                var newElem = wgt_countrycode_item.cloneNode(!0),
                li = H5("li", newElem),
                lt = H5(".lt", li),
                rt = H5(".rt", li);
                li.attr("data-index", i);
                lt.txt(o.areaname);
                rt.txt(o.areacode);
                li.on("click", function(){
                    j_code.txt(H5(".rt", li).txt());
                    uloptions.addClass("f-hide");
                });
                uloptions.clearChildren();
                uloptions.copyChildren(newElem);
                bool = !1;
            });
        });
        uloptions.rmvClass("f-hide");
    });
    j_mob.clearChildren();
    j_mob.copyChildren(wgt_input_input);

    submitLogin.on("click", function(){
        var str = `uphone=${j_code.txt()}${uphone.value}&upassword=${H5.encrypt(upassword.value)}`;
        H5.ajax("post", str, "/ulogin", function(res){
            var res = JSON.parse(res);
            (res && (res.status === 2000) && res.data.token) ? addUserInfo(res.data) : H5.toast(res.msg, 2);
        });

        function addUserInfo (info){
            self.token = info.token;
            var login_bar = H5("#nav-login-bar"),
            user_login = H5("#m-topbar-user-login", self.temp_box).cloneNode(!0),
            m_tlist = H5(".m-tlist.m-tlist-lged.j-uflag", user_login);
            H5(".head img", user_login).attr("src", info.uimg);
            H5("#nickname", user_login).txt(info.uname);

            login_bar.clearChildren();
            login_bar.copyChildren(user_login);
            login_bar.on("mouseenter", function(){
                m_tlist.show();
            }).on("mouseleave", function(){
                m_tlist.hide();
            });
            _insertWin.destory();
        }
    });
    otherLogin.on("click", function(){
        _insertWin.destory();
        login();
    });
    freeReg.on("click", function(){
        _insertWin.destory();
        self.fn.register();
    });
}

//注册
self.fn.register = function(){
    var mobileRegNode = H5("#ntp-reg-mobile", self.temp_box).cloneNode(!0),
    _wgt_input_input = H5("#wgt-phone-input", self.temp_box).cloneNode(!0),
    _current = H5(".u-phonewrap .current", _wgt_input_input),
    _j_code = H5(".j-code", _current),
    _uloptions = H5("ul.j-list", _wgt_input_input),
    _j_mob = H5(".lyct .n-log2 .j-mob", mobileRegNode),
    _bool = !0,

    nextStep = H5(".f-mgt20 .j-nextstep", mobileRegNode),
    gobackLogin = H5(".n-loglink2 .s-primary", mobileRegNode),
    _insertWin = self.fn.insertWin("手机号登录", mobileRegNode);

    _current.on("click", function(){
        var wgt_countrycode_item = H5("#wgt-countrycode-item", self.temp_box);
        _bool && H5.ajax("post", null, "/queryPhoneAreaCode", function(res){
            JSON.parse(res).data.forEach((o, i)=>{
                var newElem = wgt_countrycode_item.cloneNode(!0),
                li = H5("li", newElem),
                lt = H5(".lt", li),
                rt = H5(".rt", li);
                li.attr("data-index", i);
                lt.txt(o.areaname);
                rt.txt(o.areacode);
                li.on("click", function(){
                    _j_code.txt(H5(".rt", li).txt());
                    _uloptions.addClass("f-hide");
                });
                _uloptions.clearChildren();
                _uloptions.copyChildren(newElem);
                _bool = !1;
            });
        });
        _uloptions.rmvClass("f-hide");
    });
    _j_mob.clearChildren();
    _j_code.copyChildren(_wgt_input_input);

    nextStep.on("click", function(){
        H5.toast("下一步");
    });

    gobackLogin.on("click", function(){
        _insertWin.destory();
        login();
    });
}

// 弹窗
self.fn.insertWin = function (title, contentElem){
    var m_layer = H5(document.createElement("div")).attr("class", "m-layer z-show"),
    z_bar = H5(document.createElement("div")).addClass("zbar"),
    z_ttl = H5(document.createElement("div")).attr("class", "zttl f-thide").txt(title),
    z_content = H5(document.createElement("div")).addClass("zcnt"),
    z_close = H5(document.createElement("span")).txt("x").addClass("zcls").attr("title", "关闭窗体"),
    mask = H5(document.createElement("div")).addClass("m-mask").attr("style", "position:fixed; top: 0; bottom: 0; left: 0; right: 0; width: 100%; height: 100%;");
    z_bar.appendChild(z_ttl);

    z_content.clearChildren();
    z_content.copyChildren(contentElem);

    m_layer.appendChild(z_bar);
    m_layer.appendChild(z_content);
    m_layer.appendChild(z_close);
    document.body.appendChild(m_layer);
    document.body.appendChild(mask);

    // 初始化位置
    m_layer.css("top", `${Math.floor((innerHeight - m_layer.clientHeight)/2)}px`);
    m_layer.css("left", `${Math.floor((innerWidth - m_layer.clientWidth)/2)}px`);

    // 实现元素可以拖动的效果, 注意mosemove时间绑定在要移动的元素上时, 鼠标移动的太快, 会造成卡顿bug, 故将此事件直接的绑定到document上
    z_bar.on("mousedown", function (e){
        var x = e.clientX - m_layer.offsetLeft,
        y = e.clientY - m_layer.offsetTop,
        h = innerHeight - m_layer.clientHeight,
        w = innerWidth - m_layer.clientWidth,
        doc = H5(document);
        doc.on("mousemove", function (ev){
            var t = ev.clientY - y,
            l = ev.clientX - x,
            t = t<0 ? 0 : t>h ? h : t;
            l = l<0 ? 0 : l>w ? w : l;
            m_layer.css("left", `${l}px`);
            m_layer.css("top", `${t}px`);
        });
        doc.on("mouseup", function (){
            doc.on("mouseup", null).on("mousemove", null);
        });
    })

    z_close.on("click", destory);
    function destory(){
        m_layer.remove();
        mask.remove();
    }
    return {destory : destory};
}


self.iframe = H5("#g_iframe");
// index discover m-nav (对iframe进行内容填充) ----1
(() => {
    var ul = H5(".m-top .wrap .m-nav"),
    as = H5("li a", ul),
    m_subnav = H5("div.m-subnav.j-tflag"),
    iframe_win = self.iframe.contentWindow;
    as.forEach(a => {
        a.on("click", function(){
            !a.hasClass("z-slt") && (
                H5(".z-slt", ul).rmvClass("z-slt"),
                a.addClass("z-slt"),
                iframe_win.document.location = a.attr("href")
            );
            a.parent("fst")
             ?
            (m_subnav.item(0).addClass("f-hide"), m_subnav.item(1).rmvClass("f-hide"))
             :
            (m_subnav.item(0).rmvClass("f-hide"), m_subnav.item(1).addClass("f-hide"));
            return !1;
        });
    });
})();

// index discover nav (对iframe进行内容填充)
(() => {
    var as = H5("#g_nav2 .nav a"),
    iframe_win = self.iframe.contentWindow;
    self._url_iframe = getUrl(as.item(0));
    iframe_win.document.location = self._url_iframe;
    //iframContent(self._url_iframe);
    backtop(self.iframe);
    as.forEach(a => {
        a.on("click", function () {
            var url = getUrl(a);
            if(!self._url_iframe || self._url_iframe !== url){
                a.parent("nav").find(".z-slt").rmvAttr("class");
                a.attr("class", "z-slt");
                iframe_win.document.location = self._url_iframe = url;
                //iframContent(self._url_iframe = url);
                backtop(self.iframe);
            }
            return !1;
        });
    });
    // 不推荐
    //iframContent(self._url_iframe);
    function iframContent(url){
        var iframe_doc = iframe_win.document;
        H5.ajax("get", null, url, function (res) {
            iframe_doc.designMode = "on";
            iframe_doc.open();
            iframe_doc.write(res);
            iframe_doc.close();
            iframe_doc.designMode = "off";
        });
    }


    // 获取模型src
    function getUrl(_elem){
        return `${_elem.attr("href")}/${_elem.attr("data-module")}.html`;
    }

    // 返回顶部功能
    function backtop(_iframe){
        _iframe.on("load", function(){
            var h = Math.floor(document.documentElement.clientHeight / 2),
                m_back = H5(".m-back", iframe_win.document),
                footer = H5(".g-ft", iframe_win.document),
                top_doc = H5("div#g-topbar");
            m_back && (iframe_win.onscroll = function () {
                //滚动超出高度，显示按钮，否则隐藏
                var backtop = Math.max(iframe_win.document.documentElement.scrollTop, iframe_win.document.body.scrollTop); // 滚动超过半页就显示
                top_doc.css("top", -1 * backtop + "px");
                backtop > h ? m_back.show() : m_back.hide();
            },
            m_back.on("click", function () {
                top_doc.css("top", "0px");
                iframe_win.document.documentElement.scrollTop = 0;
            }));
            footer && H5.load(footer, "/footer.html");
        });
    }
})();


