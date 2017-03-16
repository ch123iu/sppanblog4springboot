/*
 * @Author: Paco
 * @Date:   2017-02-16
 * +----------------------------------------------------------------------
 * | jqadmin [ jq酷打造的一款懒人后台模板 ]
 * | Copyright (c) 2017 http://jqadmin.jqcool.net All rights reserved.
 * | Licensed ( http://jqadmin.jqcool.net/licenses/ )
 * | Author: Paco <admin@jqcool.net>
 * +----------------------------------------------------------------------
 */

layui.define(['form', 'layer'], function(exports) {
    "use strict";

    var $ = layui.jquery,
        device = layui.device(),
        forms = layui.form(),
        layer = layui.layer,
        MOD_NAME = 'jqform',
        ELEM = '.layui-form',
        THIS = 'layui-this',
        Jqform = function() {
            this.config = {
                verify: {
                    required: [
                        /[\S]+/, '必填项不能为空'
                    ],
                    phone: [
                        /^1\d{10}$/, '请输入正确的手机号'
                    ],
                    email: [
                        /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/, '邮箱格式不正确'
                    ],
                    url: [
                        /(^#)|(^http(s*):\/\/[^\s]+\.[^\s]+)/, '链接格式不正确'
                    ],
                    number: [
                        /^\d+$/, '只能填写数字'
                    ],
                    date: [
                        /^(\d{4})[-\/](\d{1}|0\d{1}|1[0-2])([-\/](\d{1}|0\d{1}|[1-2][0-9]|3[0-1]))*$/, '日期格式不正确'
                    ],
                    identity: [
                        /(^\d{15}$)|(^\d{17}(x|X|\d)$)/, '请输入正确的身份证号'
                    ]
                },
                blur: true,
                form: "form",
                ajax: true,
                method: 'POST',
                data: '',
                dataType: 'json',
                timeout: 50000,
                cache: false,
                loading: true //是否显示loading图片

            };
        };

    /**
     *@todo 设置参数
     */
    Jqform.prototype.set = function(options) {
        var that = this;
        $.extend(true, that.config, options);
        return that;
    };


    /**
     *@todo 合并配雷超参数
     */
    Jqform.prototype.verify = function(settings) {
        var that = this;
        $.extend(true, that.config.verify, settings);
        return that;
    };

    Jqform.prototype.init = function() {
        var _this = this;
        if (this.config.blur) {
            $(this.config.form).find('*[jq-verify]').blur(function() {
                _this.check($(this));
            });

            forms.on('select(verify)', function(data) {
                _this.check($(data.elem));
            });
        }
    }

    /**
     *@todo 根据规则验证数据
     *@param object obj 当前点击的提交按钮对象
     */
    Jqform.prototype.check = function(obj) {
        var ver = obj.attr('jq-verify').split('|'),
            err = obj.attr('jq-error'),
            errorId = obj.attr('error-id'),
            tips = '',
            stop = false,
            DANGER = 'has-warning',
            SUCCESS = 'has-success',
            verify = this.config.verify,
            value = obj.val(),
            itemOuterHeight = 0,
            top = 0;
        obj.parent('div').removeClass(DANGER);

        if (ver.indexOf("required") == -1 && "" == value && ver.indexOf("content") == -1) {
            return;
        }
        if (err) {
            err = err.split('|');
        }

        $.each(ver, function(index, thisVer) {
            var isFn = typeof verify[thisVer] === 'function';
            if (verify[thisVer] && (isFn ? tips = verify[thisVer](value, obj) : !verify[thisVer][0].test(value))) {
                var errHtml = "",
                    errMsg = (tips || (err ? err[index] : false) || verify[thisVer][1]);
                if (errorId == undefined) {
                    if (parseInt(obj.width()) > 200) {
                        errHtml = '<p class="jq-error">' + errMsg + '</p>';
                        if (obj.next('.jq-error').length <= 0) {
                            obj.after(errHtml);
                        } else {
                            obj.next('.jq-error').html(errMsg);
                        }

                    } else {
                        errHtml = '<div class="error"><p>' + errMsg + '</p></div>';
                        if (obj.parent('div').next('.error').length <= 0) {
                            obj.parent('div').after(errHtml);
                        } else {
                            obj.parent('div').next('.error').html(errMsg);
                        }

                    }
                    obj.parent('div').addClass(DANGER);
                    top = obj.parent('div').offset();
                    itemOuterHeight = obj.outerHeight();

                } else {
                    $('#' + errorId).html(errMsg);
                    top = $('#' + errorId).offset();
                    itemOuterHeight = $('#' + errorId).outerHeight();
                }
                var winScrollTop = $(window).scrollTop(),
                    itemOffsetTop = top.top,

                    winHeight = $(window).height();

                if ((winScrollTop > itemOffsetTop + itemOuterHeight) || (winScrollTop < itemOffsetTop - winHeight)) {
                    $('body,html').stop().animate({ scrollTop: top.top - 20 }, 500);
                }

                stop = true;
                return;
            } else {
                if (errorId == undefined) {
                    obj.parent('div').removeClass(DANGER);
                    if (parseInt(obj.width()) > 200) {
                        obj.next('.jq-error').remove();
                    } else {
                        obj.parent('div').next('.error').remove();
                    }
                } else {
                    $('#' + errorId).empty();
                }
            }
        })
        return stop;
    }

    /**
     *@todo ajax 事件  这里callBack特意采用json格式，如果有需要可以在这里修改
     *@param event object 事件对象
     *@param options object 拼装好的参数对象
     **/
    Jqform.prototype.ajax = function(options, form) {
        if (options == undefined || options == null) {
            return;
        }
        //变为大写
        options.method = options.method.toUpperCase();

        if (options.loading == true) {
            var l = layer.load(1);
        }

        $.ajax({
            type: options.method,
            url: options.url,
            dataType: options.dataType,
            data: options.data,
            timeout: options.timeout,
            cache: options.cache,
            error: function(XMLHttpRequest, status, thrownError) {
                if (options.loading == true) {
                    layer.close(l);
                }
                layer.msg('网络繁忙，请稍后重试...');
                return false;
            },
            success: function(msg) {
                if (options.loading == true) {
                    layer.close(l);
                }
                if (!form.callBack(msg, options)) return;
            }
        });
    };

    /**
     *@todo ajax成功后执行方法
     *@param ret object 服务端返回的信息 ret={status:200,data:data,url:baidu.com}
     *@param options object 拼装好的参数对象
     **/
    Jqform.prototype.callBack = function(ret, options) {
        if ((undefined == ret) || (null == ret))
            return false;
        if (options.complete) {
            //自定义方法
            eval(options.complete + "(ret,options,$)");
            return false;
        }
        if (0 == ret.code) {
            var index = undefined;
            if (window.name) {
                index = parent.layer.getFrameIndex(window.name);
            }
            if ((undefined == ret.data) || (null == ret.data) || ("" == ret.data)) {
                layer.msg(ret.message);
                return false;
            } else {
                if ('reload' == ret.data) {
                    if (index == undefined) {
                        location.reload();
                    } else {
                        parent.layer.close(index);
                        parent.location.reload();
                    }
                } else if (index == undefined) {
                    location.href = ret.data;
                } else {
                    parent.layer.close(index);
                    parent.location.href = ret.data;
                }
            }
        } else {
            layer.msg(ret.message)
        }
        return true;
    };


    /**
     *提交表单时较验
     */
    Jqform.prototype.submit = function(event) {
        var button = $(this),
            form = event.data.form,
            stop = null,
            field = {},
            elem = button.parents(ELEM)

        , verifyElem = elem.find('*[jq-verify]') //获取需要校验的元素

        , formElem = button.parents('form')[0] //获取当前所在的form元素，如果存在的话
            , fieldElem = elem.find('input,select,textarea') //获取所有表单域
            , filter = button.attr('lay-filter') //获取过滤器

        //开始校验
        layui.each(verifyElem, function(_, item) {
            stop = form.check($(this));
            return stop;
        });

        if (stop) return false;

        layui.each(fieldElem, function(_, item) {
            if (!item.name) return;
            if (/^checkbox|radio$/.test(item.type) && !item.checked) return;
            field[item.name] = item.value;
        });

        if (form.config.ajax) {
            var options = {
                "data": field
            }

            if (undefined != $(formElem).attr("method")) {
                options.method = $(formElem).attr("method");
            }

            if (undefined != $(formElem).attr("action")) {
                options.url = $(formElem).attr("action");
            }

            if (undefined != $(formElem).attr("complete")) {
                options.complete = $(formElem).attr("complete");
            }
            options = $.extend({}, form.config, options);
            form.ajax(options, form);
            return false;
        } else {
            //获取字段
            return layui.event.call(this, 'form', 'submit(' + filter + ')', {
                elem: this,
                form: formElem,
                field: field
            });
        }
    };

    //自动完成渲染
    var form = new Jqform(),
        dom = $(document);
    dom.on('click', '*[jq-submit]', { form: form }, form.submit);

    exports(MOD_NAME, form);
});