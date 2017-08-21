package hello;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by jiawei on 17/8/1.
 */
@Data
@Slf4j
@Component
public class Init implements ApplicationListener<ApplicationReadyEvent> {

    private List<Master> masters = new ArrayList<>();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("init service");

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
            Random r = new Random();
            for (MasterJsonVO user : users) {
                Master master = new Master(r.nextInt(), user.getTitle(), "img/8285_head.png", user.getDesc(), 24323, 24323, user.getGoodComment(), projects);
                masters.add(master);
            }
            masters.get(0).setPic("img/184830_head.jpg");
        } catch (IOException e) {
            log.error("error", e);
        }
    }
}