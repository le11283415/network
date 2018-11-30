package babytree.com.myprojecttest.net;

import babytree.com.myprojecttest.bean.Blog;
import babytree.com.myprojecttest.bean.DateListBean;
import babytree.com.myprojecttest.bean.NewsBean;
import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by kangle on 2018/10/30.
 */
public interface Inetwork {


    @POST("/blog")
    Observable<Blog> getBlogs();

    @POST("http://co-api.51wnl.com/Calendar/FestivalInfo?name=%E8%BE%9B%E4%BA%A5%E9%9D%A9%E5%91%BD%E7%BA%AA%E5%BF%B5%E6%97%A5&client=ceshi&simplified=0&token=A79D963960F24B84424723A2258BD703")
    Observable<NewsBean> getNews();

    @POST("http://co-api.51wnl.com/calendar/detailsyear?token=A29341D2D2851894A1AA8A2FB7A91962&year=2016&timestamp=1462377600&client=ceshi")
    Observable<DateListBean> getDateList();

}
