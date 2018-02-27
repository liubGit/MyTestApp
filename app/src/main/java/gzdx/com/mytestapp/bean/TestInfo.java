package gzdx.com.mytestapp.bean;

import android.support.annotation.Nullable;

/**
 * Created by liub on 2018/1/18.
 */

public class TestInfo {
    private String name;
    private String age;

    public TestInfo() {
        this.name = "张三";
        this.age = "18";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }




}
