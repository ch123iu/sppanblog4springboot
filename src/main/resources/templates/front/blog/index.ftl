<#include "/front/layout/__layout.ftl">
<#include "/front/layout/_paginate.ftl">
<@html>
<div id="blog-list">
<#list blogPage.list as x>
	<div class="qing-entry-text">
   	 <div class="qing-list-title">
   	 	<span class="qing-category">${(x.categoryName)!}<i></i></span><a href="${ctx!}/b/view/${x.id}"> ${x.title}</a>
   	 </div>
     <div class="qing-list-hint">
       	<span><i class="am-icon-user qing-color-author" title="作者"></i>&nbsp;${x.authorName}</span>&nbsp;
	    <span><i class="am-icon-clock-o qing-color-clock" title="时间"></i>&nbsp;${x.createAt}</span>&nbsp;
	    <span><i class="am-icon-eye-slash qing-color-eye" title="阅读"></i>&nbsp;阅读(${x.views})</span>&nbsp;
        <span><i class="am-icon-comments-o qing-color-comment" title="评论"></i>&nbsp;评论(<span class="ds-thread-count" data-thread-key="${x.id}"></span>)</span>&nbsp;
<!--         <span><i class="am-icon-heart-o qing-color-heart" title="点赞"></i> 点赞(1)</span> -->
     </div>
     	<p class="qing-list-content">${x.summary}</p>
      	<div class="qing-list-foot">
      			<i class="am-icon-tags"></i>
				<span class="am-radius">${x.tags}</span>
          	<a href="${ctx!}/b/view/${x.id}" class="qing-read-more">阅读全文>></a>
     	 </div> 
   </div>
</#list>
<#-- 
<div class="qing-content">
	没有任何数据
</div>
 -->
</div>
<!-- 分页 -->
<@paginate currentPage=blogPage.pageNumber  totalPage=blogPage.totalPage link="/b/" + c + "?p=" />
</@html>
