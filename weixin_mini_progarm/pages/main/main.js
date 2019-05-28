var base64 = require("../../lib/base64.js");
var URI = require("../../lib/URI.js");

Page({

    /**
     * 初始化数据
     */
    data: {
        url: "",
        currentUser: null
    },

    /**
     * 加载
     */
    onLoad: function(query) {
        this.setData({
            url: URI(query.url ? decodeURIComponent(query.url) : this.data.url).setSearch("_postCurrentUserToWeixinMiniProgarm", "true").toString()
        });
    },

    /**
     * 转发
     */
    onShareAppMessage: function(res) {
        var currentUser = this.data.currentUser;
        var webViewUrl = res.webViewUrl;
        var shareUrl = currentUser && currentUser.type === "member" ? URI(webViewUrl).fragment("SPREAD_" + base64.Base64.encode(currentUser.username)).toString() : webViewUrl;

        return {
            path: "/pages/main/main?url=" + encodeURIComponent(shareUrl)
        }
    },

    /**
     * 绑定消息
     */
    bindMessage: function(e) {
        var detail = e.detail;

        if (!detail || !detail.data) {
            return;
        }

        for (var i = detail.data.length - 1; i >= 0; i--) {
            var item = detail.data[i];

            if (item && item.hasOwnProperty("currentUser")) {
                this.setData({
                    currentUser: item.currentUser
                });
                break;
            }
        }
    }

});