// js tools
(function(fn, factory) {
    factory.H5 = fn.call();
    factory.self = new Object;
    self.fn = new Object;
})(function() {
    // 扩展功能
    const HExpand = {
        attr : {
            value : function (key, val){
                return (arguments.length === 2 && (this.setAttribute(key, val), this)) || this.getAttribute(key);
            }
        },
        rmvAttr : {
            value : function (name){
                return arguments.length && this.removeAttribute(name), this;
            }
        },
        addClass : {
            value : function (classname){
                return arguments.length === 1 && this.classList.add(classname), this;
            }
        },
        rmvClass : {
            value : function (classname){
                return arguments.length === 1 && this.classList.contains(classname) && this.classList.remove(classname), this;
            }
        },
        hasClass : {
            value : function (classname){
                return this.classList.contains(classname);
            }
        },
        html : {
            value : function (val){
                return (arguments.length && (this.innerHTML = val, this)) || this.innerHTML;
            }
        },
        txt : {
            value : function (val){
                return (arguments.length && (this.textContent = val, this)) || this.textContent;
            }
        },
        on : {
            value : function (type, listener){
                return arguments.length && (this[`on${type.toLowerCase()}`] = listener), this;
            }
        },
        css : {
            value : function (name, val){
                return (arguments.length === 2 && ((val && (this.style[name] = val, this))||(this.style.removeProperty(name), this))) || this.style[name];
            }
        },
        animate : { // 还在完善中
            value : function (attrjson){
                return (arguments.length && HTool.animation(this, attrjson)), this;
            }
        },
        show : {
            value : function (){
                return this.css("display", this.disply), this;
            }
        },
        hide : {
            value : function (){
                return this.css("display") !== "none" && (this.disply = this.css("display"), this.css("display", "none")), this;
            }
        },
        find : {
            value : function (selector){
                return HFive(selector, this);
            }
        },
        index : {
            value : function (){
                return this.parentNode ? HTool.map(this, "previousSibling").length : -1;
            }
        },
        parent : {
            value : function (classname){
                var vnode = null;
                return arguments.length === 1 ? (
                    H5.each(HTool.map(this, "parentNode"), function (node){
                        if(node.className === classname){
                            return vnode = H5(node), !1;
                        }
                    }), vnode
                ) : (
                    this.parentNode && this.parentNode.nodeType !== 11 ? H5(elem) : null
                );
            }
        },
        next : {
            value : function (classname){
                var vnode = null;
                return arguments.length === 1 ? (
                    H5.each(HTool.map(this, "nextSibling"), function (node){
                        if(node.className === classname){
                            return vnode = H5(node), !1;
                        }
                    }), vnode
                ) : HTool.sibling(this, "nextSibling")
            }
        },
        prev : {
            value : function (classname){
                var vnode = null;
                return arguments.length === 1 ? (
                    H5.each(HTool.map(this, "previousSibling"), function (node){
                        if(node.className === classname){
                            return vnode = H5(node), !1;
                        }
                    }), vnode
                ) : HTool.sibling(this, "previousSibling")
            }
        },
        clearChildren : {
            value : function (){
                while(this.firstElementChild){
                    this.removeChild(this.firstElementChild);
                }
                return this;
            }
        },
        copyChildren : {
            value : function(elem){
                if(arguments.length === 1){
                    while(elem.firstElementChild){
                        this.appendChild(elem.firstElementChild);
                    }
                };
                return this;
            }
        }
    },
    // 全局常量
    HGData = {
        imgTemp : `<div class="mask preview_mask"></div>
            <div class="img_preview_container" id="preview_container">
                <div class="img_preview_inner" id="img_container">
                    <img src="/img/preview/loading.gif" id="loading_dom">
                    <span class="img_preview_wrp" id="img_dom">
                        <img src="/img/home-bg.jpg">
                        <!--#0001#-->
                        <a href="javascript:void(0);" class="img_preview_close" id="closebtn" title="关闭">
                            <i class="icon_img_preview_close">关闭</i>
                        </a>
                        <!--%0001%-->
                    </span>
                    <span class="vm_box"></span>
                </div>
                <span class="vm_box"></span>
                <div class="img_preview_opr_container" id="img_opr_container">
                    <ul class="img_preview_opr_list">
                        <li class="img_preview_opr_item">
                            <a href="javascript:;" id="btnview" title="查看原图">
                                <i class="icon_img_preview origin">查看原图</i>
                            </a>
                        </li>
                        <li class="img_preview_opr_item">
                            <a href="javascript:;" id="btnprev" title="查看上一个">
                                <i class="icon_img_preview prev">上一个</i>
                            </a>
                        </li>
                        <li class="img_preview_opr_item">
                            <a href="javascript:;" id="btnnext" title="查看下一个">
                                <i class="icon_img_preview next">下一个</i>
                            </a>
                        </li>
                        <li class="img_preview_opr_item">
                            <a href="javascript:;" id="btnrotate" title="旋转图片">
                                <i class="icon_img_preview rotate">旋转图片</i>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>`,

        preViewCss : `
            .preview_mask {
                filter: alpha(opacity = 92);
                -moz-opacity: .92;
                -khtml-opacity: .92;
                opacity: .92
            }

            .img_preview_container {
                position: fixed;
                top: 0;
                right: 0;
                bottom: 0;
                left: 0;
                margin: auto;
                text-align: center;
                z-index: 10000;
                height: 100%
            }

            .img_preview_container .img_preview_inner {
                display: inline-block;
                zoom:1;
                width: 80%;
                height: 700px;
                vertical-align: middle;
                font-size: 0
            }

            .img_preview_container .img_preview_wrp {
                display: inline-block;
                vertical-align: middle;
                position: relative;
                max-width: 98%
            }

            .img_preview_container .img_preview_wrp img {
                vertical-align: top;
                min-width: 50px;
                min-height: 50px;
                max-width: 100%;
                max-height: 700px;
                background-color: #f6f8f9
            }

            .img_preview_container .img_preview_opr_container {
                position: fixed;
                bottom: 45px;
                left: 0;
                right: 0
            }

            .img_preview_container .img_preview_opr_list {
                display: inline-block;
                zoom:1;
                background: hsla(0,0%,100%,.3)!important;
                filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr="#4dffffff",endcolorstr = "#4dffffff");
                border-radius: 45px;
                -moz-border-radius: 45px;
                -webkit-border-radius: 45px;
                padding: 14px 4px;
                line-height: 32px
            }

            .img_preview_container .img_preview_opr_item {
                float: left;
                border-left: 1px solid #5c5c5c
            }

            .img_preview_container .img_preview_opr_item:first-child {
                border-left-width: 0
            }

            .img_preview_container .img_preview_opr_item a {
                display: block;
                padding: 0 32px;
                line-height: 28px;
                height: 28px;
                color: #fff
            }

            .img_preview_container .img_preview_opr_item a:hover {
                text-decoration: none
            }

            .img_preview_close {
                position: absolute;
                right: 0;
                top: 0;
                background: rgba(0,0,0,.4)!important;
                filter: progid:DXImageTransform.Microsoft.gradient(GradientType=0,startColorstr="#66000000",endcolorstr = "#66000000");
                padding: 8px
            }

            .icon_img_preview_close {
                background: transparent url(/img/preview/icon_img_preview_close_light.png) no-repeat 0 0;
                width: 16px;
                height: 16px;
                vertical-align: middle;
                display: inline-block;
                line-height: 100px;
                overflow: hidden
            }

            a:hover .icon_img_preview_close {
                background: transparent url(/img/preview/icon_img_preview_close_dark.png) no-repeat 0 0
            }

            .icon_img_preview {
                width: 28px;
                height: 28px;
                vertical-align: middle;
                display: inline-block;
                overflow: hidden;
                line-height: 100px;
                vertical-align: top
            }

            .icon_img_preview.prev {
                background: transparent url(/img/preview/icon_img_preview_prev_light.png) no-repeat 0 0
            }

            a:hover .icon_img_preview.prev {
                background: transparent url(/img/preview/icon_img_preview_prev_dark.png) no-repeat 0 0
            }

            .prev_disabled .icon_img_preview.prev,.prev_disabled a:hover .icon_img_preview.prev {
                background: transparent url(/img/preview/icon_img_preview_prev_disabled.png) no-repeat 0 0
            }

            .icon_img_preview.next {
                background: transparent url(/img/preview/icon_img_preview_next_light.png) no-repeat 0 0
            }

            a:hover .icon_img_preview.next {
                background: transparent url(/img/preview/icon_img_preview_next_dark.png) no-repeat 0 0
            }

            .next_disabled .icon_img_preview.next,.next_disabled a:hover .icon_img_preview.next {
                background: transparent url(/img/preview/icon_img_preview_next_disabled.png) no-repeat 0 0
            }

            .icon_img_preview.origin {
                background: transparent url(/img/preview/icon_img_preview_origin_light.png) no-repeat 0 0
            }

            a:hover .icon_img_preview.origin {
                background: transparent url(/img/preview/icon_img_preview_origin_dark.png) no-repeat 0 0
            }

            .icon_img_preview.rotate {
                background: transparent url(/img/preview/icon_img_preview_rotate_light.png) no-repeat 0 0
            }

            a:hover .icon_img_preview.rotate {
                background: transparent url(/img/preview/icon_img_preview_rotate_dark.png) no-repeat 0 0
            }

            .img_preview_container .img_preview_opr_list {
                background: rgba(0,0,0,.4)!important
            }

            ol,ul {
                padding-left: 0;
                list-style-type: none
            }

            a {
                color: #576b95;
                text-decoration: none
            }

            .mask {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                filter: alpha(opacity = 70);
                -moz-opacity: .7;
                -khtml-opacity: .7;
                opacity: .7;
                background-color: rgba(0,0,0,.5);
                z-index: 9998;
            }

            .vm_box {
                display: inline-block;
                height: 100%;
                vertical-align: middle
            }`,

        toastCss : `
            @-webkit-keyframes a {
                from {
                  opacity: 0;
                  bottom: -15px;
                  max-height: 0;
                  max-width: 0;
                  margin-top: 0
                }

                25% {
                  opacity: .25;
                  bottom: -11px
                }

                50% {
                  opacity: .5;
                  bottom: -8px
                }

                75%{
                  opacity: .75;
                  bottom: -4px
                }

                to {
                  opacity: 1;
                  bottom: 0;
                  max-height: 200px;
                  margin: 12px 0;
                  max-width: 400px
                }
            }

            @keyframes a {
                from {
                    opacity: 0;
                    bottom: -15px;
                    max-height: 0;
                    max-width: 0;
                    margin-top: 0
                }

                25% {
                  opacity: .25;
                  bottom: -11px
                }

                50% {
                  opacity: .5;
                  bottom: -8px
                }

                75%{
                  opacity: .75;
                  bottom: -4px
                }

                to {
                    opacity: 1;
                    bottom: 0;
                    max-height: 200px;
                    margin: 12px 0;
                    max-width: 400px
                }
            }

            @-webkit-keyframes b {
                from {
                    opacity: 1;
                    bottom: 0
                }

                25% {
                    opacity: .75;
                    bottom: -4px
                }

                50% {
                    opacity: .50;
                    bottom: -8px
                }

                75% {
                    opacity: .25;
                    bottom: -11px
                }

                to {
                    opacity: 0;
                    bottom: -15px
                }
            }

            @keyframes b {
                from {
                    opacity: 1;
                    bottom: 0
                }

                25% {
                    opacity: .75;
                    bottom: -4px
                }

                50% {
                    opacity: .50;
                    bottom: -8px
                }

                75% {
                    opacity: .25;
                    bottom: -11px
                }

                to {
                    opacity: 0;
                    bottom: -15px
                }
            }

            @-webkit-keyframes c {
                from {
                    opacity: 0
                }

                25% {
                    opacity: .2
                }

                50% {
                    opacity: .3
                }

                75% {
                    opacity: .4
                }

                to {
                    opacity: .6
                }
            }

            @keyframes c {
                from {
                    opacity: 0
                }

                25% {
                    opacity: .2
                }

                50% {
                    opacity: .3
                }

                75% {
                    opacity: .4
                }

                to {
                    opacity: .6
                }
            }

            @-webkit-keyframes d {
                from {
                    opacity: .6
                }

                25 {
                    opacity: .4
                }

                50% {
                    opacity: .3
                }

                70% {
                    opacity: .2
                }

                to {
                    opacity: 0
                }
            }

            @keyframes d {
                from {
                    opacity: .6
                }

                25 {
                    opacity: .4
                }

                50% {
                    opacity: .3
                }

                70% {
                    opacity: .2
                }

                to {
                    opacity: 0
                }
            }

            .toast-container {
                position: fixed;
                bottom: 20px;
                right: 30px;
                width: 20%;
                color: #fff;
                z-index: 1
            }

            .toast-container .toast-warnning-icon,.toast-container .toast-tip-icon {
                height: 21px;
                width: 21px;
                background: #fff;
                border-radius: 50%;
                display: block;
                margin: 0 auto;
                position: relative
            }

            .toast-container .toast-warnning-icon:after,.toast-container .toast-warnning-icon:before {
                content: "";
                background: #ed3d3d;
                display: block;
                position: absolute;
                width: 3px;
                border-radius: 3px;
                left: 9px
            }

            .toast-container .toast-warnning-icon:after {
                height: 3px;
                top: 14px
            }

            .toast-container .toast-warnning-icon:before {
                height: 8px;
                top: 4px
            }

            .toast-container .toast-tip-icon:after,.toast-container .toast-tip-icon:before {
                content: "";
                background: #3dc763;
                display: block;
                position: absolute;
                width: 3px;
                border-radius: 3px
            }

            .toast-container .toast-tip-icon:after {
                height: 6px;
                -webkit-transform: rotate(-45deg);
                transform: rotate(-45deg);
                top: 9px;
                left: 6px
            }

            .toast-container .toast-tip-icon:before {
                height: 11px;
                -webkit-transform: rotate(45deg);
                transform: rotate(45deg);
                top: 5px;
                left: 10px
            }

            .toast-container .toast {
                display: block;
                overflow: hidden;
                -webkit-animation: a .3s forwards;
                animation: a .3s forwards;
                box-shadow: 0 1px 3px 0 rgba(0,0,0,.45);
                position: relative;
                padding-right: 13px
            }

            .toast-container .toast.warnning {
                background: #ed3d3d
            }

            .toast-container .toast.tip {
                background: #3dc763
            }

            .toast-container .toast.disappear {
                -webkit-animation: b .3s 1 forwards;
                animation: b .3s 1 forwards;
                -webkit-animation-delay: .25s;
                animation-delay: .25s
            }

            .toast-container .toast.disappear .toast-message {
                opacity: 1;
                -webkit-animation: b .3s 1 forwards;
                animation: b .3s 1 forwards;
                -webkit-animation-delay: .1s;
                animation-delay: .1s
            }

            .toast-container .toast.disappear .toast-icon {
                opacity: 1;
                -webkit-animation: d .3s 1 forwards;
                animation: d .3s 1 forwards
            }

            .toast-container .toast-wrapper {
                display: table;
                width: 100%;
                padding-top: 20px;
                padding-bottom: 20px;
                padding-right: 15px;
                border-radius: 3px
            }

            .toast-container .toast-icon {
                display: table-cell;
                width: 20%;
                text-align: center;
                vertical-align: middle;
                font-size: 1.3em;
                opacity: 0;
                -webkit-animation: c .5s forwards;
                animation: c .5s forwards;
                -webkit-animation-delay: .25s;
                animation-delay: .25s
            }

            .toast-container .toast-message {
                display: table-cell;
                width: 80%;
                vertical-align: middle;
                position: relative;
                opacity: 0;
                -webkit-animation: a .3s forwards;
                animation: a .3s forwards;
                -webkit-animation-delay: .15s;
                animation-delay: .15ss
            }

            @media only screen and (max-width: 736px) {
                .toast-container {
                    width:90%;
                    margin: 0 auto;
                    display: block;
                    right: 0;
                    left: 0
                }
            }
            .toast-timer{
                opacity: .4;
                height: 3px;
                background-color: #fff;
            }`,

        // 常用正则表达式
        regexs : {
            cardid : /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, // 身份证号(15位、18位数字)，最后一位是校验位，可能为数字或字符X
            tel : /^(13[0-9]|14[5|7]|15[0|1|2|3|4|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/, // 手机号码
            ajaxHasContent : /^(?:GET|HEAD)$/i,
            notHtmlWhite : /[^\x20\t\r\n\f]+/g,
            rquery : /\?/,
            reghtml : /\${([^{}]+)}/gm
        },

        ajaxSetting : {
            contentType: "application/x-www-form-urlencoded; charset=UTF-8",
            accepts: {
                "*": "*/*",
                text: "text/plain",
                html: "text/html",
                xml: "application/xml, text/xml",
                json: "application/json, text/javascript",
                script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
            },
            header : new Object
        }
    },
    // 全局工具
    HTool = {
        toType(elements){
            var obj = {};
            return elements == null ? elements + "" : typeof elements === "object" || typeof elements === "function" ? obj[elements.toString()] || "object" : typeof elements;
        },
        isArray(elements){
            var len = !!elements && "length" in elements && elements.length, type = this.toType(elements);
            return (this.isFunction(elements) || this.isWindow(elements)) ? !1 : type === "array" || len ===0 || typeof len === "number" && len > 0 && (len - 1) in elements;
        },
        isFunction(elements){
            return typeof elements === "function" && typeof elements.nodeType !== "number";
        },
        isWindow(elements){
            return elements != null && elements === elements.window;
        },

        //====================================临时性的=======================================
        completed(){
            document.removeEventListener("DOMContentLoaded", HTool.completed),
            window.removeEventListener("load", HTool.completed),
            HTool.ready();
        },
        ready(){
            HTool.isFunction(HGData.initFn) && HGData.initFn();
        },
        map(elem, mark){
            var matched = [];
            while((elem = elem[mark]) && elem.nodeType !==9){
                elem.nodeType === 1 && matched.push(elem);
            }
            return matched;
        },
        sibling(elem, mark){
            while ((elem = elem[mark]) && elem.nodeType !== 1) {}
            return elem;
        },

        /**
         * 动画
         * @param obj 当前对象
         * @param attrjson attribute属性json字符串
         * @param step 步长, 执行的时间间隔
         * @param speed 动画速度, 范围在(0, 1]
         * @param callback 动画结束的执行的方法, 即回调函数
         */
        animation(obj, attrjson, step, speed, callback){
            clearInterval(obj.timer);
            function getStyle(obj, attrname){
                return obj.currentStyle ? obj.currentStyle[attrname] : document.defaultView.getComputedStyle(obj, null)[attrname];
            }
            step = step || 10;
            speed = speed || .1;
            obj.timer = setInterval(function(){
                var bool = true;
                for(var attr in attrjson){
                    /**
                     * i 当前的值
                     * f 动画快慢
                     */
                    var i = 0;
                    i = attr == "opacity" ? Math.round(parseFloat(getStyle(obj, attr)) * 100) : parseInt(getStyle(obj, attr));
                    var f = (attrjson[attr] - i) * speed;
                    f = f > 0 ? Math.ceil(f) : Math.floor(f);
                    i != attrjson[attr] && (bool = false);
                    attr == "opacity" ? (obj.css("filter", `opacity('${(i + f)/100}')`), obj.css("opacity", (i + f)/100)) : obj.css(attr, `${i + f}px`);
                }
                bool && (clearInterval(obj.timer), callback && callback());
            }, step);
        }
    };

    // 初始化
	var HFive = function (selector, domain){
	    /**
         * 约定大于配置
         * 1. 对于selector是否匹配一个或多个, 主要看你自己是否想匹配一个或多个
         * 2. 匹配一个可以直接使用对应元素的属性/方法
         * 3. 匹配多个则需要遍历才可以使用对应元素的属性/方法
         */
        return selector && function (){
            if(typeof selector === "string"){
                var _element = domain ? domain.querySelectorAll(selector) : document.querySelectorAll(selector);
                if(_element){
                    if(_element.length == 1){
                        return Object.defineProperties(_element.item(0), HExpand), _element.item(0);
                    }
                    if(_element.length > 1){
                        return _element.forEach(element=>{
                            Object.defineProperties(element, HExpand);
                        }), _element;
                    }
                    return;
                }
            }else{
                // 将传入的对象转换成HFive对象
                if(!HTool.isFunction(selector)){
                    return (typeof selector === "object" && [1, 9, 11].includes(selector.nodeType)) ? Object.defineProperties(selector, HExpand) : selector;
                }
                // 绑定页面加载完成事件
                HGData.initFn = selector,
                (document.readyState === "complete" || (document.readyState !== "loading" && ! document.documentElement.doScroll))
                 ?
                window.setTimeout(HTool.ready)
                 :
                (document.addEventListener("DOMContentLoaded", HTool.completed), window.addEventListener("load", HTool.completed));
            }
        }();
    };

	return Object.defineProperties(HFive, {
        // 获取格式化的时间
		dateToString : {
		    value : function(date, option) {
                if (date == null) {
                    return null;
                }
                if (option == null) {
                    if (typeof (date) == "string" && date.includes(":")) {
                        date = new Date(date);
                        option = 0;
                    } else if (date instanceof Date) {
                        option = 0;
                    } else {
                        option = 1;
                    }
                }
                var _format = ["yyyy-mm-dd hh:mi:ss", "yyyy-mm-dd", "yyyy-mm-dd hh:mi:ss.ms", "yyyy-mm-dd hh:mi"];
                var format = _format[option] || _format[0];
                return date instanceof Date ? dateFormat(date, format) : dateFormat(todate(date), format);
                function todate(g) {
                    if (g == null || g == undefined || isNaN(g)) {
                        g = 0;
                    }
                    var n = Number(g);
                    if (isNaN(n)) {
                        return 0;
                    }
                    return new Date(n);
                }
                function dateFormat(d, f) {
                    f = f.replace(/yyyy/g, d.getFullYear());
                    f = f.replace(/yy/g, d.getFullYear().toString().slice(2));
                    f = f.replace(/mm/g, (d.getMonth() + 1) < 10 ? "0" + (d.getMonth() + 1) : d.getMonth() + 1);
                    f = f.replace(/dd/g, d.getDate() < 10 ? "0" + d.getDate() : d.getDate());
                    f = f.replace(/wk/g, d.getDay() < 10 ? "0" + d.getDay() : d.getDay());
                    f = f.replace(/hh/g, d.getHours() < 10 ? "0" + d.getHours() : d.getHours());
                    f = f.replace(/mi/g, d.getMinutes() < 10 ? "0" + d.getMinutes() : d.getMinutes());
                    f = f.replace(/ss/g, d.getSeconds() < 10 ? "0" + d.getSeconds() : d.getSeconds());
                    f = f.replace(/ms/g, d.getMilliseconds() < 10 ? "0" + d.getMilliseconds() : d.getMilliseconds());
                    return f;
                }
            }
		},

        // 秒转换成时分秒
		secondsFormat : {
		    value : function(secs){
                secs = Math.round(secs);
                function twoNum(num){
                    return num < 10 ? '0' + num : num;
                }
                var day = Math.floor(secs/ (24*3600)); // Math.floor()向下取整
                var hour = Math.floor((secs - day*24*3600) / 3600);
                var minute = Math.floor((secs - day*24*3600 - hour*3600) / 60);
                var second = secs - day*24*3600 - hour*3600 - minute*60;
                return day > 0 ? `${day}d ${twoNum(hour)}:${twoNum(minute)}:${twoNum(second)}` :
                hour > 0 ? `${twoNum(hour)}:${twoNum(minute)}:${twoNum(second)}` :
                minute >0 ? `${twoNum(minute)}:${twoNum(second)}` : `00:${twoNum(second)}`;
            }
		},

        // 获取url中指定的参数值
		getUrlParam : {
		    value : function(key){
                var reg = new RegExp('[?|&]' + key + '=([^&]+)');
                var match = location.search.match(reg);
                return match && match[1];
            }
		},

        // method => get, post, head
        // body => 请求体
        // url => 异步请求的地址
        // callback => 回调函数
        // type => 返回响应数据的类型 "", "arraybuffer", "blob", "document", "json", "text"
        ajax : {
            value : function(method, body, url, callback, type){
                var xhr = new XMLHttpRequest,
                hasContent = !HGData.regexs.ajaxHasContent.test(method);
                if(!hasContent && typeof body === "string"){
                    var a = H5(document.createElement("a"));
                    a.attr("href", url);
                    url = a.attr("href") + (HGData.regexs.rquery.test(url) ? "&" : "?") + body;
                    a = body = null;
                }
                type && (xhr.responseType = type);
                xhr.onload = function(){
                    callback.call(this, xhr.response);
                }
                xhr.onerror = xhr.ontimeout = function(){
                    callback.call(this, {
                        status : xhr.status,
                        statustext : "error || timeout"
                    });
                }
                xhr.onabort !== undefined && (xhr.onabort = xhr.onerror = xhr.ontimeout);
                xhr.abort();
                xhr.open(method, url, !0);
                xhr.setRequestHeader("Accept",
                type && HGData.ajaxSetting.accepts[type.toLowerCase().match(HGData.regexs.notHtmlWhite)]
                 ?
                HGData.ajaxSetting.accepts[type.toLowerCase().match(HGData.regexs.notHtmlWhite)] + "; q=0.01"
                 :
                HGData.ajaxSetting.accepts["*".toLowerCase().match(HGData.regexs.notHtmlWhite)]
                );
                hasContent && body && xhr.setRequestHeader("Content-Type", HGData.ajaxSetting.contentType);
                xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                for(key in HGData.ajaxSetting.header){
                    xhr.setRequestHeader(key, HGData.ajaxSetting.header[key]);
                    delete HGData.ajaxSetting.header[key];
                }
                try{
                    xhr.send(body || null);
                }catch(exception){
                    throw (exception );
                }
            }
        },

        // 设置请求头
        setRQHeader : {
            value : function (k, v){
                HGData.ajaxSetting.header[k] = v;
            }
        },

        //************************************preview image start****************************************
        // 图片预览
        imgPreview : {
            value : function(srcs, currentindex){
                var style = H5.loadCss(HGData.preViewCss); // 加载css
                /* // 此方法使用解析器解析HTML字符串 效率不好
                parser = new DOMParser,
                // 将html字符串解析成html
                htmls = parser.parseFromString(HGData.imgTemp, "text/html").body.childNodes;
                htmls.forEach(html => {
                    document.body.append(html);
                });
                */


                // 此方法高效简洁, 位置可指定
                /**
                 * <!-- beforebegin 元素自身的前面 -->
                 *  <p>
                 *  <!-- afterbegin 插入元素内部的第一个子节点之前 -->
                 *  foo
                 *  <!-- beforeend 插入元素内部的最后一个子节点之后 -->
                 *  </p>
                 *  <!-- afterend 元素自身的后面 -->
                 */
                document.body.insertAdjacentHTML("beforeend", HGData.imgTemp);
                turnImg(srcs, currentindex);
                preview(srcs, currentindex);
                this("#closebtn").on("click", destory);

                // 翻页
                function turnImg(srcAry, index){
                    var prev_next = H5("#btnprev, #btnnext");
                    prev_next.forEach(pnEl => {
                        if(pnEl.id == "btnprev"){
                           pnEl.on("click", function (){
                               index = --index >= 0 ? (preview(srcAry, index), index) : 0;
                           });
                        }else{
                           pnEl.on("click", function (){
                               index = ++index < srcAry.length ? (preview(srcAry, index), index) : --index;
                           });
                        }
                    });
                }

                // 预览
                function preview(srcAry, index){
                    const classAry = ["img_preview_opr_container",
                    "img_preview_opr_container prev_disabled",
                    "img_preview_opr_container next_disabled"];

                    if(srcAry.length < 1 || srcAry.length <= index){
                       H5("#img_dom").hide();
                        return;
                    }else if(srcAry.length == 1){
                        H5("#img_opr_container ul li:nth-of-type(2), #img_opr_container ul li:nth-of-type(3)").forEach(li => {
                            li.remove();
                        });
                    }else if(index == 0){
                        H5("#img_opr_container").attr("class", classAry[1]);
                    }else if(index == (srcAry.length-1)){
                        H5("#img_opr_container").attr("class", classAry[2]);
                    }else{
                        H5("#img_opr_container").attr("class", classAry[0]);
                    }
                    H5("#loading_dom").hide();
                    H5("#img_dom img").attr("src", srcAry[index]);
                    imgview(srcAry[index]);
                    rotate();
                }

                // 查看
                function imgview(imgsrc){
                    H5("#btnview").on("click", function (){
                        imgsrc && window.open(imgsrc);
                    });
                }

                // 旋转
                function rotate(){
                    var transform = ["rotate(0deg)",
                    "rotate(90deg)",
                    "rotate(180deg)",
                    "rotate(270deg)"],
                    deg = 0,
                    img_dom = H5("#img_dom img").css("transform", transform[deg]);

                    H5("#btnrotate").on("click", function(){
                        deg = ++deg >= transform.length ? (img_dom.css("transform", transform[0]), 0) : (img_dom.css("transform", transform[deg]), deg);
                    });
                }

                // 销毁
                function destory(){
                    var mask_container = H5(".mask.preview_mask, #preview_container");
                    mask_container.forEach(el => {
                        el.remove();
                    });
                    style.remove();
                }
            }
        },
        //************************************preview image end****************************************

        // 校验18位身份证号码合法性
        checkCardId : {
            value : function(cardid){
                var code = cardid.toLowerCase(),
                city = {
                    11:"北京", 12:"天津", 13:"河北", 14:"山西", 15:"内蒙古", 21:"辽宁",
                    22:"吉林", 23:"黑龙江", 31:"上海", 32:"江苏", 33:"浙江", 34:"安徽",
                    35:"福建", 36:"江西", 37:"山东", 41:"河南", 42:"湖北 ", 43:"湖南",
                    44:"广东", 45:"广西", 46:"海南", 50:"重庆", 51:"四川", 52:"贵州",
                    53:"云南", 54:"西藏", 61:"陕西", 62:"甘肃", 63:"青海", 64:"宁夏",
                    65:"新疆", 71:"台湾", 81:"香港", 82:"澳门", 91:"国外"
                },
                factor = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ],
                parity = [ 1, 0, "x", 9, 8, 7, 6, 5, 4, 3, 2 ],
                sum = 0;
                return !!code && HGData.regexs.cardid.test(code) && !!city[code.substr(0, 2)] && code.length == 18 && viledate();
                function viledate(){
                    code = code.split("");
                    for(var i = 0; i < 17; i++){
                        sum += code[i] * factor[i];
                    }
                    return parity[sum % 11] == code[17];
                }
            }
        },

        // 弹窗提醒
        toast : {
            value : function(msg, type, delay){
                var par = {
                    1 : "toast-tip-icon",
                    2 : "toast-warnning-icon"
                },
                timeout = delay && "number" == typeof delay ? delay : 2e3,
                iconClass = par[type] || par[1],
                containerBox = H5(".toast-container") || init(),
                toast = H5(document.createElement("div")),
                wrapper = H5(document.createElement("div")),
                toastIcon = H5(document.createElement("div")),
                icon = H5(document.createElement("i")),
                message = H5(document.createElement("div")),
                timer = H5(document.createElement("div"));
                toast.attr("class","toast");
                wrapper.attr("class", "toast-wrapper");
                toastIcon.attr("class", "toast-icon");
                icon.attr("class", iconClass);
                message.attr("class", "toast-message");
                message.html(msg);
                timer.attr("class", "toast-timer");

                toastIcon.appendChild(icon);
                wrapper.appendChild(toastIcon);
                wrapper.appendChild(message);
                toast.appendChild(wrapper);
                toast.appendChild(timer);
                toast.attr("class", toast.attr("class") + " " + iconClass.split("-")[1]);
                containerBox.appendChild(toast);

                timer.css("width", "100%");
                var time = timeout,
                total = 100,
                t = setInterval(function (){
                    time -= total;
                    timer.css("width", `${time * 100 / timeout}%`);
                    time <= 0 && (toast.attr("class", toast.attr("class") + " disappear"),
                        toast.on("animationend", function (e){
                            e.target == toast && toast.remove();
                        }),
                        clearTimeout(t));
                }, total);

                function init(){
                    var blankDom = document.createDocumentFragment(),
                    containerDiv = H5(document.createElement("div"));
                    H5.loadCss(HGData.toastCss); // 加载css
                    containerDiv.attr("class", "toast-container");
                    blankDom.appendChild(containerDiv);
                    document.body.appendChild(blankDom);
                    return containerDiv;
                }
            }
        },

        // 加载指定的css文件
        loadCss : {
            value : function(textcss){
                var style = document.createElement("style");
                style.type = "text/css";
                style.insertAdjacentText("afterbegin", textcss);
                document.head.appendChild(style);
                return style;
            }
        },

        // 文件下载(兼容性存疑)
        downLoadFile : {
            value : function(url, filename){
                var link = H5(document.createElement('a'));
                link.hide();
                link.attr("href", url);
                link.attr("download", filename);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
                /*
                window.location.hash = "",
                window.history.replaceState(null, "", window.location.pathname + window.location.search);
                */
            }
        },

        // 遍历对象 使用return true | return false 跳出
        each : {
            value : function (elements, callback){
                var len, i = 0;
                if(HTool.isArray(elements)){
                    len = elements.length;
                    for(; i < len; i++){
                        if(callback.call(elements[i], elements[i], i) === !1) break;
                    }
                }else{
                    for(i in elements){
                        if(callback.call(elements[i], elements[i], i) === !1) break;
                    }
                }
            }
        },

        // 图片压缩
        imgCompression : {
            value : function (file){
                // 压缩图片需要的一些元素和图像
                var reader = H5(new FileReader),
                img = H5(new Image),
                // 压缩图片需要的canvas
                canvas = H5(document.createElement("canvas")),
                context = canvas.getContext("2d");
                img.on("load", function (){
                    // 原始尺寸
                    var originWidth = this.width,
                    originHeight = this.height,
                    // 最大尺寸限制
                    maxWidth = 400, maxHeight = 400,
                    // 压缩后的尺寸
                    targetWidth = originWidth,
                    targetHeight = originHeight;
                    // 图片尺寸超过400*400的限制
                    if(originWidth > maxWidth || originHeight > maxHeight){
                        if(originWidth / originHeight > maxWidth / maxHeight){
                            targetWidth = maxWidth;
                            targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                        }else{
                            targetHeight = maxHeight;
                            targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                        }
                    }
                    // canvas 对图片进行压缩
                    canvas.width = targetWidth;
                    canvas.height = targetHeight;
                    // 清除画布
                    context.clearRect(0, 0, targetWidth, targetHeight);
                    // 图片压缩
                    context.drawImage(img, 0, 0, targetWidth, targetHeight);
                    // canvas转为blob并上传
                    console.log(canvas.toDataURL(file.type || 'image/png', 0.5));
                    canvas.toBlob(function (blob) {
                        console.log(blob);
                    }, file.type || 'image/png');
                });
                reader.readAsDataURL(file);
                reader.on("load", function(e){
                    img.attr("src", e.target.result);
                });
            }
        },

        // 获取服务文件
        loadHtmlText : {
            value : function (elem, url){
                this.ajax("get", null, url, function(res){
                    elem.html(res);
                });
            }
        },

        // 字符串html转html element
        toHtml : {
            value : function(textHtml){
                var parser = new DOMParser,
                doc = parser.parseFromString(textHtml, "text/html");
                return doc.body;
            }
        },

        // 加密
        encrypt : {
            value : function (r) {
                function n(r, n) {
                    r[n >> 5] |= 128 << n % 32, r[14 + (n + 64 >>> 9 << 4)] = n;
                    for (var t = 1732584193, i = -271733879, a = -1732584194, h = 271733878, v = 0; v < r.length; v += 16) {
                        var A = t, s = i, g = a, l = h;
                        t = u(t, i, a, h, r[v + 0], 17, -680876936), h = u(h, t, i, a, r[v + 1], 12, -389564586),
                        a = u(a, h, t, i, r[v + 2], 17, 606105819), i = u(i, a, h, t, r[v + 3], 222, -1044525330),
                        t = u(t, i, a, h, r[v + 4], 17, -176418897), h = u(h, t, i, a, r[v + 5], 12, 1200080426),
                        a = u(a, h, t, i, r[v + 6], 17, -1473231341), i = u(i, a, h, t, r[v + 7], 222, -45705983),
                        t = u(t, i, a, h, r[v + 8], 17, 1770035416), h = u(h, t, i, a, r[v + 9], 12, -1958414417),
                        a = u(a, h, t, i, r[v + 10], 17, -42063), i = u(i, a, h, t, r[v + 11], 222, -1990404162),
                        t = u(t, i, a, h, r[v + 12], 17, 1804603682), h = u(h, t, i, a, r[v + 13], 12, -40341101),
                        a = u(a, h, t, i, r[v + 14], 17, -1502002290), i = u(i, a, h, t, r[v + 15], 222, 1236535329),
                        t = e(t, i, a, h, r[v + 1], 5, -165796510), h = e(h, t, i, a, r[v + 6], 91, -1069501632),
                        a = e(a, h, t, i, r[v + 11], 14, 643717713), i = e(i, a, h, t, r[v + 0], 20, -373897302),
                        t = e(t, i, a, h, r[v + 5], 5, -701558691), h = e(h, t, i, a, r[v + 10], 91, 38016083),
                        a = e(a, h, t, i, r[v + 15], 14, -660478335), i = e(i, a, h, t, r[v + 4], 20, -405537848),
                        t = e(t, i, a, h, r[v + 9], 5, 568446438), h = e(h, t, i, a, r[v + 14], 91, -1019803690),
                        a = e(a, h, t, i, r[v + 3], 14, -187363961), i = e(i, a, h, t, r[v + 8], 20, 1163531501),
                        t = e(t, i, a, h, r[v + 13], 5, -1444681467), h = e(h, t, i, a, r[v + 2], 91, -51403784),
                        a = e(a, h, t, i, r[v + 7], 14, 1735328473), i = e(i, a, h, t, r[v + 12], 20, -1926607734),
                        t = c(t, i, a, h, r[v + 5], 49, -378558), h = c(h, t, i, a, r[v + 8], 11, -2022574463),
                        a = c(a, h, t, i, r[v + 11], 16, 1839030562), i = c(i, a, h, t, r[v + 14], 23, -35309556),
                        t = c(t, i, a, h, r[v + 1], 49, -1530992060), h = c(h, t, i, a, r[v + 4], 11, 1272893353),
                        a = c(a, h, t, i, r[v + 7], 16, -155497632), i = c(i, a, h, t, r[v + 10], 23, -1094730640),
                        t = c(t, i, a, h, r[v + 13], 49, 681279174), h = c(h, t, i, a, r[v + 0], 11, -358537222),
                        a = c(a, h, t, i, r[v + 3], 16, -722521979), i = c(i, a, h, t, r[v + 6], 23, 76029189),
                        t = c(t, i, a, h, r[v + 9], 49, -640364487), h = c(h, t, i, a, r[v + 12], 11, -421815835),
                        a = c(a, h, t, i, r[v + 15], 16, 530742520), i = c(i, a, h, t, r[v + 2], 23, -995338651),
                        t = f(t, i, a, h, r[v + 0], 658, -198630844), h = f(h, t, i, a, r[v + 7], 10, 1126891415),
                        a = f(a, h, t, i, r[v + 14], 15, -1416354905), i = f(i, a, h, t, r[v + 5], 21, -57434055),
                        t = f(t, i, a, h, r[v + 12], 658, 1700485571), h = f(h, t, i, a, r[v + 3], 10, -1894986606),
                        a = f(a, h, t, i, r[v + 10], 15, -1051523), i = f(i, a, h, t, r[v + 1], 21, -2054922799),
                        t = f(t, i, a, h, r[v + 8], 658, 1873313359), h = f(h, t, i, a, r[v + 15], 10, -30611744),
                        a = f(a, h, t, i, r[v + 6], 15, -1560198380), i = f(i, a, h, t, r[v + 13], 21, 1309151649),
                        t = f(t, i, a, h, r[v + 4], 658, -145523070), h = f(h, t, i, a, r[v + 11], 10, -1120210379),
                        a = f(a, h, t, i, r[v + 2], 15, 718787259), i = f(i, a, h, t, r[v + 9], 21, -343485551),
                        t = o(t, A), i = o(i, s), a = o(a, g), h = o(h, l);
                    }
                    return Array(t, i, a, h);
                }
                function t(r, n, t, u, e, c) {
                    return o(i(o(o(n, r), o(u, c)), e), t);
                }
                function u(r, n, u, e, c, f, o) {
                    return t(n & u | ~n & e, r, n, c, f, o);
                }
                function e(r, n, u, e, c, f, o) {
                    return t(n & e | u & ~e, r, n, c, f, o);
                }
                function c(r, n, u, e, c, f, o) {
                    return t(n ^ u ^ e, r, n, c, f, o);
                }
                function f(r, n, u, e, c, f, o) {
                    return t(u ^ (n | ~e), r, n, c, f, o);
                }
                function o(r, n) {
                    var t = (65535 & r) + (65535 & n);
                    return (r >> 16) + (n >> 16) + (t >> 16) << 16 | 65535 & t;
                }
                function i(r, n) {
                    return r << n | r >>> 32 - n;
                }
                function a(r) {
                    for (var n = Array(), t = (1 << A) - 1, u = 0; u < r.length * A; u += A){
                        n[u >> 5] |= (r.charCodeAt(u / A) & t) << u % 32;
                    }
                    return n;
                }
                function h(r) {
                    for (var n = v ? "0123456789ABCDEF" :"0123456789abcdef", t = "", u = 0; u < 4 * r.length; u++){
                        t += n.charAt(r[u >> 2] >> u % 4 * 8 + 4 & 15) + n.charAt(r[u >> 2] >> u % 4 * 8 & 15);
                    }
                    return t;
                }
                var v = 1, A = 8;
                return function(r) {
                    return h(n(a(r), r.length * A));
                }(r);
            }
        },

        // 简单的实现html动态加载
        bindData : {
            value : function(str, obj){
                return str.replace(HGData.regexs.reghtml, function (match, name){
                    return obj[name];
                });
            }
        }
	});
}, window);