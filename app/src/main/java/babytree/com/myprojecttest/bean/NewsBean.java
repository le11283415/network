package babytree.com.myprojecttest.bean;

/**
 * Created by kangle on 2018/11/10.
 */
public class NewsBean  {

    public String id;
    public String Name;
    public String  Descption;
    public String BgImghttps;
    public String MoreTxt;
    public String MoreUrlhttp;
    public String ShareImghttps;

    @Override
    public String toString() {
        return "NewsBean{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", Descption='" + Descption + '\'' +
                ", BgImghttps='" + BgImghttps + '\'' +
                ", MoreTxt='" + MoreTxt + '\'' +
                ", MoreUrlhttp='" + MoreUrlhttp + '\'' +
                ", ShareImghttps='" + ShareImghttps + '\'' +
                '}';
    }
}
