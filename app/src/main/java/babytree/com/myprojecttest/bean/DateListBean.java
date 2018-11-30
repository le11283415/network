package babytree.com.myprojecttest.bean;

/**
 * Created by kangle on 2018/11/10.
 */
public class DateListBean  {

   public Object data;

    public static class DateBean{
        public String datekey;
        public String festivals;
        public String yi;
        public String pzbj;
        public String constellation;
        public String url;

        @Override
        public String toString() {
            return "DateBean{" +
                    "datekey='" + datekey + '\'' +
                    ", festivals='" + festivals + '\'' +
                    ", yi='" + yi + '\'' +
                    ", pzbj='" + pzbj + '\'' +
                    ", constellation='" + constellation + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
