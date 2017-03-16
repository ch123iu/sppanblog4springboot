/*
 * @Author: Paco
 * @Date:   2017-02-08
 * +----------------------------------------------------------------------
 * | jqadmin [ jq酷打造的一款懒人后台模板 ]
 * | Copyright (c) 2017 http://jqadmin.jqcool.net All rights reserved.
 * | Licensed ( http://jqadmin.jqcool.net/licenses/ )
 * | Author: Paco <admin@jqcool.net>
 * +----------------------------------------------------------------------
 */

layui.define(['jquery', 'layer', 'form'], function(exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form(),
        modal = function() {
            this.options = {
                type: 1,
                title: false,
                full: false,
                shadeClose: true,
                shade: false,
                content: "",
                area: 'auto',
                anim: 5,
            };
        };

    /**
     *@todo 初始化方法
     */
    modal.prototype.init = function() {
        var _this = this;
        //绑定模态iframe
        $(".modal-iframe:not([modal-bind])").each(function() {
            _this.setBind($(this));
            $(this).bind('click', _this.iframe);
        });

        //绑定模态捕获页
        $(".modal-catch:not([modal-bind])").each(function() {
            _this.setBind($(this));
            $(this).bind('click', _this.catch);
        });
        //绑定相册
        $(".modal-photo:not([modal-bind])").each(function() {
            _this.setBind($(this));
            _this.photos;
        });
    }


    /**
     *@todo 为已绑定事件元素设置绑定属性，防止再次初始化时重复绑定
     *@param obj 当前对象
     */
    modal.prototype.setBind = function(obj) {
        obj.attr('modal-bind', 1);
    }

    /**
     *@todo 合并参数
     *@param obj 当前对象
     */
    modal.prototype.setOptions = function(obj) {

        var params = obj.data('params');
        if (undefined == params || null == params) {
            layer.msg('请为当前元素配置data-garams属性');
            return;
        }
        if (typeof params === 'string') {
            params = JSON.parse(params)
        }
        var options = $.extend({}, this.options, params);

        return options;
    }


    /**
     *@todo 打开全屏窗口
     */
    modal.prototype.iframe = function(event) {
        //取得参数并设置
        var options = modal.setOptions($(this));
        if (undefined == options.content || null == options.content) {
            layer.msg('请为当前元素配置的data-garams属性配置content值');
            return false;
        };
        var _area = "auto";
        if (options.area != "auto") {
            _area = options.area.split(',');
        }

        var l = layer.open({
            type: 2,
            title: options.title,
            shadeClose: options.shadeClose,
            shade: options.shade,
            area: _area,
            content: options.content
        });
        if (options.full) {
            layer.full(l);
        }
    }

    /**
     *@todo 捕捉窗口
     */
    modal.prototype.catch = function() {

        //取得参数并设置
        var options = modal.setOptions($(this));
        if (undefined == options.content || null == options.content) {
            layer.msg('请为当前元素配置的data-garams属性配置content值');
            return;
        }
        var _area = "auto";
        if (options.area != "auto") {
            _area = options.area.split(',');
        }
        //判断是否有表单存在，如果有就重置表单
        if ($(options.content).find("form").length > 0) {
            $(options.content).find("form")[0].reset();
        }

        //判断是否需要为表单填充数据
        if (undefined != options.data || null != options.data) {
            var inputHtml = "";
            // if (options.data.indexOf('&')) {
            var data = options.data.split("&");
            for (var i = 0; i < data.length; i++) {
                var val = data[i].split("=");
                if (val[0] == "url") {
                    $(options.content).find("form").attr("action", val[1]);
                    continue;
                }
                var obj = $(options.content).find('input[name=' + val[0] + ']');
                if (obj.length > 0) {
                    if (obj.prop('type') == "text" || obj.prop('type') == "hidden") {
                        obj.val(val[1]);
                    } else if (obj.prop('type') == "checkbox" || obj.prop('type') == "radio") {
                        obj.each(function(i, n) {
                            if ($(n).val() == val[1]) {
                                $(n).prop("checked", true);
                            }
                        })

                    }
                } else if ($(options.content).find('textarea[name=' + val[0] + ']').length > 0) {
                    $(options.content).find('textarea[name=' + val[0] + ']').val(val[0]);
                } else if ($(options.content).find('select[name=' + val[0] + ']').length > 0) {
                    $(options.content).find('select[name=' + val[0] + ']').val(val[1]);
                } else {
                    inputHtml += '<input type="hidden" name="' + val[0] + '" value="' + val[1] + '" />';
                }
            }
            form.render();

            $(options.content).find("form").append(inputHtml);
        }

        var l = layer.open({
            type: options.type,
            title: options.title,
            shade: options.shade,
            shadeClose: options.shadeClose,
            area: _area,
            content: $(options.content)
        });

        if (options.full) {
            layer.full(l);
        }
    }


    /**
     *@todo 相册窗口
     */

    modal.prototype.photos = function() {
        var options = modal.setOptions($(this));
        if (undefined == options.content || null == options.content) {
            layer.msg('请为当前元素配置的data-garams属性配置content值');
            return;
        }
        layer.photos({
            photos: options.content,
            anim: options.anim
        });
    }

    var modal = new modal();
    //modal.init();
    exports('modal', modal);
});