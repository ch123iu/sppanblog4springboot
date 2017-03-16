/*
 * @Author: Paco
 * @Date:   2017-02-10
 * +----------------------------------------------------------------------
 * | jqadmin [ jq酷打造的一款懒人后台模板 ]
 * | Copyright (c) 2017 http://jqadmin.jqcool.net All rights reserved.
 * | Licensed ( http://jqadmin.jqcool.net/licenses/ )
 * | Author: Paco <admin@jqcool.net>
 * +----------------------------------------------------------------------
 */

layui.define(['jquery', 'laytpl', 'layer', 'modal', 'ajax', 'laypage', 'form'], function(exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        tpl = layui.laytpl,
        modal = layui.modal,
        ajax = layui.ajax,
        form = layui.form(),
        laypage = layui.laypage,
        dtable = function() {
            this.options = {
                method: "GET",
                dataType: "json",
                timeout: 10000,
                cache: false,
                url: '',
                data: '',
                tplid: "list-tpl",
                listid: "list",
                pageid: "", //如果留空则不分页
                curr: 1,
                pages: 0 //分页的总页数，通过服务端获取
            };
        };

    /**
     *@todo 初始化数据
     */
    dtable.prototype.init = function(tplid) {

        this.setOption(tplid);
        var curr_page = parseInt(location.hash.replace('#!page=', ''));
        if (curr_page > 1) {
            this.setUrlPage(curr_page);
        }
        this.ajax();
        this.setFormUrl();
        modal.init();
    }

    /**
     *@设置请求参数
     *@param string id 渲染模板的ID
     *备注：模板需配置data-params属性，值需严格的json格式，如：'{"content": "add-article.html", "title": "{{item.title}}"}'
     */
    dtable.prototype.setOption = function(id) {
        if (undefined != id || null != id) {
            this.options.tplid = id;
        }
        var params = $("#" + this.options.tplid).data("params");
        if (typeof params === 'string') {
            params = JSON.parse(params)
        }
        this.options = $.extend({}, this.options, params);
        if (undefined == this.options.url || null == this.options.url) {
            this.options.url = window.location.pathname;
        }

        if (this.options.url.indexOf("_=") != -1) {
            this.options.url = this.options.url.replace(/_=\d+/, "_=" + new Date().getTime());
        } else {
            if (this.options.url.indexOf("?") != -1) {
                this.options.url = this.options.url + "&_=" + new Date().getTime();
            } else {
                this.options.url = this.options.url + "?_=" + new Date().getTime();
            }
        }
    }

    /**
     *@todo 设置url中page参数为当前点击的页数
     *@param int curr 需要跳转到的页数（即点击分页按钮后的页数）
     */
    dtable.prototype.setUrlPage = function(curr) {

        if (this.options.url.indexOf("p=") != -1) {
            this.options.url = this.options.url.replace(/p=\d+/, "p=" + curr);
        } else {
            if (this.options.url.indexOf("?") != -1) {
                this.options.url = this.options.url + "&p=" + curr;
            } else {
                this.options.url = this.options.url + "?p=" + curr;
            }
        }
        this.ajax(curr);
    }

    /**
     *@todo 把表单参数拼成url
     */
    dtable.prototype.setFormUrl = function() {
        var _this = this;
        form.on('submit(search)', function(data) {
            var params = $.param(data.field);
            if (params == "" || params == undefined) return false;
            var url = $(data.form).attr('action');
            if (url.indexOf("?") != -1) {
                _this.options.url = url + "&" + params;
            } else {
                _this.options.url = url + "?" + params;
            }
            _this.ajax();
            return false;
        })
    }

    /**
     *@todo 向服务请求数据
     */
    dtable.prototype.ajax = function(curr) {
        var _this = this,
            l = layer.load(1);
        $.ajax({
            type: _this.options.method,
            url: _this.options.url,
            dataType: _this.options.dataType,
            data: _this.options.data,
            cache: _this.options.cache,
            timeout: _this.options.timeout,
            error: function(XMLHttpRequest, status, thrownError) {

                layer.close(l);
                layer.msg('网络繁忙，请稍后重试...');
                return;
            },
            success: function(ret) {
                layer.close(l);
                if (ret.status == 200) {
                    if (undefined == ret.data || "" == ret.data) {
                        return false;
                    }
                    _this.options.pages = ret.pages;
                    _this.render(ret.data);
                    if ("" != _this.options.pageid && curr == undefined) {
                        _this.page();
                    }
                } else {
                    layer.msg('请求数据出错，请稍后再试...');
                    return;
                }
            }
        });
    }

    /**
     *@todo 数据绑定到模板视图
     *@param json data 后台取得的数据
     */
    dtable.prototype.render = function(data) {

        var _this = this;
        if ("" != data || undefined != data) {
            var getTpl = $('#' + _this.options.tplid).html();

            tpl(getTpl).render(data, function(html) {
                $('#' + _this.options.listid).html(html);
            })
        }

        _this.selectAll();
        modal.init();
        ajax.init();
        form.render();
    }


    dtable.prototype.selectAll = function() {
        var _this = this;
        form.on('checkbox(check)', function(data) {

            var checked = data.elem.checked,
                name = $(data.elem).data('name');
            $('#' + _this.options.listid).find("input[name=" + name + "]").prop("checked", function() {
                return checked;
            });
            form.render();
        });
    }

    dtable.prototype.page = function() {
        var _this = this;
        laypage({
            cont: $(_this.options.pageid),
            pages: _this.options.pages,
            first: 1,
            last: _this.options.pages,
            prev: '<em><</em>',
            next: '<em>></em>',
            curr: location.hash.replace('#!page=', ''), //获取hash值为fenye的当前页
            hash: 'page', //自定义hash值
            jump: function(obj, first) {
                if (!first) {
                    _this.setUrlPage(obj.curr);
                }
            }
        });
    }

    exports('dtable', dtable);
});