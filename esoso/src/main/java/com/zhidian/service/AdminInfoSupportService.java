package com.zhidian.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zhidian.bases.SearchEngineEnumDefine;
import com.zhidian.exception.PageArgumentsException;
import com.zhidian.mapper.ConfigMapper;
import com.zhidian.mapper.PullArticleMapper;
import com.zhidian.mapper.VersionMapper;
import com.zhidian.mapper.WebsiteMapper;
import com.zhidian.model.PullArticle;
import com.zhidian.model.Version;
import com.zhidian.model.Website;
import com.zhidian.model.sys.ConfigBO;
import com.zhidian.model.sys.NameValueModel;
import com.zhidian.model.sys.PullArticleBO;
import com.zhidian.model.sys.VersionBO2;
import com.zhidian.model.sys.WebsiteBO2;
import com.zhidian.model.sys.WebsiteBO3;
import com.zhidian.model.websites.config.ConfigWebsiteItemModel;
import com.zhidian.model.websites.config.ConfigWebsiteModel;
import com.zhidian.util.BasicUtils;
import com.zhidian.views.ConfigDTO;
import com.zhidian.views.ServiceSettingsDTO;
import com.zhidian.views.VersionAddVO;
import com.zhidian.views.VersionControlDTO;
import com.zhidian.views.VersionControlViewDTO;
import com.zhidian.views.VersionMainDTO;
import com.zhidian.views.VersionUpdateVO;
import com.zhidian.views.WebsiteDetailDTO;
import com.zhidian.views.WebsiteDetailDTO2;
import com.zhidian.views.WebsiteMainDTO;
import com.zhidian.views.WebsitePaDTO;
import com.zhidian.views.WebsitePalistPullArticleDTO;

@Service
public class AdminInfoSupportService {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Autowired
	ConfigMapper configMapper;

	@Autowired
	VersionMapper versionMapper;

	@Autowired
	PullArticleMapper pullMapper;

	@Autowired
	WebsiteMapper websiteMapper;

	public List<ConfigDTO> getConfigForVersionList() {
		List<ConfigBO> list = configMapper.queryConfigsForAdminInfoSupportService01ListConfigBO();
		List<ConfigDTO> configs = null;
		if (list != null && list.size() > 0) {
			configs = new ArrayList<ConfigDTO>(list.size());
			for (ConfigBO c : list) {
				if (c != null) {
					ConfigDTO d = new ConfigDTO(c);
					configs.add(d);
				}
			}
		}
		return configs;
	}

	public List<NameValueModel> getWebsitesFromVersionByType(String type) {
		String type2 = SearchEngineEnumDefine.Type.问答.getValue();
		return versionMapper.queryVersionsForAdminInfoSupportService01ListVersionBO2(type, type2);
	}

