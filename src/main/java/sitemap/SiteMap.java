package sitemap;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiawei on 17/8/22.
 */
@Data
@XmlRootElement(name = "urlset")
public class SiteMap {
    private List<SiteUrl> url = new ArrayList<>();
}
