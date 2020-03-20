package com.tisanehealth.Model.payout;

public class RewardIncomeModel {

    String member_id;
    String name;
    String reward_business;
    String right_business;
    String left_business;
    String generate_date;
    String reward;

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    String post;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReward_business() {
        return reward_business;
    }

    public void setReward_business(String reward_business) {
        this.reward_business = reward_business;
    }

    public String getRight_business() {
        return right_business;
    }

    public void setRight_business(String right_business) {
        this.right_business = right_business;
    }

    public String getLeft_business() {
        return left_business;
    }

    public void setLeft_business(String left_business) {
        this.left_business = left_business;
    }

    public String getGenerate_date() {
        return generate_date;
    }

    public void setGenerate_date(String generate_date) {
        this.generate_date = generate_date;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }


    public RewardIncomeModel(String member_id, String name,String post, String reward_business, String right_business, String left_business,String generate_date,String reward)
    {
        this.member_id=member_id;
        this.name=name;
        this.post=post;
        this.reward_business=reward_business;
        this.right_business=right_business;
        this.generate_date=generate_date;
        this.left_business=left_business;
        this.reward=reward;

    }


}
