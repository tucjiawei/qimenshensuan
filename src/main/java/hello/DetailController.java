package hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;

@Controller
@Slf4j
public class DetailController {

    @Autowired
    private Init init;

    @RequestMapping("/dashi/detail")
    public String greeting(long id, @RequestParam(required = false, defaultValue = "PROFILE") DetailType type, Model model) {
        for (Master master : init.getMasters()) {
            if (master.getId() == id) {
                String res = "detail";
                model.addAttribute("master", master);
                if (type == DetailType.SCORE) {
                    model.addAttribute("content", master.getComments());
                    res = "score";

                } else {
                    model.addAttribute("content", master.getServices());
                }
                model.addAttribute("type", type.toString());
                log.info(master.toString());
                return res;
            }
        }
        return "404";
    }

}
