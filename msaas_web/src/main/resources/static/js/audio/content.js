/** content.js **/
//"discover",
//"toplist",
//"playlist",
//"djradio",
//"artist",
//"album",
//"my_other",
//"frien_other"
// discover
function navTop(module){
    module !== "discover" && clearInterval(self.timer);
    self[module] && self[module].call(this, self.iframe.contentWindow.document);
}

// discover
self.discover = function (domain){
// 轮播图
(() => {
    var ibanner = H5("#index-banner", domain),
    a_img = H5(".wrap .ban .ban-img .active", ibanner),
    img = H5(" img", a_img),
    btnl = H5(".wrap .ban .btnl", ibanner),
    btnr = H5(".wrap .ban .btnr", ibanner),
    dots = H5(".wrap .ban .dots a", ibanner),
    data_imgs = [
        "/img/audio/discover/109951165003688094.jpg",
        "/img/audio/discover/109951165003773165.jpg",
        "/img/audio/discover/109951165003775261.jpg",
        "/img/audio/discover/109951165003785101.jpg",
        "/img/audio/discover/109951165003793161.jpg",
        "/img/audio/discover/109951165003813524.jpg",
        "/img/audio/discover/109951165003816642.jpg",
        "/img/audio/discover/109951165004813763.jpg"
    ],
    imgIndex = 0;

    initData(imgIndex);
    function initData(index) {
        img.css("transition", "none 0s ease 0s").css("opacity", .2);
        setTimeout(function () {
            img.attr("src", data_imgs[index]);
            img.css("transition", "opacity 1s ease 0s").css("opacity", 1);
        }, 100);
        a_img.attr("href", `/song?id=${data_imgs[index].substr(this.length - 11, 7)}`);
        dots.forEach(dot => {
            dot.rmvClass("z-slt");
        });
        dots.item(imgIndex).addClass("z-slt");
        ibanner.css("background-image", `url('${data_imgs[index]}')`);
    }
    btnl.on("click", function () {
        imgIndex = --imgIndex < 0 ? data_imgs.length - 1 : imgIndex;
        initData(imgIndex);
    });
    btnr.on("click", function () {
        imgIndex = ++imgIndex > data_imgs.length - 1 ? 0 : imgIndex;
        initData(imgIndex);
    });
    dots.forEach(dot => {
        dot.on("click", function () {
            initData(imgIndex = dot.attr("data-index"));
        });
    });
    self.timer = setInterval(function () {
        imgIndex = ++imgIndex > data_imgs.length - 1 ? 0 : imgIndex;
        initData(imgIndex);
    }, 10000);
})();

//album(新碟上架, 轮播);
(() => {
    var album = H5("#album-roller", domain),
        pre = H5("a.pre", album),
        uls = H5("ul.f-cb", album),
        nxt = H5("a.nxt", album),
        curIndex = H5("[style='left:0px;']", domain).index(),
        l_index = 0,
        r_index = 0;
    pre.on("click", function () {
        curIndex = --curIndex < 0 ? uls.length - 1 : curIndex;
        l_index = curIndex === 0 ? uls.length - 1 : curIndex - 1;
        r_index = curIndex === uls.length - 1 ? 0 : curIndex + 1;
        uls.item(l_index).css("transition", "none 0s ease 0s").css("left", "-645px");
        uls.item(curIndex).css("transition", "left 1s ease 0s").css("left", "0px");
        uls.item(r_index).css("transition", "left 1s ease 0s").css("left", "645px");
    });
    nxt.on("click", function () {
        curIndex = ++curIndex === uls.length ? 0 : curIndex;
        l_index = curIndex === 0 ? uls.length - 1 : curIndex - 1;
        r_index = curIndex === uls.length - 1 ? 0 : curIndex + 1;
        uls.item(l_index).css("transition", "left 1s ease 0s").css("left", "-645px");
        uls.item(curIndex).css("transition", "left 1s ease 0s").css("left", "0px");
        uls.item(r_index).css("transition", "none 0s ease 0s").css("left", "645px");
    });
})();
}

// playlist
self.playlist = function(domain){
(()=>{
    var cateToggleLink = H5("#cateToggleLink", domain),
    cateListBox = H5("#cateListBox", domain),
    bool = true;
    cateToggleLink.on("click", function(){
        bool ? (cateListBox.addClass("n-sltlyr-show"), bool = false) : (cateListBox.rmvClass("n-sltlyr-show"), bool = true);
    });
})();
}

// djradio
self.djradio = function(domain){
// 电台类型
(() => {
    var rditype = H5("#id-category-box", domain),
    uls = H5("ul.box.f-cb", rditype),
    turn_left = H5("span.turn-left", domain),
    turn_right = H5("span.turn-right", domain),
    dots = H5("div.dotpage span.dot", domain);

    turn_left.on("click", function(){
        !turn_left.hasClass("z-dis") && (
            uls.item(0).addClass("z-show"),
            uls.item(1).rmvClass("z-show"),
            turn_left.addClass("z-dis"),
            turn_right.rmvClass("z-dis"),
            dots.item(0).addClass("z-curr"),
            dots.item(1).rmvClass("z-curr")
        );
    });
    turn_right.on("click", function(){
        !turn_right.attr("class").includes("z-dis") && (
            uls.item(0).rmvClass("z-show"),
            uls.item(1).addClass("z-show"),
            turn_left.rmvClass("z-dis"),
            turn_right.addClass("z-dis"),
            dots.item(0).rmvClass("z-curr"),
            dots.item(1).addClass("z-curr")
        );
    });
})();
}