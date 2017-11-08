package hello;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jiawei on 17/11/8.
 */
@Data
@AllArgsConstructor
public class Comment {
    private String author;
    private String content;
    private String time;
}
