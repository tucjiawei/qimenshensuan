package hello;

import lombok.Data;

import java.util.List;

/**
 * Created by jiawei on 17/8/3.
 */
@Data
public class MasterJsonVO {
    private String img;
    private String desc;
    private double goodComment;
    private String lastestComment;
    private String title;
    private String totalCommentNum;
    private List<CommentJsonVO> comments;
}
