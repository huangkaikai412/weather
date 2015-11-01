package pku.ss.kitty.bean;

/**
 * Created by HuangKaiKai on 2015/10/20.
 */
public class City {

    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFirstPY;

    public City(String province, String city, String number,
                String firstPY, String allPY, String allFirstPY) {
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFirstPY = allFirstPY;
    }

    public void setProvince(String str) {
        this.province = str;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public void setNumber(String str) {
        this.number = str;
    }

    public void setFirstPY(String str) {
        this.firstPY = str;
    }

    public void setAllPY(String str) {
        this.allPY = str;
    }

    public void setAllFirstPY(String str) {
        this.allFirstPY = str;
    }

    public String getProvince() {
        return this.province;
    }

    public String getCity() {
        return this.city;
    }

    public String getNumber() {
        return this.number;
    }

    public String getFirstPY() {
        return this.firstPY;
    }

    public String getAllPY() {
        return this.allPY;
    }

    public String getAllFirstPY() {
        return this.allFirstPY;
    }

}
