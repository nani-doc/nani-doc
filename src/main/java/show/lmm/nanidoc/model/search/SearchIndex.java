package show.lmm.nanidoc.model.search;

import lombok.Getter;

/**
 * 搜索索引首页
 *
 * @author 刘明明
 * @date 2022-01-30 11:53
 */
public class SearchIndex {

    public SearchIndex(int totalCount, int pageSize) {
        this.totalPage = (totalCount + pageSize - 1) / pageSize;
    }

    /**
     * 索引总页数
     */
    @Getter
    private int totalPage;
}
