
/**
* @Title: AppTest.java
* @Package com.zhidian.test
* @Description: TODO(用一句话描述该文件做什么)
* @author dongneng
* @date 2017-3-19 上午1:51:13
* @version V1.0
*/
package com.zhidian.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhidian.bases.AppEnumDefine;
import com.zhidian.bases.ResourceEnumDefine;
import com.zhidian.bases.SearchEngineEnumDefine;
import com.zhidian.bases.worm.WormsService;
import com.zhidian.mapper.GlobalInfoMapper;
import com.zhidian.mapper.PullArticleMapper;
import com.zhidian.mapper.ResultMapper;
import com.zhidian.mapper.ScheduleQueueMapper;
import com.zhidian.mapper.UserMapper;
import com.zhidian.mapper.VersionMapper;
import com.zhidian.mapper.WebsiteMapper;
import com.zhidian.mapper.WormLogMapper;
import com.zhidian.model.PullArticle;
import com.zhidian.model.Result;
import com.zhidian.model.ScheduleQueue;
import com.zhidian.model.User;
import com.zhidian.model.Version;
import com.zhidian.model.WormLog;
import com.zhidian.model.sys.PullResultBO;
import com.zhidian.model.sys.PullResultPageModel;
import com.zhidian.model.sys.ResultRoleBO;
import com.zhidian.model.sys.WebsiteBO;

/**
 * @ClassName: AppTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author dongneng
 * @date 2017-3-19 上午1:51:13
 *
 */
@RunWith(SpringJUnit4ClassRunner.class) // 使用junit4进行测试
//@WebAppConfiguration //http://blog.zenika.com/2013/01/21/using-thymeleaf-with-spring-mvc/
@ContextConfiguration({ "classpath*:SpringContext.xml" })
public class AppTest {
	// static ApplicationContext app;
	// static UserMapper userMapper;
	// static ArticleMapper articleMapper;

	// static {
	// app = new
	// ClassPathXmlApplicationContext("classpath:com/zhidian/util/SpringContext.xml");
	// if (app != null) {
	// userMapper = app.getBean(UserMapper.class);
	// articleMapper = app.getBean(ArticleMapper.class);
	// }
	// }

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	PullArticleMapper articleMapper;

	@Autowired
	ResultRoleBO resultRole;

	@Autowired
	WormsService wormsService;

	@Test
	@Transactional
	@Rollback
	public void Insert() throws Exception {
//		PullArticle a = new PullArticle();
//		a.setUuid("TTTTT");
//		a.setTags("python");
//		a.setStartTime(new Date());
//		System.out.println(a);
//		articleMapper.addArticle(a);
//		System.out.println(a);
	}

	@Test
	public void test() throws Exception {
		if (resultRole != null) {
			System.out.println(resultRole.getDescri());
			Map<String, Object> map = resultRole.getWebsites();
			// for(Map.Entry<String, Object> entry : map.entrySet()){
			// System.out.println(entry.getKey()+"_"+entry.getValue());
			// }
			System.out.println(map.get("name"));
			System.out.println(map.get("ggg"));
			int i = (Integer) map.get("ggg");
			System.out.println(i);
		}
	}

	@Test
	public void testWormService() throws Exception {
		List<String> from = new ArrayList<String>();
		from.add("segmentfault");
		List<PullResultPageModel> list = wormsService.getResultsByOnlineSearch("python", from);
		String str = JSON.toJSONString(list);
		System.out.println("results:"+str);
	}

	@Autowired
	ResultMapper resultMapper;

