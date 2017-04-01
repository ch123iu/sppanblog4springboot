package net.sppan.blog.controller.admin;

import javax.annotation.Resource;

import net.sppan.blog.common.JsonResult;
import net.sppan.blog.controller.BaseController;
import net.sppan.blog.service.OptionsService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/about")
public class AdminAboutController extends BaseController{
	
	@Resource
	private OptionsService optionsService;
	
	@GetMapping("/index")
	public String index(){
		return "admin/about/index";
	}
	
	@PostMapping("/save")
	@ResponseBody
	public JsonResult save(
			String content
			){
		try {
			optionsService.saveAboutMe(content);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok();
		
	}
}
