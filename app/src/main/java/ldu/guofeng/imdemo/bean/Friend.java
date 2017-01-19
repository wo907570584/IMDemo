package ldu.guofeng.imdemo.bean;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * 好友
 *
 * @author GUOFENG
 */

public class Friend extends BaseIndexPinyinBean {

    private String name;//好友名字,此列表只用name
    private boolean isTop;//是否是最上面的 不需要被转化成拼音的

    public String getName() {
        return name;
    }

    public Friend setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isTop() {
        return isTop;
    }

    public Friend setTop(boolean top) {
        isTop = top;
        return this;
    }

    @Override
    public String getTarget() {
        return name;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }

    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
