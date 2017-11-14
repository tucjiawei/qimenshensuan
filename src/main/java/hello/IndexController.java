package hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    private Init init;
    private static final int PAGE_SIZE = 10;

    @RequestMapping("/index.html")
    public String greeting(@RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(required = false, defaultValue = "HUNYIN") ServiceType serviceType,
                           Model model) {
        model.addAttribute("masters", init.getMasters().subList((page - 1) * PAGE_SIZE, Math.min(page * PAGE_SIZE, init.getMasters().size())));

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", init.getMasters().size() / PAGE_SIZE + 1);
        int firstPage = 1, lastPage = init.getMasters().size() / PAGE_SIZE + 1;

        firstPage = page - 2 >= 1 ? page - 2 : firstPage;
        lastPage = firstPage + 4 >= lastPage ? lastPage : firstPage + 4;
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("serviceType", serviceType);
        return "index";
    }

    @RequestMapping(value = {"/hunyin/index.html", "/shiye/index.html", "/jixiong/index.html", "/caiyun/index.html"})
    public String index(@RequestParam(defaultValue = "1") Integer page,
                        HttpServletRequest request,
                        Model model) {
        String serviceTypeStr = request.getRequestURI().split("/")[1];
        ServiceType serviceType = ServiceType.valueOf(serviceTypeStr.toUpperCase());
        model.addAttribute("masters", init.getMasters().subList((page - 1) * PAGE_SIZE, Math.min(page * PAGE_SIZE, init.getMasters().size())));

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", init.getMasters().size() / PAGE_SIZE + 1);
        int firstPage = 1, lastPage = init.getMasters().size() / PAGE_SIZE + 1;

        firstPage = page - 2 >= 1 ? page - 2 : firstPage;
        lastPage = firstPage + 4 >= lastPage ? lastPage : firstPage + 4;
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("serviceType", serviceType);
        return "index";
    }
}
