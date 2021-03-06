package hello;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import sitemap.JaxbUtils;
import sitemap.SiteMap;
import sitemap.SiteUrl;

import java.io.*;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jiawei on 17/8/1.
 */
@Data
@Slf4j
@Component
public class Init implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${talk.dir}")
    private String talkDir;
    @Value("${sitemap.dir}")
    private String siteMapDir;
    private List<Master> masters = new ArrayList<>();
    private Map<String, MasterTalk> talkMap = new TreeMap<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("init service");
        log.info("talk dir is {}", talkDir);

        try {
            Project project1 = new Project(ServiceType.HUNYIN, 100, "desc");
            Project project2 = new Project(ServiceType.SHIYE, 100, "desc");
            Project project3 = new Project(ServiceType.CAIYUN, 100, "desc");
            Project project4 = new Project(ServiceType.JIXIONG, 100, "desc");
            Project project5 = new Project(ServiceType.OTHER, 100, "desc");
            List<Project> projects = Arrays.asList(project1, project2, project3, project4, project5);

            BufferedReader services = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("services.txt")));
            String str;
            List<Service> serviceList = new ArrayList<>();
            while ((str = services.readLine()) != null) {
                String[] service = str.split("\\|");
                serviceList.add(new Service(service[0], service[1], service[2]));
            }

            //大师列表
            BufferedReader bis = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("masters")));
            String masterStr = "";
            while ((str = bis.readLine()) != null) {
                masterStr += str;
            }

            TypeToken typeToken = new TypeToken<List<MasterJsonVO>>() {
            };
            Type type = typeToken.getType();
            List<MasterJsonVO> users = new Gson().fromJson(masterStr, type);

            //评论
            BufferedReader comments = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("comments")));
            String commentStr = "";
            while ((str = comments.readLine()) != null) {
                commentStr += str;
            }
            List<MasterJsonVO> usersComments = new Gson().fromJson(commentStr, type);

            int id = 1;
            for (MasterJsonVO user : users) {
                int totalNum = 0;
                if (!user.getTotalCommentNum().equals("暂无数据")) {
                    totalNum = Integer.valueOf(user.getTotalCommentNum());
                }
                List<Comment> commentList = Lists.transform(usersComments.get(id - 1).getComments(), commentJsonVO -> new Comment(commentJsonVO.getAuthor(), commentJsonVO.getContent(), commentJsonVO.getTime()));
                String[] imgPath = user.getImg().split("/");
                String imgName = imgPath[imgPath.length - 1];
                Master master = new Master(id++, user.getTitle(), "/img/" + imgName, user.getDesc(), totalNum, totalNum, user.getGoodComment(), projects, commentList, serviceList);
                masters.add(master);
            }
//            masters.get(0).setPic("img/184830_head.jpg");


            File f = new File(talkDir);
            if (!f.exists() || !f.isDirectory()) {
                log.warn("talk dir is error");
                return;
            }

            id = 1;
            for (File file : f.listFiles()) {
                boolean isFirstLine = true;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                MasterTalk masterTalk = new MasterTalk();
                masterTalk.setId(id++);
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        String[] lineStr = line.split("\\|");
                        masterTalk.setTitle(lineStr[0]);
                        masterTalk.setAuthor(lineStr[1]);
                        masterTalk.setTime(lineStr[2]);
                        isFirstLine = false;
                    } else {
                        masterTalk.setContent(masterTalk.getContent() + line + "\n");
                    }
                }
                talkMap.put(file.getName(), masterTalk);
            }
            SiteMap siteMap = new SiteMap();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(new Date());
            for (int i = 1; i <= 32; i++) {
                SiteUrl url = new SiteUrl("http://www.qimenshensuan.com/index.html?page=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
                url = new SiteUrl("http://www.qimenshensuan.com/index.html?page=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
                url = new SiteUrl("http://www.qimenshensuan.com/caiyun/index.html?page=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
                url = new SiteUrl("http://www.qimenshensuan.com/hunyin/index.html?page=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
                url = new SiteUrl("http://www.qimenshensuan.com/jixiong/index.html?page=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
                url = new SiteUrl("http://www.qimenshensuan.com/shiye/index.html?page=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
            }
            for (int i = 1; i <= 240; i++) {
                SiteUrl url = new SiteUrl("http://www.qimenshensuan.com/dashi/detail?id=" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
            }
            SiteUrl url = new SiteUrl("http://www.qimenshensuan.com/suanmingzatan/", "daily", "1.0", now);
            siteMap.getUrl().add(url);
            for (int i = 1; i <= f.listFiles().length; i++) {
                url = new SiteUrl("http://www.qimenshensuan.com/suanmingzatan/" + i, "daily", "1.0", now);
                siteMap.getUrl().add(url);
            }
            String siteMapXml = JaxbUtils.convertToXml(siteMap);

            File siteMapFile = new File(siteMapDir + "/sitemap.xml");
            if (siteMapFile.exists()) {
                siteMapFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(siteMapFile);
            fileWriter.write(siteMapXml);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            log.error("error", e);
        }
    }
}