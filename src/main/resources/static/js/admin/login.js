layui.define(['jquery', 'jqform', 'element'], function(exports) {
    var $ = layui.jquery,
        form = layui.jqform;
    
    form.set({
    	"blur": true,
    	"form": "#loginForm"
    }).init();
    
    //自定义
    form.verify({
    	pass: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格']
    });

    
    exports('login', {});
});