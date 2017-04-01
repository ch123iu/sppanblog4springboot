package net.sppan.blog.controller.admin;

import javax.annotation.Resource;

import net.sppan.blog.common.JsonResult;
import net.sppan.blog.controller.BaseController;
import net.sppan.blog.entity.Category;
import net.sppan.blog.service.CategoryService;

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
@RequestMapping("/admin/category")
public class AdminCategoryController extends BaseController {
	@Resource
	private CategoryService categoryService;

	@GetMapping("/index")
	public String index() {
		return "admin/category/index";
	}

	@PostMapping("/list")
	@ResponseBody
	public Page<Category> list() {
		PageRequest pageRequest = getPageRequest();
		Page<Category> page = categoryService.findAll(pageRequest);
		return page;
	}

	@GetMapping("/form")
	public String form(@RequestParam(required=false) Long id,
			ModelMap map
			){
		if(id != null){
			Category category = categoryService.findById(id);
			map.put("category", category);
		}
		return "admin/category/form";
	}
	
	@PostMapping("/save")
	@ResponseBody
	public JsonResult save(Category category){
		try {
			categoryService.saveOrUpdate(category);
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
			categoryService.delete(id);
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
			categoryService.changeStatus(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
	}
	
	
}
