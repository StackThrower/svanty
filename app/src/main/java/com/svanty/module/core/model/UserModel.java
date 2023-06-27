package com.svanty.module.core.model;

import com.svanty.module.core.db.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
public class UserModel {
    private Integer id;
    private String username;
    private String name;
    private String bio;
    private String password;
    private String email;
    private Date date;
    private String formattedDate;
    private String avatar;
    private String cover;
    private User.Status status;
    private User.TypeAccount typeAccount;
    private User.Role role;
    private String website;
    private String remember_token;
    private String twitter;
    private String facebook;
    private String google;
    private String paypal_account;
    private String oauth_uid;
    private String oauth_provider;
    private String token;
    private User.AuthorizedToUpload authorizedToUpload;
    private String instagram;
    private BigDecimal funds;
    private BigDecimal balance;
    private String paymentGateway;
    private String bank;
    private String ip;
    private User.AuthorExclusive authorExclusive;
    private Long likeCount;
    private Long photosCount;
}
