package hello;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jiawei on 17/8/1.
 */
@Data
@AllArgsConstructor
public class Service {
    private String title;
    private String content;
    private String buy;
}
