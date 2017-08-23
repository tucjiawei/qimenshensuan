package sitemap;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by jiawei on 17/8/22.
 */
@Data
@AllArgsConstructor
public class SiteUrl {
    private String loc;
    private String changefreq;
    private String priority;
    private String lastmod;
}
