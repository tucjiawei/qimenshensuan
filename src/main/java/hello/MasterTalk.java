package hello;

import lombok.Data;

/**
 * Created by jiawei on 17/8/21.
 */
@Data
public class MasterTalk {
    private long id;
    private String title = "";
    private String content = "";
    private String time;
}
