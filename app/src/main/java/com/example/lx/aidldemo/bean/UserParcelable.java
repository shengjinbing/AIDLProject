package com.example.lx.aidldemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by lixiang
 * on 2018/12/13
 */
public class UserParcelable implements Parcelable {
    private String name;
    private int age;

    /**
     * 反序列化的过程需要传递当前线程的上下文加载器
     *
     * @param in
     */
    protected UserParcelable(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    /*
      CREATOR完成反序列化功能
     */
    public static final Creator<UserParcelable> CREATOR = new Creator<UserParcelable>() {
        /**
         *从序列化的对象中创建原始对象
         *
         * @param in
         * @return
         */
        @Override
        public UserParcelable createFromParcel(Parcel in) {
            return new UserParcelable(in);
        }

        /**
         * 创建指定长度的原始对象数组
         *
         * @param size
         * @return
         */
        @Override
        public UserParcelable[] newArray(int size) {
            return new UserParcelable[size];
        }
    };

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 完成序列化功能
     *
     * @param dest
     * @param flags 值0或1，为1时标识当前对象需要作为返回值返回，不能立即释放资源。默认情况都是0；
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
    }
}
