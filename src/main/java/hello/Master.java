package hello;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Created by jiawei on 17/8/1.
 */
@Data
@AllArgsConstructor
public class Master {
    private long id;
    private String name;
    private String pic;
    private String desc;
    private int rateNum;
    private int score;
    private double rate;
    private List<Project> projectList;
}
