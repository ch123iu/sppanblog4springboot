package net.sppan.blog.controller.admin;

import javax.annotation.Resource;

import net.sppan.blog.common.JsonResult;
import net.sppan.blog.controller.BaseController;
import net.sppan.blog.entity.User;
import net.sppan.blog.service.UserService;

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
@RequestMapping("/admin/user")
public class AdminUserController extends BaseController {
	@Resource
	private UserService userService;

	@GetMapping("/index")
	public String index() {
		return "admin/user/index";
	}

	@PostMapping("/list")
	@ResponseBody
	public Page<User> list() {
		PageRequest pageRequest = getPageRequest();
		Page<User> page = userService.findAll(pageRequest);
		return page;
	}

	@GetMapping("/form")
	public String form(@RequestParam(required=false) Long id,
			ModelMap map
			){
		if(id != null){
			User user = userService.findById(id);
			map.put("user", user);
		}
		return "admin/user/form";
	}
	
	@PostMapping("/save")
	@ResponseBody
	public JsonResult save(User user){
		try {
			userService.saveOrUpdate(user);
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
			userService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
	}
	
	
}
