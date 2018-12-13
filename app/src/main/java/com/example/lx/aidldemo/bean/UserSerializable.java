package com.example.lx.aidldemo.bean;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by lixiang
 * on 2018/12/13
 */
public class UserSerializable implements Serializable {
    //只有两个对象的serialVersionUID相同才能反序列化成功
    private static final long serialVersionUID = 519391278216481276L;
    private String name;
    private int age;

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

    /**
     * 序列化过程
     */
    public void writeObject(ObjectOutputStream out) throws IOException {
        UserSerializable userSerializable = new UserSerializable();
        userSerializable.setAge(11);
        userSerializable.setName("zhanshan");
        out.writeObject(userSerializable);
        out.close();

    }

    /**
     * 反序列化
     */
    public void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        UserSerializable userSerializable = (UserSerializable) in.readObject();
        in.close();
    }
}
