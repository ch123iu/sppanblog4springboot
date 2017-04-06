package net.sppan.blog.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import net.sppan.blog.common.JsonResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UploadController extends BaseController{
	
	
	@Resource
	private ServletContext servletContext;
	
	@ResponseBody
	@RequestMapping("/upload/image")
	public JsonResult upload(@RequestParam("upload_file") MultipartFile file) throws IOException{
		String relativePath;
		String newFileName;
		try {
			if(!isLogin()){
				return JsonResult.create("success", false).set("msg", "没有登录不能上传文件");
			}
			if (file == null) {
				return JsonResult.create("success",false).set("msg", "上传文件为 null");
			}
			if (file.getBytes().length > 200*1024) {
				return JsonResult.create("success",false).set("msg", "图片尺寸只允许 200K 大小");
			}
			// 获取上传的文件名
			String fileName = file.getOriginalFilename();
			// 获取文件的后缀名
			String extName = fileName .substring(fileName.lastIndexOf("."));
			// 文件保存的相对位置，相对于upload
			relativePath = File.separator + "images" + File.separator;
			// 生成的文件名
			newFileName = generateFileName(getLoginUser().getId(), extName);
			// 文件保存的完整目录
			String absolutePath = servletContext.getRealPath("upload") + relativePath;
			File temp = new File(absolutePath);
			if (!temp.exists()) {
				temp.mkdirs();  // 如果目录不存在则创建
			}
			BufferedOutputStream stream = null;
			stream = new BufferedOutputStream(new FileOutputStream(absolutePath + newFileName));
			if(stream != null){
				stream.write(file.getBytes());
				stream.close();
			}
			//返回给编辑器的文件位置
			String resultFilePath = File.separator + "upload" + relativePath + newFileName;
			return JsonResult.create("success", true).set("file_path", resultFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.create("success",false).set("msg", e.getMessage());
		}
		
	}
	
	/**
	 * 生成规范的文件名
	 */
	private String generateFileName(Long userId, String extName) {
		return userId + "_" + System.currentTimeMillis() + extName;
	}

}
