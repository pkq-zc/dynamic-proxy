package com.buydeem;

public class StaticUserDao implements UserDao{
    private UserDao target;

    public StaticUserDao(UserDao target) {
        this.target = target;
    }

    @Override
    public void save() {
        System.out.println("开启事务");
        target.save();
        System.out.println("提交事务");
    }
}
