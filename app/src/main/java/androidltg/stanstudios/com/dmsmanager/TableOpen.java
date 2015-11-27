package androidltg.stanstudios.com.dmsmanager;

import android.graphics.Bitmap;

/**
 * Created by DMSv4 on 9/8/2015.
 */
public class TableOpen  {
private String title,Area,No_,titlearea;
private String startdate;
private String prices;
private int status,stt;

        public TableOpen() {

        }

        public TableOpen( String title,String titlearea,String No_,String Area, String startdate, String prices, int status,int stt) {
            super();

            this.title = title;
            this.No_ = No_;
            this.Area =Area;
            this.startdate = startdate;
            this.prices = prices;
            this.titlearea = titlearea;

            this.status = status;
            this.stt =stt;
        }

    public String gettitlearea() {
        return titlearea;
    }

    public void settitlearea(String titlearea) {
        this.titlearea = titlearea;
    }

        public String getArea() {
            return Area;
        }

        public void setArea(String Area) {
            this.Area = Area;
        }
        public String getTitle() {
            return title;
        }

        public void setNo_(String No_) {
            this.No_ = No_;
        }
        public String getNo_() {
            return No_;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getstartdate() {
            return startdate;
        }

        public void setstartdate(String startdate) {
            this.startdate = startdate;
        }
        public String getprices() {
            return prices;
        }

        public void setprices(String prices) {
            this.prices = prices;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    public int getstt() {
        return stt;
    }

    public void setstt(int stt) {
        this.stt = stt;
    }


}
