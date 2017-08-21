package hello;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jiawei on 17/8/1.
 */
@Data
@AllArgsConstructor
public class Project {
    private ServiceType serviceType;
    private int price;
    private String desc;
}
