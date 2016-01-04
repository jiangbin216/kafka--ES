/**
 * Created by liqingjie on 15/11/3.
 */
require.config({
    paths: {
        'echarts': './skins/js/libs/echarts',
        'theme': './skins/js/libs/echarts/theme'
    }
});
!function($) {
    /* 日期格式化 */
    Date.prototype.format = function(fmt) {
        var o = {
            'M+': this.getMonth() + 1,
            'd+': this.getDate(),
            'h+': this.getHours(),
            'm+': this.getMinutes(),
            's+': this.getSeconds(),
            'q+': Math.floor((this.getMonth() + 3) / 3),
            'S': this.getMilliseconds()
        };
        if(/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        return fmt;
    };
    var minuteChartOption = {
        title: {
            text: '每秒数据',
            x: 50,
            textStyle: {
                fontSize: 16,
                fontFamily: 'Microsoft YaHei'
            },
            subtextStyle: {
                fontSize: 14
            }
        },
        grid: {
            x: 50,
            x2: 50
        },
        xAxis: [
            {
                type: 'category',
                name: 'mm:ss',
                nameLocation: 'start'
            }
        ],
        yAxis: [
            {
                type: 'value',
                position: 'right',
                name: 'quantity'
            }
        ],
        series: [
            {
                type: 'bar',
                itemStyle: {
                    normal: {
                        label: {
                            show: true
                        }
                    }
                }
            }
        ]
    };

    require(['echarts', 'theme/macarons', 'echarts/chart/bar'], function(ec, theme) {
        var minuteChart = ec.init(document.getElementById('minuteChart'), theme);

        $.monitorInit = {
            countData: [],
            xAxisData: [],
            dataLength: 30,
            start: start,
            intervalTime: 1000,
            action: function() {
                var self = this;
                self.addData();
                setInterval(function() {
                    self.addData();
                }, self.intervalTime);
            },
            addData: function() {
                var self = this;
                self.start ? self.doAjax().done(function(data) {
                    self.renderData(data);
                }) : self.renderData(0);
            },
            renderData: function(data) {
                var self = this;
                self.countData.push(parseInt(data));
                self.countData.length >= self.dataLength && self.countData.shift();
                self.xAxisData.push(new Date().format('mm:ss'));
                self.xAxisData.length >= self.dataLength && self.xAxisData.shift();
                minuteChartOption.title.subtext = new Date().format('yyyy-MM-dd hh:mm:ss');
                minuteChartOption.xAxis[0].data = self.xAxisData;
                minuteChartOption.series[0].data = self.countData;
                minuteChart.setOption(minuteChartOption, true);
            },
            doAjax: function() {
                var def = $.Deferred();
                $.ajax({
                    url: 'count/getRate',
                    type: 'get',
                    cache: false,
                    success: function(data) {
                        def.resolve(data);
                    },
                    error: function() {
                        def.resolve(0);
                    }
                });
                return def.promise();
            }
        };
        $.monitorInit.action();
    });

    $('#start').click(function() {
        var _this = $(this);
        if(_this.hasClass('disabled')) return;
        _this.addClass('disabled');
        $.ajax({
            url: 'start',
            type: 'get',
            success: function(data) {
                console.log(data);
                $.monitorInit.start = true;
                _this.addClass('hidden');
                $('#stop').removeClass('hidden');
                setTimeout(function() {
                    $('#stop').removeClass('disabled');
                }, 3000);
            },
            error: function(err) {
                console.log(err);
                _this.removeClass('disabled');
            }
        });
    });
    $('#stop').click(function() {
        var _this = $(this);
        if(_this.hasClass('disabled')) return;
        $.layer.confirm('确认信息', '确认停止吗？', function() {
            _this.addClass('disabled');
            $.ajax({
                url: 'stop',
                type: 'get',
                success: function(data) {
                    console.log(data);
                    $.monitorInit.start = false;
                    _this.addClass('hidden');
                    $('#start').removeClass('hidden');
                    setTimeout(function() {
                        $('#start').removeClass('disabled');
                    }, 3000);
                },
                error: function(err) {
                    console.log(err);
                    _this.removeClass('disabled');
                }
            });
        });
    });
    $('#conf_btn').click(function() {
        $.ajax({
            url: 'prop/getProps',
            dataType: 'json',
            success: function(data) {
                var postData = {},
                    html = '';
                for(var key in data) {
                    html += '<div class="form-group">' +
                            '<div class="col col-4">' +
                            '<label class="form-label pull-right">' + key + '</label>' +
                            '</div>' +
                            '<div class="col col-8">' +
                            '<input type="text" class="form-input" data-key="' + key + '" value="' + data[key] + '"/>' +
                            '</div>' +
                            '</div>';
                }
                $.layer.pop({
                    title: '配置信息',
                    content: html,
                    layerStyle: 'width: 40%; top: 10%; left: 50%; margin-left: -20%;',
                    renderCallback: function() {
                        $('.layer .layer-body input.form-input').change(function() {
                            var _this = $(this),
                                _key = _this.attr('data-key');
                            postData[_key] = _this.val();
                        });
                    },
                    callback: function() {
                        if(!postData) return;
                        $.ajax({
                            url: 'prop/updateProps',
                            type: 'post',
                            data: {
                                props: JSON.stringify(postData)
                            },
                            success: function(res) {
                                console.log(res);
                            },
                            error: function(err) {

                            }
                        })
                    }
                });
            },
            error: function(err) {
                console.log(err);
            }
        });
    });
    /* 弹出框 */
    $.layer = {
        defaultOption: {
            title: '提示信息',
            content: '',
            layerStyle: 'width: 20%; top: 20%; left: 50%; margin-left: -10%;',
            renderCallback: null,
            callback: null
        },
        pop: function(option) {
            var self = this,
                _option = $.extend(true, {}, self.defaultOption, option);

            var html = '<div class="layer-shadow">' +
                        '<div class="layer" style="' + _option.layerStyle + '">' +
                        '<div class="layer-content">' +
                        '<div class="layer-header">' +
                        '<p>' + _option.title + '</p>' +
                        '</div>' +
                        '<div class="layer-body">' + _option.content + '</div>' +
                        '<div class="layer-footer">' +
                        '<a href="javascript:;" class="btn btn-start pull-right">确定</a>' +
                        '<a href="javascript:;" class="btn btn-link pull-right">取消</a>' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>';
            $('body').append(html);
            $('.layer-shadow .layer-footer > .btn-start').click(function() {
                self.remove();
                $.isFunction(_option.callback) && _option.callback();
            });
            $('.layer-shadow .layer-footer > .btn-link').click(function() {
                self.remove();
            });
            $.isFunction(_option.renderCallback) && _option.renderCallback();
        },
        confirm: function(title, content, callback) {
            var option = {
                title: title,
                content: '<p class="confirm-info">' + content + '</p>',
                callback: callback
            };
            this.pop(option);
        },
        remove: function() {
            $('.layer-shadow').remove();
        }
    }

}(jQuery);