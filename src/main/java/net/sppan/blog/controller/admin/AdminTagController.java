package net.sppan.blog.controller.admin;

import javax.annotation.Resource;

import net.sppan.blog.common.JsonResult;
import net.sppan.blog.controller.BaseController;
import net.sppan.blog.entity.Tag;
import net.sppan.blog.service.TagService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/tag")
public class AdminTagController extends BaseController {
	@Resource
	private TagService tagService;

	@GetMapping("/index")
	public String index() {
		return "admin/tag/index";
	}

	@PostMapping("/list")
	@ResponseBody
	public Page<Tag> list() {
		PageRequest pageRequest = getPageRequest();
		Page<Tag> page = tagService.findAll(pageRequest);
		return page;
	}

	@GetMapping("/form")
	public String form(@RequestParam(required=false) Long id,
			ModelMap map
			){
		if(id != null){
			Tag tag = tagService.findById(id);
			map.put("tag", tag);
		}
		return "admin/tag/form";
	}
	
	@PostMapping("/save")
	@ResponseBody
	public JsonResult save(Tag tag){
		try {
			tagService.saveOrUpdate(tag);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
	}
	
	@PostMapping("/{id}/del")
	@ResponseBody
	public JsonResult delete(
			@PathVariable Long id
			){
		try {
			tagService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
	}
	
	@PostMapping("/{id}/changeStatus")
	@ResponseBody
	public JsonResult changeStatus(@PathVariable Long id){
		try {
			tagService.changeStatus(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
	}
	
	
}
