package hello;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jiawei on 17/8/21.
 */
@Controller
@Slf4j
public class TalkController {
    @Autowired
    private Init init;
    private final static int PAGE_SIZE = 20;

    @RequestMapping("/talk")
    public String talk(@RequestParam(required = false, defaultValue = "1") int page, Model model) {
        Map<String, MasterTalk> talkMap = init.getTalkMap();
        Set<String> talkKeys = talkMap.keySet();
        List<String> talkDocs = new ArrayList<>(talkKeys);
        talkDocs.sort((o1, o2) -> o2.compareTo(o1));
        talkDocs = talkDocs.subList((page - 1) * PAGE_SIZE, Math.min(page * PAGE_SIZE, talkDocs.size()));
        List<MasterTalk> res = talkDocs.stream().map(talkMap::get).collect(Collectors.toList());

        model.addAttribute("docs", res);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", talkMap.size() / PAGE_SIZE + 1);
        int firstPage = 1, lastPage = talkDocs.size() / PAGE_SIZE + 1;

        firstPage = page - 2 >= 1 ? page - 2 : firstPage;
        lastPage = firstPage + 4 >= lastPage ? lastPage : firstPage + 4;
        model.addAttribute("firstPage", firstPage);
        model.addAttribute("lastPage", lastPage);
        return "talk";
    }

    @RequestMapping("/talk/{id}")
    public String talkDetail(@PathVariable long id, Model model) {
        Map<String, MasterTalk> talkMap = init.getTalkMap();
        for (MasterTalk talk : talkMap.values()) {
            if (talk.getId() == id) {
                model.addAttribute("doc", talk);
                return "talk-detail";
            }
        }
        return "404";
    }
}
