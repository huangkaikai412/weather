package pku.ss.kitty.bean;

/**
 * Created by HuangKaiKai on 2015/10/19.
 */
public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;
    private String fengli;
    private String date;
    private String high;
    private String low;
    private String type;

    public void setCity(String str) {
        this.city = str;
    }

    public void setUpdatetime(String str) {
        this.updatetime = str;
    }

    public void setWendu(String str) {
        this.wendu = str;
    }

    public void setShidu(String str) {
        this.shidu = str;
    }

    public void setPm25(String str) {
        this.pm25 = str;
    }

    public void setQuality(String str) {
        this.quality = str;
    }

    public void setFengxiang(String str) {
        this.fengxiang = str;
    }

    public void setFengli(String str) {
        this.fengli = str;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public void setHigh(String str) {
        this.high = str.substring(str.indexOf('温')+1,str.length());
    }

    public void setLow(String str) {
        this.low = str.substring(str.indexOf('温')+1,str.length());
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getCity() {
        return city;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public String getWendu() {
        return wendu;
    }

    public String getShidu() {
        return shidu;
    }

    public String getPm25() {
        return pm25;
    }

    public String getQuality() {
        return quality;
    }

    public String getFengxiang() {
        return fengxiang;
    }

    public String getFengli() {
        return fengli;
    }

    public String getDate() {
        return date;
    }

    public String getHigh() {
        return high;
    }

    public String getLow() {
        return low;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city=" + city + '\'' +
                "updatetime=" + updatetime + '\'' +
                "wendu=" + wendu + '\'' +
                "shidu=" + shidu + '\'' +
                "pm2.5=" + pm25 + '\'' +
                "quality=" + quality + '\'' +
                "fengxiang=" + fengxiang + '\'' +
                "fengli=" + fengli + '\'' +
                "date=" + date + '\'' +
                "high=" + high + '\'' +
                "low=" + low + '\'' +
                "type=" + type + "}";
    }
}

