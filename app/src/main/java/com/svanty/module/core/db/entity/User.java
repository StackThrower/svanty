package com.svanty.module.core.db.entity;

import com.svanty.module.stock.db.entity.Images;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "users", schema = "public")
public class User implements Serializable {

    public static final int MODIFICATION_TIME_OUT = 15 * 1000 * 60; // In 15 minutes
    public enum Status {
        pending, active, suspended, delete
    }

    public enum TypeAccount {
        buyer, seller
    }

    public enum Role {
        normal, admin
    }

    public enum AuthorizedToUpload {
        yes, no
    }

    public enum AuthorExclusive {
        yes, no
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    private String username;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "bio", nullable = false, length = 200)
    private String bio;

    @Column(name = "countries_id", nullable = false)
    private String countries_id;

    @Column(name ="city", nullable = false, length = 255)
    private String city;

    @Column(name ="password", nullable = false, length = 60)
    private String password;

    @Column(name ="email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name="modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Column(name ="avatar", nullable = false, length = 70)
    private String avatar;

    @Column(name ="cover", nullable = false, length = 255)
    private String cover;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="type_account", nullable = false)
    private TypeAccount typeAccount;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @Column(name ="website", nullable = false, length = 255)
    private String website;

    @Column(name ="remember_token", nullable = false, length = 100)
    private String remember_token;

    @Column(name ="twitter", nullable = false, length = 200)
    private String twitter;

    @Column(name ="facebook", nullable = false, length = 200)
    private String facebook;

    @Column(name ="google", nullable = false, length = 200)
    private String google;

    @Column(name ="paypal_account", nullable = false, length = 200)
    private String paypal_account;

    @Column(name ="activation_code", nullable = false, length = 150)
    private String activation_code;

    @Column(name ="oauth_uid", nullable = false, length = 200)
    private String oauth_uid;

    @Column(name ="oauth_provider", nullable = false, length = 200)
    private String oauth_provider;

    @Column(name ="token", nullable = false, length = 80)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name="authorized_to_upload", nullable = false)
    private AuthorizedToUpload authorizedToUpload;

    @Column(name ="instagram", nullable = false, length = 200)
    private String instagram;

    @Column(name="funds", nullable = false)
    private BigDecimal funds;

    @Column(name="balance", nullable = false)
    private BigDecimal balance;

    @Column(name ="payment_gateway", nullable = false, length = 50)
    private String paymentGateway;

    @Column(name="bank", nullable = false, columnDefinition = "TEXT")
    private String bank;

    @Column(name ="ip", nullable = false, length = 30)
    private String ip;

    @Enumerated(EnumType.STRING)
    @Column(name="author_exclusive", nullable = false)
    private AuthorExclusive authorExclusive;
}
