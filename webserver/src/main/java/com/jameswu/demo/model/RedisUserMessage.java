package com.jameswu.demo.model;

import com.jameswu.demo.model.entity.UserProfile;
import java.io.Serializable;
import lombok.Data;

@Data
public class RedisUserMessage extends RedisMessage implements Serializable {

    UserProfile userProfile;

    public RedisUserMessage(boolean isAdd, UserProfile userProfile) {
        super(isAdd);
        this.userProfile = userProfile;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    //    public RedisUserMessage(boolean isAdd, UserProfile userProfile) {
    //        super(isAdd);
    //        this.userProfile = userProfile;
    //    }
}
