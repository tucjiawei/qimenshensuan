package hello;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jiawei on 17/8/21.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterTalk {
    private long id;
    private String title = "";
    private String content = "";
    private String time;
}
