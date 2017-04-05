<#include "/front/layout/__layout.ftl">
<@html>
<!--nav start-->
<div class="qing-blog-nav">
	<a href="${ctx!}/"><i class="am-icon-home"></i>首页</a> 
	<i class="am-icon-angle-double-right"></i> <a href="${ctx!}/b/${(blog.category.id)!}">${(blog.category.name)!}</a>
	<i class="am-icon-angle-double-right"></i> <a>${(blog.title)!}</a>
</div>
<!--nav end-->

<!--blog start -->
<div class="qing-content">
	<!-- blog header start -->
	<div class="qing-blog-header qing-text-center">
		<h1 class="am-article-title">${(blog.title)!}</h1>
		<div class="qing-blog-tips">
			<span><i class="am-icon-user qing-color-author" title="作者"></i>&nbsp;${(blog.author.nickName)!}</span>&nbsp; 
			<span><i class="am-icon-clock-o qing-color-clock" title="时间"></i>&nbsp;${(blog.createAt)!}</span>&nbsp; 
			<span><i class="am-icon-eye-slash qing-color-eye" title="阅读"></i>&nbsp;阅读(<em>${(blog.views)!}</em>)</span>&nbsp;
			<span><i class="am-icon-comments-o qing-color-comment" title="评论"></i>&nbsp;评论(<em><span class="ds-thread-count" data-thread-key="${(blog.id)!}"></span></em>)</span>&nbsp; 
<!-- 			<span><i class="am-icon-heart-o qing-color-heart" title="点赞"></i> 点赞(<em>0</em>)</span> -->
		</div>
	</div>
	<!-- blog header end -->
	
	<!-- blog content start -->
	<div class="qing-margin-top">${(blog.content)!}</div>
	<!-- blog content end -->

	<!-- blog tag  start -->
	<div class="qing-margin-top qing-blog-tag">
		<span class="am-icon-tags"> &nbsp;</span> 
		<#list blog.tags?split(",") as tag>
			<a href="${ctx!}/t/${tag}"><span class="am-radius">${tag}</span></a>
		</#list>
	</div>
	<!-- blog tag  end -->
	
</div>
<!--blog end -->


<!-- comment start-->
<div class="qing-content qing-margin-top">
</div>


<!-- comment end-->
<#assign js>
<script type="text/javascript">
layui.use(['code'], function(){ //加载code模块
	layui.code({
		elem: 'pre', //默认值为.layui-code
		title: '程序代码',
		about: false
	});
});
</script>
</#assign>
</@html>