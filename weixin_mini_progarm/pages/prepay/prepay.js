var URI = require("../../lib/URI.js");
Page({

    /**
     * 初始化数据
     */
    data: {
        url: ""
    },

    /**
     * 加载
     */
    onLoad: function(query) {
        var that = this;

        wx.login({
            success: function(res) {
                if (res.code) {
                    that.setData({
                        url: URI(decodeURIComponent(query.payUrl)).setSearch("code", res.code).toString()
                    });
                }
            }
        });
    }

});