	public List<VersionControlViewDTO> getVersionBySearch(String type, String value) throws Exception {
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(value)) {
			if ("version".equals(type)) {
				int id = BasicUtils.version2Id(value);
				if (id > 0) {
					List<Version> list = versionMapper.queryVersionsForAdminInfoSupportService01ListVersion(id);
					return createVersionControlViewDTOList(list);
				} else {
					throw new PageArgumentsException("参数有误,无法转换!");
				}
			} else if ("websites".equals(type)) {
				List<Version> list = versionMapper.queryVersionsForAdminInfoSupportService02ListVersion(value);
				return createVersionControlViewDTOList(list);
			}
			throw new PageArgumentsException("参数不符合预期...");
		} else {
			throw new PageArgumentsException("参数为空..");
		}
	}

	private List<VersionControlViewDTO> createVersionControlViewDTOList(List<Version> list) {
		if (list != null && list.size() > 0) {
			List<VersionControlViewDTO> ls = new ArrayList<VersionControlViewDTO>(list.size());
			for (Version v : list) {
				if (v != null) {
					VersionControlViewDTO d = new VersionControlViewDTO();
					d.setId(v.getId());
					d.setName(v.getName());
					d.setUsing(v.getUsing() > 0 ? true : false);
					d.setNmp(v.getNmp() > 0 ? true : false);
					if (v.getCreateTime() != null) {
						d.setCreateTime(sdf.format(v.getCreateTime()));
					}
					d.setVersionId(BasicUtils.id2Version(v.getId()));
					ls.add(d);
				}
			}
			return ls;
		}
		return null;
	}

	public VersionUpdateVO getVersionInfoByVersionId(String versionId) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(versionId)) {
			int id = BasicUtils.version2Id(versionId);
			if (id > 0) {
				Version v = versionMapper.queryVersionsForAdminInfoSupportService01SimpleVersion(id);
				return createVersionUpdateViewModel(v);
			} else {
				throw new PageArgumentsException("参数不符合预期...");
			}
		} else {
			throw new PageArgumentsException("参数为空..");
		}
	}

	private VersionUpdateVO createVersionUpdateViewModel(Version version) {
		if (version != null) {
			VersionUpdateVO v = new VersionUpdateVO();
			v.setId(version.getId());
			v.setDefCss(version.getDefCss());
			v.setDefJs(version.getDefJs());
			v.setDefPage(version.getDefPage());
			v.setName(version.getName());
			v.setType(version.getType());// 需要转换
			v.setType2(version.getType2());
			v.setUsing(version.getUsing() > 0 ? true : false);
			v.setVersionId(BasicUtils.id2Version(version.getId()));
			return v;
		}
		return null;
	}

	public List<VersionControlDTO> getVersionInfoUsingList() {
		List<Version> list = versionMapper.queryVersionsForAdminInfoSupportService03ListVersion();
		return createVersionControlVO(list);
	}

	private List<VersionControlDTO> createVersionControlVO(List<Version> list) {
		if (list != null && list.size() > 0) {
			List<VersionControlDTO> vos = new ArrayList<VersionControlDTO>(list.size());
			for (Version v : list) {
				if (v != null) {
					VersionControlDTO vo = new VersionControlDTO();
					if (v.getCreateTime() != null) {
						vo.setCreateTime(sdf.format(v.getCreateTime()));
					}
					vo.setName(v.getName());
					vo.setUsing(v.getUsing() > 0 ? true : false);
					vo.setId(v.getId());
					vo.setVersionId(BasicUtils.id2Version(v.getId()));
					vos.add(vo);
				}
			}
			return vos;
		}
		return null;
	}

	public VersionAddVO getVersionAddInfoUsing() {
		List<ConfigBO> list = configMapper.queryConfigsForAdminInfoSupportService01ListConfigBO();
		VersionAddVO vo = new VersionAddVO();
		vo.setItems(list);
		return vo;
	}

	public List<WebsitePaDTO> getWebsitesPaList() {
		// 获取每个站点特有的一条正使用的数据，现获取最近记录入库的
		List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie01ListPullArticle();
		return createWebsitePalistDTO(list);
	}

	private List<WebsitePaDTO> createWebsitePalistDTO(List<PullArticle> list) {
		if (list != null && list.size() > 0) {
			List<WebsitePaDTO> dtos = new ArrayList<WebsitePaDTO>(list.size());
			for (PullArticle p : list) {
				if (p != null) {
					WebsitePaDTO d = new WebsitePaDTO();
					// 装载数据
					d.setId(p.getId());
					d.setCollets(p.getCollets());
					if (p.getStartTime() != null) {
						d.setCreateTime(sdf.format(p.getStartTime()));
					}
					d.setMark(p.getMark());
					d.setName(p.getName());
					d.setRelyVersionId(BasicUtils.id2Version(p.getWebsiteId()));
					d.setTags(p.getTags());
					d.setTitle(p.getTitle());
					d.setUrl(p.getUrl());
					d.setCollets(p.getCollets());
					d.setScores(p.getScores());
					d.setViews(p.getViews());
					d.setUuid(p.getUuid());
					d.setType(p.getType());// 单独为website-pa-verison.html加的一条
					d.setSign(p.getSign());// 单独为website-pa-verison.html加的一条
					dtos.add(d);
				}
			}
			return dtos;
		}
		return null;
	}

	public List<ServiceSettingsDTO> getItemServiceByItemsIdAndName(int id, String name) throws PageArgumentsException {
		if (id > 0 && StringUtils.isNotEmpty(name)) {
			// 通过字符串模糊查询。如果有数据，则取出字符串。
			PullArticleBO p = pullMapper.queryPullArticlesForAdminInfoSupportServcie01SimplePullArticleBO(id, name);
			if (p != null) {
				String sql = createSqlForWebsiteItem(p);
				List<ConfigBO> configs = configMapper.queryConfigsForAdminInfoSupportService02ListConfigBO(sql);
				if (configs != null && configs.size() > 0) {
					return createServiceSettingsDTOList(configs);
				} else {
					return null;
				}
			} else {
				throw new PageArgumentsException("参数异常，数据无该记录...");
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private List<ServiceSettingsDTO> createServiceSettingsDTOList(List<ConfigBO> configs) {
		if (configs != null && configs.size() > 0) {
			List<ServiceSettingsDTO> dtos = new ArrayList<ServiceSettingsDTO>(configs.size());
			for (ConfigBO c : configs) {
				if (c != null) {
					ServiceSettingsDTO s = new ServiceSettingsDTO();
					s.setId(c.getId());
					s.setName(c.getName());
					s.setUsing(false);
					dtos.add(s);
				}
			}
			return dtos;
		}
		return null;
	}

	private String createSqlForWebsite(Website w) {
		// 强制依赖，w肯定不为空
		ConfigWebsiteModel config = new ConfigWebsiteModel();
		config.setId(w.getId());
		config.setName(w.getName());
		config.setType(w.getType());
		config.setType2(w.getType2());
		return JSON.toJSONString(config);
	}

	public WebsitePalistPullArticleDTO getItemInfoByItemsIdAndName(int id, String name) throws PageArgumentsException {
		if (id > 0 && StringUtils.isNotEmpty(name)) {
			PullArticle p = pullMapper.queryPullArticlesForAdminInfoSupportServcie01SimplePullArticle(id, name);
			return createWebsitePalistPullArticleDTO(p);
		} else {
			throw new PageArgumentsException();
		}
	}

	private WebsitePalistPullArticleDTO createWebsitePalistPullArticleDTO(PullArticle p) {
		if (p != null) {
			WebsitePalistPullArticleDTO dto = new WebsitePalistPullArticleDTO();
			dto.setId(p.getId());
			dto.setVersionId(BasicUtils.id2Version(p.getId()));
			if (p.getStartTime() != null) {
				dto.setCreateTime(sdf.format(p.getStartTime()));
			}
			dto.setCollets(p.getCollets());
			dto.setCssPath(p.getCssPath());
			dto.setJsPath(p.getJsPath());
			dto.setMark(p.getMark());
			dto.setName(p.getName());
			dto.setPagePath(p.getPagePath());
			if (p.getWebsiteId() > 0) {
				dto.setRelyVersionId(BasicUtils.id2Version(p.getWebsiteId()));
			}
			dto.setResultContent(p.getResultContent());
			dto.setScores(p.getScores());
			dto.setSign(p.getSign());
			dto.setTags(p.getTags());
			dto.setTitle(p.getTitle());
			dto.setType(p.getType());
			if (p.getUpdateTime() != null) {
				dto.setUpdateTime(sdf.format(p.getUpdateTime()));
			}
			dto.setUrl(p.getUrl());
			dto.setUsing(p.getUsing() > 0 ? true : false);
			dto.setUuid(p.getUuid());
			dto.setViews(p.getViews());
			return dto;
		}
		return null;
	}

	public List<String> getWebsitesListId(int id, String name) throws PageArgumentsException {
		if (id > 0 && StringUtils.isNotEmpty(name)) {
			List<Integer> list = websiteMapper.selectWebsitesForAdminInfoSupport01ListId(id, name);
			if (list != null && list.size() > 0) {
				return createWebsiteListId(list);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private List<String> createWebsiteListId(List<Integer> list) {
		// 强依赖
		List<String> ls = new ArrayList<String>(list.size());
		for (Integer id : list) {
			if (id != null && id > 0) {
				ls.add(BasicUtils.id2Version(id));
			}
		}
		return ls;
	}

	public WebsiteMainDTO getWebsiteMainDTOInfo(String websiteId) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(websiteId)) {
			int id = BasicUtils.version2Id(websiteId);
			if (id > 0) {
				WebsiteBO2 website = websiteMapper.queryWebsitesForAdminSupportService01SimpleWebsiteBO2(id);
				return createWebsiteMainDTO(website);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private WebsiteMainDTO createWebsiteMainDTO(WebsiteBO2 website) {
		if (website != null) {
			WebsiteMainDTO w = new WebsiteMainDTO();
			w.setDefaultPageCss(website.getDefaultPageCss());
			w.setDefPageConfig(website.getDefPageConfig());
			w.setDefPageCss(website.getDefaultPageCss());
			w.setDefRequestHeader(website.getDefRequestHeader());
			w.setDefResultConfig(website.getDefResultConfig());
			w.setId(website.getId());
			w.setName(website.getName());
			w.setPagePipeline(website.getPagePipeline());
			w.setPageProcessor(website.getPageProcessor());
			w.setPageRObject(website.getPageRObject());
			w.setPagination(website.getPagination());
			w.setResultPipeline(website.getResultPipeline());
			w.setResultProcessor(website.getResultProcessor());
			w.setResultRObject(website.getResultRObject());
			w.setSearchAddr(website.getSearchAddr());
			w.setSign(website.getSign());
			w.setUseSearch(website.isUseSearch());
			w.setVersionId(BasicUtils.id2Version(website.getVersionId()));
			w.setWebsiteId(BasicUtils.id2Version(website.getId()));
			return w;
		}
		return null;
	}

	public VersionMainDTO getVersionMainDTOInfo(String websiteId) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(websiteId)) {
			int id = BasicUtils.version2Id(websiteId);
			if (id > 0) {
				VersionBO2 v = versionMapper.queryVersionsForAdminSupportService01SimpleVerionBO2(id);
				return createVersionMainDTO(v);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private VersionMainDTO createVersionMainDTO(VersionBO2 v) {
		if (v != null) {
			VersionMainDTO d = new VersionMainDTO();
			d.setDefCss(v.getDefCss());
			d.setDefJs(v.getDefJs());
			d.setDefPage(v.getDefPage());
			d.setId(v.getId());
			d.setName(v.getName());
			d.setSign(v.getSign());
			d.setVersionId(BasicUtils.id2Version(v.getId()));
			return d;
		}
		return null;
	}

	public List<WebsitePaDTO> getWebsitePaListList(String startTime, String endTime, String type, String value)
			throws PageArgumentsException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date sd = null;
		if (StringUtils.isNotEmpty(startTime)) {
			try {
				sd = sdf.parse(startTime);
			} catch (ParseException e) {
			}
		}
		Date ed = null;
		if (StringUtils.isNotEmpty(endTime)) {
			try {
				// 因为前端的问题，需要endTime+1天包括今天
				ed = sdf.parse(endTime);
				if (ed != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(ed);
					c.add(Calendar.DAY_OF_MONTH, 1);// 天数+1
					ed = c.getTime();
				}
			} catch (ParseException e) {
			}
		}
		if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(value)) {
			// 时间是包括startTime这一天 包括endTime这一天
			if ("website".equals(type)) {// 现只有两种方式.
				List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie02ListPullArticle(value,
						sd, ed);
				return createWebsitePalistDTO(list);
			} else if ("url".equals(type)) {
				List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie03ListPullArticle(value,
						sd, ed);
				return createWebsitePalistDTO(list);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	public List<WebsitePaDTO> getWebsitesPaList(Integer page) {
		// 获取默认站点segmentfault的数据。前20条
		if (page == null) {
			page = 1;
		}
		int size = 20;// 默认20
		int offset = (page - 1) * 20;
		List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie04ListPullArticle(offset, size);
		return createWebsitePalistDTO(list);
	}

	public List<WebsitePaDTO> getWebsitePaListList(String type, String value) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(value)) {
			if ("uuid".equals(type)) {
				List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie05ListPullArticle(value);
				return createWebsitePalistDTO(list);
			} else if ("url".equals(type)) {
				List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie06ListPullArticle(value);
				return createWebsitePalistDTO(list);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	public List<WebsiteDetailDTO> getWebsiteDetailDTODefaultList() {
		List<WebsiteBO3> webs = websiteMapper.queryWebsitesForAdminSupportService01ListWebsiteBO3();
		if (webs != null && webs.size() > 0) {
			List<WebsiteDetailDTO> list = new ArrayList<WebsiteDetailDTO>(webs.size());
			for (WebsiteBO3 w : webs) {
				if (w != null) {
					WebsiteDetailDTO d = createWebsiteDetailDTO(w);
					list.add(d);
				}
			}
			return list;
		}
		return null;
	}

	private WebsiteDetailDTO createWebsiteDetailDTO(WebsiteBO3 website) {
		if (website != null) {
			WebsiteDetailDTO w = new WebsiteDetailDTO();
			w.setDefaultPageCss(website.getDefaultPageCss());
			w.setDefPageConfig(website.getDefPageConfig());
			w.setDefPageCss(website.getDefaultPageCss());
			w.setDefRequestHeader(website.getDefRequestHeader());
			w.setDefResultConfig(website.getDefResultConfig());
			w.setId(website.getId());
			w.setName(website.getName());
			w.setPagePipeline(website.getPagePipeline());
			w.setPageProcessor(website.getPageProcessor());
			w.setPageRObject(website.getPageRObject());
			w.setPagination(website.getPagination());
			w.setResultPipeline(website.getResultPipeline());
			w.setResultProcessor(website.getResultProcessor());
			w.setResultRObject(website.getResultRObject());
			w.setSearchAddr(website.getSearchAddr());
			w.setSign(website.getSign());
			w.setUseSearch(website.isUseSearch());
			w.setVersionId(BasicUtils.id2Version(website.getVersionId()));
			w.setWebsiteId(BasicUtils.id2Version(website.getId()));
			// extra
			w.setCreateMan(website.getCreateMan());
			w.setUpdateMan(website.getUpdateMan());
			if (website.getCreateTime() != null) {
				w.setCreateTime(sdf.format(website.getCreateTime()));
			}
			if (website.getUpdateTime() != null) {
				w.setUpdateTime(sdf.format(website.getUpdateTime()));
			}
			w.setNowLink(website.getNowLink());
			w.setUsing(website.getUsing() > 0 ? true : false);
			w.setNowNumber(website.getNowNumber());
			return w;
		}
		return null;
	}

	public List<WebsiteDetailDTO> getWebsisteMainBySearch(String type, String value) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(type) && StringUtils.isNotEmpty(value)) {
			if ("website".equals(type)) {
				List<WebsiteBO3> websites = websiteMapper.queryWebsitesForAdminSupportService02ListWebsiteBO3(value);
				return createWebsiteDetailDTOList(websites);
			} else if ("websiteId".equals(type)) {
				int id = BasicUtils.version2Id(value);
				if (id > 0) {
					List<WebsiteBO3> websites = websiteMapper.queryWebsitesForAdminSupportService03ListWebsiteBO3(id);
					return createWebsiteDetailDTOList(websites);
				} else {
					throw new PageArgumentsException();
				}
			} else if ("versionId".equals(type)) {
				int id = BasicUtils.version2Id(value);
				if (id > 0) {
					List<WebsiteBO3> websites = websiteMapper.queryWebsitesForAdminSupportService04ListWebsiteBO3(id);
					return createWebsiteDetailDTOList(websites);
				} else {
					throw new PageArgumentsException();
				}
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private List<WebsiteDetailDTO> createWebsiteDetailDTOList(List<WebsiteBO3> websites) {
		if (websites != null && websites.size() > 0) {
			List<WebsiteDetailDTO> list = new ArrayList<WebsiteDetailDTO>(websites.size());
			for (WebsiteBO3 w : websites) {
				if (w != null) {
					WebsiteDetailDTO d = createWebsiteDetailDTO(w);
					if (d != null) {
						list.add(d);
					}
				}
			}
			return list;
		}
		return null;
	}

	public WebsiteDetailDTO2 getWebsiteDetailInfo(String websiteId) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(websiteId)) {
			int id = BasicUtils.version2Id(websiteId);
			if (id > 0) {
				WebsiteBO3 website = websiteMapper.queryWebsitesForAdminSupportService01SimpleWebsiteBO3(id);
				return createWebsiteDetailDTO2(website);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private WebsiteDetailDTO2 createWebsiteDetailDTO2(WebsiteBO3 website) {
		if (website != null) {
			WebsiteDetailDTO2 w = new WebsiteDetailDTO2();
			w.setDefaultPageCss(website.getDefaultPageCss());
			w.setDefPageConfig(website.getDefPageConfig());
			w.setDefPageCss(website.getDefaultPageCss());
			w.setDefRequestHeader(website.getDefRequestHeader());
			w.setDefResultConfig(website.getDefResultConfig());
			w.setId(website.getId());
			w.setName(website.getName());
			w.setPagePipeline(website.getPagePipeline());
			w.setPageProcessor(website.getPageProcessor());
			w.setPageRObject(website.getPageRObject());
			w.setPagination(website.getPagination());
			w.setResultPipeline(website.getResultPipeline());
			w.setResultProcessor(website.getResultProcessor());
			w.setResultRObject(website.getResultRObject());
			w.setSearchAddr(website.getSearchAddr());
			w.setSign(website.getSign());
			w.setUseSearch(website.isUseSearch());
			w.setVersionId(BasicUtils.id2Version(website.getVersionId()));
			w.setWebsiteId(BasicUtils.id2Version(website.getId()));
			// extra
			w.setCreateMan(website.getCreateMan());
			w.setUpdateMan(website.getUpdateMan());
			if (website.getCreateTime() != null) {
				w.setCreateTime(sdf.format(website.getCreateTime()));
			}
			if (website.getUpdateTime() != null) {
				w.setUpdateTime(sdf.format(website.getUpdateTime()));
			}
			w.setNowLink(website.getNowLink());
			w.setUsing(website.getUsing() > 0 ? true : false);
			w.setNowNumber(website.getNowNumber());
			if (!w.isUsing()) {
				if (website.getUnuseTime() != null) {
					w.setUnuseTime(sdf.format(website.getUnuseTime()));
				}
			}
			w.setUnuseMan(website.getUnuseMan());
			w.setAlias(website.getAlias());
			w.setShortAddr(website.getShortAddr());
			w.setFullAddr(website.getFullAddr());
			return w;
		}
		return null;
	}

	public List<String> getWebsiteRelyVersionIdList(String websiteId) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(websiteId)) {
			int id = BasicUtils.version2Id(websiteId);
			if (id > 0) {
				List<Integer> list = versionMapper.selectVersionsForAdminInforSupportService01ListString(id);
				return createVersionIdFromListInteger(list);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private List<String> createVersionIdFromListInteger(List<Integer> list) {
		if (list != null && list.size() > 0) {
			List<String> ls = new ArrayList<String>(list.size());
			for (Integer s : list) {
				if (s != null && s > 0) {
					ls.add(BasicUtils.id2Version(s));
				}
			}
			return ls;
		}
		return null;
	}

	public List<String> getWebsiteNameList() {
		// 获得站点名称列表
		return versionMapper.selectVersionsForAdminInfoSupportService01ListString();
	}

	public List<String> getWebsiteTypeList() {
		// 获得站点类型列表
		return null;
	}

	public List<String> getWebsiteAllVersionList(String name) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(name)) {
			List<Integer> list = versionMapper.selectVersionsForAdminInfoSupportService02ListInteger(name);
			return createVersionIdFromListInteger(list);
		} else {
			throw new PageArgumentsException();
		}
	}

	public String getWebsiteDefaultVersionId(String name) throws PageArgumentsException {
		if (StringUtils.isNotEmpty(name)) {
			Integer id = versionMapper.selectVersionsForAdminInfoSupportService02SimpleInteger(name);
			if (id != null && id > 0) {
				return BasicUtils.id2Version(id);
			} else {
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	public List<ServiceSettingsDTO> getWebsiteServiceByItemsIdAndName(String websiteId, String name)
			throws PageArgumentsException {
		if (StringUtils.isNotEmpty(websiteId) && StringUtils.isNotEmpty(name)) {
			// 通过字符串模糊查询。如果有数据，则取出字符串。
			int id = BasicUtils.version2Id(websiteId);
			if (id > 0) {
				Website w = websiteMapper.queryWebsitesForAdminInfoSupportService01SimpleWebsite(id, name);
				if (w != null) {
					String sql = createSqlForWebsite(w);
					List<ConfigBO> configs = configMapper.queryConfigsForAdminInfoSupportService03ListConfigBO(sql);
					if (configs != null && configs.size() > 0) {
						return createServiceSettingsDTOList(configs);
					} else {
						return null;
					}
				} else {
					throw new PageArgumentsException("参数异常，数据无该记录...");
				}
			}else{
				throw new PageArgumentsException();
			}
		} else {
			throw new PageArgumentsException();
		}
	}

	private String createSqlForWebsiteItem(PullArticleBO p) {
		// 强制依赖，p肯定不为空
		ConfigWebsiteItemModel config = new ConfigWebsiteItemModel();
		config.setId(p.getId());
		config.setName(p.getName());
		config.setType(p.getType());
		config.setUrl(p.getUrl());
		config.setUuid(p.getUuid());
		return JSON.toJSONString(config);
	}
}
