package hello;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Type;
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
    private List<Master> masters = new ArrayList<>();
    private Map<String, MasterTalk> talkMap = new TreeMap<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("init service");
        log.info("talk dir is {}", talkDir);

        try {
            Project project1 = new Project(ServiceType.MARRIAGE, 100, "desc");
            Project project2 = new Project(ServiceType.AUSPICE, 100, "desc");
            Project project3 = new Project(ServiceType.AUSPICE, 100, "desc");
            Project project4 = new Project(ServiceType.AUSPICE, 100, "desc");
            Project project5 = new Project(ServiceType.OTHER, 100, "desc");
            List<Project> projects = Arrays.asList(project1, project2, project3, project4, project5);

            BufferedReader bis = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("masters")));
            String masterStr = "", str;
            while ((str = bis.readLine()) != null) {
                masterStr += str;
            }

            TypeToken typeToken = new TypeToken<List<MasterJsonVO>>() {
            };
            Type type = typeToken.getType();
            List<MasterJsonVO> users = new Gson().fromJson(masterStr, type);
            int id = 1;
            for (MasterJsonVO user : users) {
                Master master = new Master(id++, user.getTitle(), "img/8285_head.png", user.getDesc(), 24323, 24323, user.getGoodComment(), projects);
                masters.add(master);
            }
            masters.get(0).setPic("img/184830_head.jpg");

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
                        masterTalk.setTime(lineStr[1]);
                        isFirstLine = false;
                    } else {
                        masterTalk.setContent(masterTalk.getContent() + line + "\n");
                    }
                }
                talkMap.put(file.getName(), masterTalk);
            }
        } catch (IOException e) {
            log.error("error", e);
        }
    }
}