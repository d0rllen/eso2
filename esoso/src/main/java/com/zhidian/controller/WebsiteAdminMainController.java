package com.zhidian.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.zhidian.bases.AppEnumDefine;
import com.zhidian.exception.PageArgumentsException;
import com.zhidian.service.AdminMainSupportService;
import com.zhidian.service.DataInfoAdminService;
import com.zhidian.util.BasicUtils;
import com.zhidian.views.PullArticleUpdateModel;
import com.zhidian.views.ResultModel;
import com.zhidian.views.ResultSimpleModel;
import com.zhidian.views.WebsitePostModel;
import com.zhidian.views.WebsitePostModel2;

@RestController
@RequestMapping("/admin/website/settings")
public class WebsiteAdminMainController {

	@Autowired
	DataInfoAdminService dataService;

	@Autowired
	AdminMainSupportService mainService;

	@PostMapping("/add")
	public Object addNewWebsite(@ModelAttribute @Valid WebsitePostModel2 model, MultipartHttpServletRequest request,
			BindingResult error) {
		ResultModel result = new ResultModel();
		if (error.getErrorCount() > 0) {
			result.setMessage("参数验证不通过!");
		} else {
			MultipartFile f1 = request.getFile("pageProcessorClass");
			MultipartFile f2 = request.getFile("pageRObjectClass");
			MultipartFile f3 = request.getFile("resultProcessorClass");
			if (f1 != null && f2 != null && f3 != null) {
				if (f1.getOriginalFilename().lastIndexOf(".class") <= 0
						|| f2.getOriginalFilename().lastIndexOf(".class") <= 0
						|| f3.getOriginalFilename().lastIndexOf(".class") <= 0) {
					result.setMessage("字节码文件的格式不正确!");
					return result;
				} else {
					// 校验其他的字节码文件是否上传
					MultipartFile f4 = request.getFile("pagePipelineClass");
					MultipartFile f5 = request.getFile("resultPipelineClass");
					MultipartFile f6 = request.getFile("resultRObjectClass");
					if (f4 != null) {
						if (f4.getOriginalFilename().lastIndexOf(".class") <= 0) {
							result.setMessage("字节码文件的格式不正确!");
							return result;
						}
					}
					if (f5 != null) {
						if (f5.getOriginalFilename().lastIndexOf(".class") <= 0) {
							result.setMessage("字节码文件的格式不正确!");
							return result;
						}
					}
					if (f6 != null) {
						if (f6.getOriginalFilename().lastIndexOf(".class") <= 0) {
							result.setMessage("字节码文件的格式不正确!");
							return result;
						}
					}
					// 校验上传的配置文件格式是否正确[待改进]

					// 所有文件上傳成功之後的操作...
					int i = dataService.addNewWebsite(model, "Admin");
					if (i == 1) {
						// 开始保存文件
						String root = System.getProperty("webapp.root");
						String r = root + File.separator + "WEB-INF" + File.separator + "classes2" + File.separator
								+ "com" + File.separator + "zhidian" + File.separator;
						File f = new File(
								r + "bases" + File.separator + "worms" + File.separator + "processor" + File.separator,
								f1.getOriginalFilename());
						try {
							BasicUtils.copyFromBytes(f1.getBytes(), f);// pageProcessor
							f = new File(r + "model" + File.separator + "websites" + File.separator + "answer"
									+ File.separator, f2.getOriginalFilename());// pageRObject
							BasicUtils.copyFromBytes(f2.getBytes(), f);
							f = new File(
									r + "bases" + File.separator + "worms" + File.separator + "processor"
											+ File.separator, // resultProcessor
									f3.getOriginalFilename());
							BasicUtils.copyFromBytes(f3.getBytes(), f);
							if (f4 != null) {
								f = new File(r + "bases" + File.separator + "worms" + File.separator + "pipeline"
										+ File.separator, f4.getOriginalFilename());// pagePipeline
								BasicUtils.copyFromBytes(f4.getBytes(), f);
							}
							if (f5 != null) {
								f = new File(r + "bases" + File.separator + "worms" + File.separator + "pipeline"
										+ File.separator, f5.getOriginalFilename());// resultPipeline
								BasicUtils.copyFromBytes(f5.getBytes(), f);
							}
							if (f6 != null) {
								f = new File(r + "model" + File.separator + "websites" + File.separator + "answer"
										+ File.separator, f6.getOriginalFilename());// resultRObject
								BasicUtils.copyFromBytes(f6.getBytes(), f);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						result.setMessage("新增成功!");
					} else if (i == -1) {
						result.setMessage("新增失败,参数异常");
					}
				}
			} else {
				result.setMessage("字节码文件不能为空!");
				return result;
			}
			System.out.println(JSON.toJSONString(model));
			result.setMessage("验证中...");
		}
		return result;
	}

	@DeleteMapping("/delete/{id:[0-9]*}")
	public Object deleteWebsiteById(@PathVariable("id") int id) {
		ResultSimpleModel result = new ResultSimpleModel();
		int i = dataService.deleteWebsiteById(id);
		if (i == 1) {
			result.setMessage("删除成功!");
		} else if (i == 0) {
			result.setMessage("删除失败!");
		} else if (i == -1) {
			result.setMessage("参数有误!");
		} else if (i == -2) {
			result.setMessage("不可删除!");
		}
		return result;
	}

	@PostMapping("/upload")
	public Object uploadTest(MultipartHttpServletRequest request) {
		MultipartFile f = request.getFile("file");
		if (f != null) {
			System.out.println(f.getOriginalFilename());
			System.out.println(f.getContentType());
			System.out.println(f.getSize());
			String root = System.getProperty("webapp.root");
			File f1 = new File(root + "/WEB-INF/classes2/com/common/", f.getOriginalFilename());// resultPipeline
			try {
				BasicUtils.copyFromBytes(f.getBytes(), f1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "yes";
		}
		return "no!";
	}

	@PostMapping("/updateWebsite")
	public Object updateWebsite(@ModelAttribute @Valid WebsitePostModel model, BindingResult error) {
		// 可能需要接收到上传的字节码文件。
		ResultModel result = new ResultModel();
		if (error != null && error.getErrorCount() > 0) {
			result.setMessage("检查参数!");
		} else {
			dataService.updateWebsiteFromPostObject(model, "Admin");
			result.setCode("200");
			result.setMessage("更新成功!");
		}
		return result;
	}

	// 上面代码待验证

	@PostMapping("/setDefault")
	public Object setPullArticleDefaultUsing(@RequestParam("id") int id, @RequestParam("name") String name)
			throws PageArgumentsException {
		ResultModel result = new ResultModel();
		int num = mainService.setPullArticleDefaultUsing(id, name);
		if (num > 0) {
			result.setMessage("操作成功!");
		} else {
			result.setMessage("操作失败!");
		}
		return result;
	}

	@PostMapping("/deleteItem")
	public Object deletePullArticle(@RequestParam("id") int id, @RequestParam("name") String name)
			throws PageArgumentsException {
		ResultModel result = new ResultModel();
		int num = mainService.deletePullArticle(id, name);
		if (num > 0) {
			result.setMessage("操作成功!");
		} else {
			result.setMessage("操作失败!");
		}
		return result;
	}

	@PostMapping("/updateItemService")
	public Object updatePullArticleService(@RequestParam("id") int id, @RequestParam("name") String name,
			HttpServletRequest request) throws PageArgumentsException {
		ResultModel result = new ResultModel();
		Enumeration<String> map = request.getAttributeNames();
		// 取出有效的key，value
		if (map != null) {
			List<String> list = new ArrayList<String>();
			while (map.hasMoreElements()) {
				String key = map.nextElement();
				if (key != null && key.length() > 0) {
					// 校验 key 是否存在 AppEnumDefine.ConfigWebsiteService
					try {
						if (AppEnumDefine.ConfigWebsiteService.valueOf(key) != null) {
							// 记录key and value
							Integer value = (Integer) request.getAttribute(key);
							if (value != null && value == 0) {// 0代表关闭，关闭直接进入数据库
								list.add(key);
							}
						}
					} catch (Exception e) {
						// valueOf如果不是枚举类型的值会报错！
					}
				}
			}
			if (list.size() > 0) {
				int num = mainService.updateItemServiceByListKey(id, name, list);
				if (num > 0) {
					result.setMessage("操作成功!");
				} else {
					result.setMessage("操作失败!");
				}
			} else {
				// 没有更新的，就直接告知忽略了!
			}
		}
		return result;
	}

	@PostMapping("/updateItemInfo")
	public Object updatePullArticle(@ModelAttribute @Valid PullArticleUpdateModel article, BindingResult error)
			throws PageArgumentsException {
		ResultModel result = new ResultModel();
		if (error != null && error.getErrorCount() > 0) {
			result.setMessage("参数异常!");
		} else {
			int num = mainService.updateItemInfo(article, "Admin");
			if (num > 0) {
				result.setMessage("操作成功!");
			} else {
				result.setMessage("操作失败!");
			}
		}
		return result;
	}

	@PostMapping("/web/deleteWebsiteForce")
	public Object deleteWebsiteForceByWebsiteIdAndName(@RequestParam("id") String websiteId,
			@RequestParam("name") String name) throws PageArgumentsException {
		ResultModel result = new ResultModel();
		int num = mainService.deleteWebsiteForceByWebsiteIdAndName(websiteId,name);
		if(num>0){
			result.setCode("200");
			result.setMessage("操作成功!");
		}else{
			result.setCode("401");// 可能前置条件有
			result.setMessage("操作失败!");
		}
		return result;
	}
	
	@PostMapping("/web/deleteWebsite")
	public Object deleteWebsiteByWebsiteIdAndName(@RequestParam("id") String websiteId,
			@RequestParam("name") String name) throws PageArgumentsException {
		ResultModel result = new ResultModel();
		int num = mainService.deleteWebsiteByWebsiteIdAndName(websiteId,name);
		if(num>0){
			result.setCode("200");
			result.setMessage("操作成功!");
		}else{
			result.setCode("401");// 可能前置条件有
			result.setMessage("操作失败!");
		}
		return result;
	}
	
	@PostMapping("/web/setDefaultWebsite")
	public Object setDefaultWebsiteByWebsiteIdAndName(@RequestParam("id") String websiteId,
			@RequestParam("name") String name) throws PageArgumentsException {
		ResultModel result = new ResultModel();
		int num = mainService.updateWebsiteForSetDefaultByWebsiteIdAndName(websiteId,name);
		if(num>0){
			result.setCode("200");
			result.setMessage("操作成功!");
		}else{
			result.setCode("401");// 可能前置条件有
			result.setMessage("操作失败!");
		}
		return result;
	}
}
