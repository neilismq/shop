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

        wx.requestPayment({
            "timeStamp": query.timeStamp,
            "nonceStr": query.nonceStr,
            "package": "prepay_id=" + query.prepayId,
            "signType": query.signType,
            "paySign": query.paySign,
            "success": function(res) {
                that.setData({
                    url: decodeURIComponent(query.postPayUrl)
                });
            },
            "fail": function (res) {
                that.setData({
                    url: decodeURIComponent(query.return_url)
                });
            }
        });
    }

});