	@Test
	@Transactional
	@Rollback
	public void testResults() throws Exception {
		Map<String, Object> map = resultMapper.testDD(1);
		System.out.println(map);
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				System.out.println(entry.getKey() + " - " + entry.getValue());
			}
		}
	}

	@Autowired
	VersionMapper versionMapper;
	
	@Test
	@Transactional
	@Rollback
	public void testVersions() throws Exception {
		Version v = versionMapper.queryVersionsForSearchService01SimpleVersion("results", "answer");
		System.out.println(v);
	}
	
	
	@Test
	public void testBooleanValue() throws Exception{
		User u = userMapper.getUser(1);
		System.out.println(JSON.toJSONString(u));
	}
	@Test
	@Transactional
	@Rollback
	public void testInsertBooleanValue() throws Exception{
		User u = JSON.parseObject("{\"address\":\"tianjing\",\"age\":35,\"id\":2,\"name\":\"dongneng\",\"using\":true}",User.class);
		System.out.println(u);
		userMapper.addUser(u);
		System.out.println(JSON.toJSONString(u));
	}

	@Autowired
	GlobalInfoMapper globalInfoMapper;
	
	@Test
	public void testGlobalInfo() throws Exception{
		String value = globalInfoMapper.selectGlobalInfoForPageService01SimpleString("websites", "segmentfault");
		System.out.println(value);
	}
	
	@Test
	public void testVersion2() throws Exception{
		Version v = versionMapper.queryVersionsForPullArticleService01SimpleVersion("websites","segmentfault","0.0.0.0");
		System.out.println(JSON.toJSONString(v));
		
	}
	
	
	@Test
	public void testPullArticleData(){
		List<String> uuid = new ArrayList<String>();
		uuid.add("abc");
		uuid.add("abd");
		List<String> from = new ArrayList<String>();
		from.add("segmentfault");
		from.add("github");
		List<Result> re = resultMapper.queryResultsForPullArticleService02ListResult(from,uuid);	
		System.out.println(JSON.toJSONString(re));
	}
	
	@Test
	@Transactional
	@Rollback
	public void testInsertPullArticle(){
		List<PullArticle> list= new ArrayList<PullArticle>();
		PullArticle p = new PullArticle();
		p.setUuid("GGGGGGGGGG");
		list.add(p);
		p = new PullArticle();
		p.setUuid("abbbbbbb");
		list.add(p);
		articleMapper.insertArticlesForWormsService02ListPullArticle(list);
	}
	
	@Autowired
	ScheduleQueueMapper scheduleMapper;
	
	@Test
	@Transactional
	@Rollback
	public void testInsertScheduleQueue(){
		List<ScheduleQueue> list = new ArrayList<ScheduleQueue>();
		ScheduleQueue s = new ScheduleQueue();
		s.setCreateMan(AppEnumDefine.AppUser.系统.getValue());
//		s.setCreateTime(new Date());// 时间采用了数据库的入库时间，即数据库内置函数【需注意】
		s.setName("segmentfault");
		s.setType(AppEnumDefine.ScheduleQueuesType.系统自增.getValue());
		s.setType2(SearchEngineEnumDefine.Type.问答.getValue());// 默认是搜索引擎的answer类型
		s.setType3(ResourceEnumDefine.ResourceType.内容详情页.getValue());// 爬虫页面的类型
		s.setUrl("http://segmentfault.com/a/123456");
		list.add(s);
		s = new ScheduleQueue();
		s.setCreateMan(AppEnumDefine.AppUser.系统.getValue());
		s.setName("github");
		s.setType(AppEnumDefine.ScheduleQueuesType.系统自增.getValue());
		s.setType2(SearchEngineEnumDefine.Type.问答.getValue());// 默认是搜索引擎的answer类型
		s.setType3(ResourceEnumDefine.ResourceType.内容详情页.getValue());// 爬虫页面的类型
		s.setUrl("http://github.com/a/123456");
		list.add(s);
		scheduleMapper.insertScheduleQueuesForPullArticleService01SimpleVoid(list);
	}
	
	@Test
	public void testUpdateScheduleQueue(){
		List<Integer> queues = new ArrayList<Integer>();
		queues.add(7);
		scheduleMapper.updateScheduleQueuesForWormsServiceListInteger(queues);
	}
	
	@Test
	public void testQueryScheduleQueue(){
		List<PullResultBO> list = new ArrayList<PullResultBO>();
		PullResultBO p = new PullResultBO();
		p.setUrl("https://segmentfault.com/q/1010000008561228");
		p.setName("segmentfault");
		list.add(p);
		p = new PullResultBO();
		p.setUrl("https://segmentfault.com/q/1010000005915613");
		p.setName("segmentfault");
		list.add(p);
		p = new PullResultBO();
		p.setUrl("https://segmentfault.com/q/1010000006984096");
		p.setName("segmentfault");
		list.add(p);
		List<ScheduleQueue> qu = scheduleMapper.queryScheduleQueuesForPullArticleService01ListScheduleQueue(list);
		System.out.println(JSON.toJSONString(qu));
	}
	
	@Autowired
	WebsiteMapper websiteMapper;
	@Test
	public void testWebsiteMapper02(){
		List<String> names = new ArrayList<String>();
		names.add("segmentfault");
		names.add("github");
		List<WebsiteBO> list = websiteMapper.queryWebsitesForWormsService01ListWebsiteBO(AppEnumDefine.SiteService.搜索.getValue(), names);
		System.out.println(JSON.toJSONString(list));
		System.out.println(list.get(0).getCssLists());
	}
	
	@Test
	public void testWebsiteMapper03(){
		
	}
	
	@Test
	public void testPullPgaeData(){
		
	}
	
	@Autowired
	WormLogMapper wormLogMapper;
	
	@Test
	@Transactional
	@Rollback
	public void insertWormLogs(){
		List<WormLog> list = new ArrayList<WormLog>();
		WormLog w= new WormLog();
		w.setId(1);
		w.setPropertyName("hello");
		list.add(w);
		w = new WormLog();
		w.setPropertyName("nice");
		w.setId(2);
		list.add(w);
		wormLogMapper.insertWormsLogForWormsService01ListWormLog(list);
	}
	
	
	
	public static void main(String[] args) {
//		User u = JSON.parseObject("{\"address\":\"sh\",\"age\":17,\"id\":2,\"name\":\"dongneng\",\"using\":true}",User.class);
		//User u = (User) JSON.parse("{\"address\":\"sh\",\"age\":17,\"id\":2,\"name\":\"dongneng\",\"using\":true}");
//		System.out.println(u.getName());
	}
	
}
