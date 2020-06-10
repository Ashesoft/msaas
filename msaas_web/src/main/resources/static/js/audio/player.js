// audio.js

// player
(function (){
    const attrMap = {
    1 : "rgb(225, 52, 48)", 2 : "play-pause icon playing", 3 : "play-pause icon paused", 4 : "locker-btn locked",
    5 : `<div class="history-item" data-id="0">
             <div class="playing-flag common-playing-flag"></div>
             <div class="program-name-wrapper">
                 <span title="" class="program-name"></span>
             </div>
             <div class="channel-name-wrapper">
                 <span title="" class="channel-name"></span>
             </div>
             <div title="刚刚" class="timestr">刚刚</div>
             <div class="item-progress">已收听2%</div>
             <div class="action-buttons actionButtons">
                 <span class="share-btn"></span>
                 <span class="download-btn"></span>
                 <span class="remove-btn"></span>
             </div>
         </div>`,
    6 : `<div class="playlist-item" data-id="0">
             <div class="playing-flag common-playing-flag"></div>
             <div class="program-name-wrapper">
                 <span title="" class="program-name"></span>
                 <span class="playing-text">直播中</span>
             </div>
             <div class="channel-name-wrapper">
                 <span class="channel-name"></span>
             </div>
             <div class="duration">00:00:00</div>
             <div class="action-buttons actionButtons">
                 <span class="share-btn"></span>
                 <span class="download-btn"></span>
             </div>
         </div>`,
    7 : "audio-player hiding", 8 : "audio-player",
    "locker-btn locked" : "locker-btn", "locker-btn" : "locker-btn locked",
    "play-pause icon paused" : "play-pause icon playing", "play-pause icon playing" : "play-pause icon paused",
    "history-wrapper" : "history-wrapper active", "history-wrapper active" : "history-wrapper",
    "playlist-wrapper" : "playlist-wrapper active", "playlist-wrapper active" : "playlist-wrapper",
    "history-item active" : "history-item", "history-item" : "history-item active",
    "playlist-item active" : "playlist-item", "playlist-item" : "playlist-item active",
    }

    var player = H5(".audio-player"),
    playbtn = H5(".audio-player .play-pause.icon"),
    locker = H5(".audio-player .locker .locker-btn"),
    prev = H5(".audio-player .prev.icon"),
    next = H5(".audio-player .next.icon"),
    img = H5(".audio-player .cover"),
    programtitle = H5(".audio-player .program-title"),
    podcaster = H5(".audio-player .podcaster"),
    channeltitle = H5(".audio-player .channel-title"),
    timeelapsed = H5(".audio-player .progress .time-elapsed"),
    timetotal = H5(".audio-player .progress .time-total"),
    progressbar = H5(".audio-player .progress .progress-bar"),
    progresscache = H5(".audio-player .progress .progress-bar-cache"),
    progresselapsed = H5(".audio-player .progress .progress-bar-elapsed"),
    volumebar = H5(".audio-player .volume .volume-bar"),
    volumecurrent = H5(".audio-player .volume .volume-bar-current"),
    playratetitle = H5(".audio-player .play-rate-view .btn.play-rate-title"),
    playrateselect = H5(".audio-player .play-rate-view .rate-select"),
    sharebtn = H5(".audio-player .btns.action-buttons .share-btn"),
    downloadbtn = H5(".audio-player .btns.action-buttons .download-btn"),
    historyicon = H5(".audio-player .player-wrapper .history-icon.icon"),
    playlisticon = H5(".audio-player .player-wrapper .playlist-icon.icon"),
    historywrapper = H5(".audio-player .history-wrapper"),
    historycontent = H5(".history .content", historywrapper),
    historyclearall = H5(".history .clear-all", historywrapper),
    playlistwrapper = H5(".audio-player .playlist-wrapper"),
    playlist = H5(".playlist-collection .content .playlist", playlistwrapper),
    shrinkbtns = H5(".audio-player .shrink-btn"),
    audios = new Array, historymap = new Map, indx = 0, playbackRate = 1, vol = 0.2,
    audio = H5(new Audio);

    playing(indx, !0);
    playbtn.on("click", function (){
        playbtn.attr("class", attrMap[playbtn.attr("class")]);
        playbtn.attr("class") == attrMap[3] ? audio.pause() : audio.play();
    });

    prev.on("click", function(){
        return --indx < 0 ? (indx++, H5.toast("已经是第一首了!", 2), void 0) : initAudio(indx),
        playbtn.attr("class", attrMap[2]),
        audio.play();
    });

    next.on("click", function(){
        return ++indx >= audios.length ? (indx--, H5.toast("已经是最后一首了!", 2), void 0) : initAudio(indx),
        playbtn.attr("class", attrMap[2]),
        audio.play();
    });

    progressbar.on("click", function (e){
        var percent = e.offsetX / progressbar.offsetWidth,
        time = percent * audio.duration;
        audio.currentTime = time;
        timeelapsed.txt(H5.secondsFormat(time));
        progresselapsed.css("width", `${percent * 100}%`);
    });

    volumebar.on("click", function(e){
        var percent = e.offsetX / volumebar.offsetWidth;
        audio.volume = vol = Number(percent.toFixed(1));
        volumecurrent.css("width", `${percent * 100}%`);
    });

    playratetitle.on("click", function (){
        playrateselect.flag ? (playrateselect.hide(), playrateselect.flag = !1) : (playrateselect.show(), playrateselect.flag = !0);
    });

    playrateselect.on("click", function(e){
        var element = H5(e.srcElement || e.target);
        element.attr("class") == "rate-option" && (playratetitle.html("x " + element.html()),
        audio.playbackRate = element.html(),
        H5("[style]", element.parentElement).rmvAttr("style"),
        element.css("color", `${attrMap[1]}`),
        playrateselect.hide(),
        playrateselect.flag = !1);
    });

    downloadbtn.on("click", function(){
        H5.downLoadFile(audio.attr("src"), audio.attr("src").substr(audio.attr("src").lastIndexOf("/") + 1));
    });

    historyicon.on("click", function (){
        playrateselect.flag == !0 && (playrateselect.hide(), playrateselect.flag = !1),
        playlistwrapper.attr("class").includes("active") && playlistwrapper.attr("class", attrMap[playlistwrapper.attr("class")]),
        historywrapper.attr("class", attrMap[historywrapper.attr("class")]);
        if(historywrapper.attr("class").includes("active")){
            while(historycontent.firstElementChild){
                historycontent.firstElementChild.remove();
            }
            historymap.forEach((v, k) => {
                var history = H5(document.createElement("div"));
                history.html(attrMap[5]);
                var historyitem = H5(".history-item", history),
                pname = H5(".program-name", history),
                cname = H5(".channel-name", history),
                iprogress = H5(".item-progress", history);
                historyitem.attr("data-id", k);
                pname.attr("title", v["programtitle"]);
                pname.html(v["programtitle"]);
                cname.attr("title", v["channeltitle"]);
                cname.html(v["channeltitle"]);
                iprogress.txt(`已收听${v["progress"]}%`);
                history.firstElementChild.on("click", function(){
                    var old = H5(".history-item.active", historycontent);
                    old && old.attr("class", attrMap[old.attr("class")]);
                    indx = k,
                    initAudio(indx),
                    playbtn.attr("class", attrMap[2]),
                    H5(this).attr("class", attrMap["history-item"]),
                    audio.play();
                });
                historycontent.appendChild(history.firstElementChild);
            });
            var currentEl = H5(`[data-id="${indx}"]`, historycontent);
            currentEl && currentEl.attr("class", attrMap["history-item"]);
        }
    });

    historyclearall.on("click", function (){
        while(historycontent.firstElementChild){
            historycontent.firstElementChild.remove();
        }
        historymap.clear();
    });

    playlisticon.on("click", function (){
        playrateselect.flag == !0 && (playrateselect.hide(), playrateselect.flag = !1),
        historywrapper.attr("class").includes("active") && historywrapper.attr("class", attrMap[historywrapper.attr("class")]),
        playlistwrapper.attr("class", attrMap[playlistwrapper.attr("class")]);

        if(playlistwrapper.attr("class").includes("active")){
            var old = H5(".playlist-item.active", playlist),
            currentEl = H5(`[data-id="${indx}"]`, playlist);

            old && old.attr("class", attrMap[old.attr("class")]);
            currentEl && currentEl.attr("class", attrMap["playlist-item"]);
        }
    });

    shrinkbtns.forEach(closebtn => {
        closebtn.on("click", function(e){
            var wrapper = e.target || e.currentTarget;
            findParent(wrapper);
        });
        function findParent(par){
            par.className.includes("active") ? H5(par).attr("class", par.classList[0]) : findParent(par.parentElement) ;
        }
    });

    function playing(index, flag){
        flag && H5.ajax("get", null, "/getAudios", function (res){
            res.data ? (audios = res.data,
            initAudio(index),
            initPlayList() // 初始化播放列表
            ) : H5.toast("没有获取到播放列表", 2, 5000);
        }, "json");
        function initPlayList(){
            audios.forEach((aud, i) => {
                var playitem = H5(document.createElement("div"));
                playitem.html(attrMap[6]);
                var playlistitem = H5(".playlist-item", playitem),
                pname = H5(".program-name", playitem),
                cname = H5(".channel-name", playitem),
                pduration = H5(".duration", playitem);
                playlistitem.attr("data-id", i);
                pname.attr("title", aud["programtitle"]);
                pname.html(aud["programtitle"]);
                cname.attr("title", aud["channeltitle"]);
                cname.html(aud["channeltitle"]);
                pduration.txt(H5.secondsFormat(aud["timetotal"]));
                playitem.firstElementChild.on("click", function(){
                    var old = H5(".playlist-item.active", playlist);
                    old && old.attr("class", attrMap[old.attr("class")]);
                    indx = i,
                    initAudio(indx),
                    playbtn.attr("class", attrMap[2]),
                    H5(this).attr("class", attrMap["playlist-item"]),
                    audio.play();
                });
                playlist.appendChild(playitem.firstElementChild);
            });
        }
    }

    function initAudio(index){
        img.attr("src", audios[index]["imgsrc"]),
        programtitle.html(audios[index]["programtitle"]),
        podcaster.html(audios[index]["podcaster"]),
        channeltitle.html(audios[index]["channeltitle"]),
        timetotal.html(H5.secondsFormat(audios[index]["timetotal"]));
        audio.attr("src", audios[index]["audiosrc"]);
        audio.currentTime = audios[index]["progress"] / 100 * audios[index]["timetotal"]; // 记忆播放
        audio.volume = vol;
        audio.playbackRate = playbackRate;
        volumecurrent.css("width", `${audio.volume * 100}%`);
    }
    audio.on("loadstart", function (){
        progresscache.css("width", "0%");
    });
    audio.on("timeupdate", function(){ // audio.duration 总时长
        timeelapsed.html(H5.secondsFormat(audio.currentTime));
        var progress = audio.currentTime / audio.duration * 100;
        progresselapsed.css("width", `${progress}%`);
        if(progress > 0){
            historymap.set(indx, (audios[indx].progress = (Math.round(progress) >= 99.9 ? 0 : Math.round(progress)), audios[indx]));
        }
    });
    audio.on("ended", function (){
        playbtn.attr("class", attrMap[2]);
        timeelapsed.txt("00:00");
        progresselapsed.css("width", "0%");
        return ++indx >= audios.length ? (indx--, void 0) : initAudio(indx), audio.play();
    });
    audio.on("progress", function (){
        for (var i = 0, len = audio.buffered.length; i < len; i++) {
            var sindex = audio.buffered.start(i) , eindex = audio.buffered.end(i);
            if (sindex <= audio.currentTime && audio.currentTime < eindex){
                progresscache.css("width", `${eindex * 100 / audio.duration}%`);
            }
        }
    });

    locker.on("click", function (){
        locker.attr("class", attrMap[locker.attr("class")]);
        locker.attr("class") == attrMap[4] ? (player.on("mouseenter", null), player.on("mouseleave", null)) : locker.on("mouseout", lockerInit);
    });

    function lockerInit(){
        locker.on("mouseout", null),
        player.onmouseleave = function (){
            player.attr("class", attrMap[7]);
        },
        setTimeout(mouseEvent, 100);
    }

    function mouseEvent (){
        player.on("mouseenter", function(){
            player.attr("class", attrMap[8]);
        });
    }
})();
