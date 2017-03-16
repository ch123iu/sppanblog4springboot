/*
 * @Author: Paco
 * @Date:   2017-03-12
 * +----------------------------------------------------------------------
 * | jqadmin [ jq酷打造的一款懒人后台模板 ]
 * | Copyright (c) 2017 http://jqadmin.jqcool.net All rights reserved.
 * | Licensed ( http://jqadmin.jqcool.net/licenses/ )
 * | Author: Paco <admin@jqcool.net>
 * +----------------------------------------------------------------------
 */

layui.define(['jquery', 'laytpl', 'tabmenu', 'layer', 'elem'], function(exports) {
    var $ = layui.jquery,
        tpl = layui.laytpl,
        element = layui.elem(),
        tabmenu = layui.tabmenu(),
        layer = layui.layer,
        jqmenu = function() {
            this.options = {
                method: "GET",
                dataType: "json",
                timeout: 10000,
                cache: false,
                url: '',
                data: '',
                tplid: "menu-tpl",
                listid: "menu",
                icon: false
            };
        };

    /**
     *@todo 初始化数据
     */
    jqmenu.prototype.init = function(tplid) {
        this.setOption(tplid);
    }

    /**
     *@设置请求参数
     *@param string id 渲染模板的ID
     *备注：模板需配置data-params属性，值需严格的json格式，如：'{"content": "add-article.html", "title": "{{item.title}}"}'
     */
    jqmenu.prototype.setOption = function(id) {
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
        this.ajax();
    }


    /**
     *@todo 向服务请求数据
     */
    jqmenu.prototype.ajax = function() {
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
                    _this.render(ret.data);

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
    jqmenu.prototype.render = function(data) {

        var _this = this;
        if ("" != data || undefined != data) {
            var getTpl = $('#' + _this.options.tplid).html();
            tpl(getTpl).render(data, function(html) {
                $('#' + _this.options.listid).html(html);
            })
            var submenuTpl = $('#submenu-tpl').html();

            tpl(submenuTpl).render(data, function(html) {
                $('#submenu').html(html);
            })
        }

        _this.resize();
        _this.menuBind();
        element.init();
        _this.menuShowType();


    }

    /**
     *@todo 自适应窗口
     */
    jqmenu.prototype.resize = function() {
        $(window).on('resize', function() {
            tabmenu.init();
            tabmenu.tabMove(0, 1);
        }).resize();
    }

    /**
     *@todo 初始化菜单 
     */
    jqmenu.prototype.menuBind = function() {
        var _this = this;
        //初始化时显示第一个菜单
        $('.sub-menu').eq(0).slideDown();

        //绑定左侧树菜单的单击事件
        $('.sub-menu .layui-nav-item,.tab-menu').bind("click", function() {
            var obj = $(this);
            if (obj.find('dl').length > 0) {
                if (obj.attr('data-bind') == 1) {
                    return;
                }
                obj.attr('data-bind', '1');
                obj.find('dd').bind("click", function() {
                    _this.menuSetOption($(this));
                });
            } else {
                _this.menuSetOption(obj);
            }
        });

        //绑定主菜单单击事件，点击时显示相应的菜单
        element.on('nav(main-menu)', function(elem) {
            var index = (elem.index())
            $('.sub-menu').slideUp().eq(index).slideDown();
        });

        //绑定tag菜单向左滚动按钮事件
        $('span.move-left-btn').bind("click", function() {
            var item = tabmenu.config.item,
                ml = parseInt($('' + item + '').children('ul.layui-tab-title').css("margin-left"));
            if (ml < 0) {
                ml = ml + 130;
                if (ml > 0) {
                    ml = 0;
                }
                $('' + item + '').children('ul.layui-tab-title').css("margin-left", ml);
            }
        });

        //绑定tag菜单向右滚动按钮事件
        $('span.move-right-btn').bind("click", function() {
            var obj = $('' + tabmenu.config.item + '').children('ul.layui-tab-title'),
                ml = parseInt(obj.css("margin-left")),
                tab_all_width = parseInt(obj.prop('scrollWidth')),
                navWidth = parseInt(obj.parent('div').width());
            if (ml - navWidth > -tab_all_width) {
                ml = ml - 130;
                if (ml <= parseInt(navWidth - tab_all_width - 54)) {
                    ml = navWidth - tab_all_width - 54;
                }
                obj.css("margin-left", ml);
            }
        });

        //禁止双击选中
        $('span.move-left-btn,span.move-right-btn').bind("selectstart", function() {
            return false;
        })

    }

    /**
     *@todo 设置菜单项，并创建tab菜单
     */
    jqmenu.prototype.menuSetOption = function(obj) {
        var $a = obj.children('a'),
            href = $a.data('url'),
            icon = $a.children('i:first').data('icon'),
            title = $a.data('title'),
            data = {
                href: href,
                icon: icon,
                title: title
            }
        tabmenu.tabAdd(data);
    }

    jqmenu.prototype.menuShowType = function(type) {

        var locationShowType = layui.data('showType');
        var showType = locationShowType.showType ? locationShowType.showType : 1;
        if (type) {
            showType = type;
            layui.data('showType', {
                key: 'showType',
                value: type
            });
        }
        if (showType == 3) {
            $('.menu-type').data('type', 1);
        } else {
            $('.menu-type').data('type', showType + 1);
        }
        var bar = $('.jqamdin-left-bar');
        var showIcon = $(".menu-type").find("i");
        switch (showType) {
            case 2:
                $('.jqadmin-body').animate({ left: '60' });
                bar.animate({ width: '60' });
                $('.jqadmin-foot').animate({ left: '60' });
                showIcon.html('&#xe616;');
                $('#submenu,.jqadmin-home').find("span").hide();
                $('#submenu').find("ul li").css("width", "60px").find("a").css("padding-left", "10px").find('i').css("font-size", "20px");
                $('.jqadmin-home').find("a").css("padding-left", "0").find('i').css("font-size", "32px");
                $('#submenu').find("ul li dl dd a").css("padding-left", "15px");
                $('#submenu').find("ul li").find("a").on('mouseenter', function() {
                    layer.tips($(this).data("title"), $(this));
                });

                if (this.options.icon) {
                    $('.jqadmin-main-menu').find("span").hide();
                    $('.jqadmin-main-menu').find("ul li").css({ "width": "64px", "line-height": "56px" }).find('i').css("font-size", "26px");
                    $('.jqadmin-main-menu').find("ul li").find("a").on('mouseenter', function() {
                        layer.tips($(this).data("title"), $(this));
                    });
                }


                break;
            case 3:

                $('.jqadmin-body').animate({ left: '0' });
                bar.animate({ width: '0' });
                $('.jqadmin-foot').animate({ left: '0' });
                showIcon.html('&#xe61a;');
                break;
            default:
                $('.jqadmin-body').animate({ left: '200' });
                bar.animate({ width: '200' });
                $('.jqadmin-foot').animate({ left: '200' });
                showIcon.html('&#xe683;');
                $('#submenu,.tab-menu').find("span").show();
                $('#submenu').find("ul li").css("width", "200px").find("a").css("padding-left", "30px").find('i').css("font-size", "14px");
                $('#submenu').find("ul li").find("a").off('mouseenter');
                $('.tab-menu').find("a").css("padding-left", "10px").find('i').css("font-size", "16px");
                if (this.options.icon) {
                    $('.jqadmin-main-menu').find("span").show();
                    $('.jqadmin-main-menu').find("ul li").css({ "width": "auto", "line-height": "60px" }).find('i').css("font-size", "14px");
                    $('.jqadmin-main-menu').find("ul li").find("a").off('mouseenter');
                }
        }
    }

    exports('jqmenu', jqmenu);
});