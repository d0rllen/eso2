package com.zhidian.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhidian.bases.SearchEngineEnumDefine;
import com.zhidian.exception.PageArgumentsException;
import com.zhidian.mapper.ConfigMapper;
import com.zhidian.mapper.PullArticleMapper;
import com.zhidian.mapper.VersionMapper;
import com.zhidian.model.PullArticle;
import com.zhidian.model.Version;
import com.zhidian.model.sys.ConfigBO;
import com.zhidian.model.sys.NameValueModel;
import com.zhidian.util.BasicUtils;
import com.zhidian.views.ConfigDTO;
import com.zhidian.views.VersionAddVO;
import com.zhidian.views.VersionControlVO;
import com.zhidian.views.VersionControlViewDTO;
import com.zhidian.views.VersionUpdateVO;
import com.zhidian.views.WebsitePalistDTO;

@Service
public class AdminInfoSupportService {

	@Autowired
	ConfigMapper configMapper;

	@Autowired
	VersionMapper versionMapper;
	
	@Autowired
	PullArticleMapper pullMapper;

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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			for (Version v : list) {
				if (v != null) {
					VersionControlViewDTO d = new VersionControlViewDTO();
					d.setId(v.getId());
					d.setName(v.getName());
					d.setUsing(v.getUsing()>0?true:false);
					d.setNmp(v.getNmp()>0?true:false);
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

	public List<VersionControlVO> getVersionInfoUsingList() {
		List<Version> list = versionMapper.queryVersionsForAdminInfoSupportService03ListVersion();
		return createVersionControlVO(list);
	}

	private List<VersionControlVO> createVersionControlVO(List<Version> list) {
		if (list != null && list.size() > 0) {
			List<VersionControlVO> vos = new ArrayList<VersionControlVO>(list.size());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			for (Version v : list) {
				if (v != null) {
					VersionControlVO vo = new VersionControlVO();
					if (v.getCreateTime() != null) {
						vo.setCreateTime(sdf.format(v.getCreateTime()));
					}
					vo.setName(vo.getName());
					vo.setUsing(v.getUsing()>0?true:false);
					vo.setId(vo.getId());
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

	public List<WebsitePalistDTO> getWebsitesPaList() {
		// 获取每个站点特有的一条正使用的数据，现获取最近记录入库的
		List<PullArticle> list = pullMapper.queryPullArticlesForAdminInfoSupportServcie01ListPullArticle();
		return createWebsitePalistDTO(list);
	}

	private List<WebsitePalistDTO> createWebsitePalistDTO(List<PullArticle> list) {
		if(list!=null&&list.size()>0){
			List<WebsitePalistDTO> dtos = new ArrayList<WebsitePalistDTO>(list.size());
			for(PullArticle p : list){
				if(p!=null){
					WebsitePalistDTO d = new WebsitePalistDTO();
					// 装载数据
					
					dtos.add(d);
				}
			}
			return dtos;
		}
		return null;
	}
}
