package com.sim.management;

import jakarta.persistence.*;

@Entity
@Table(name = "sims")
public class Sim {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sim_seq")
    @SequenceGenerator(name = "sim_seq", sequenceName = "sim_sequence", allocationSize = 1)
    private Long id;

    private String provider;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "org_name")
    private String orgName;

    public Sim() {}

    public Long getId() {
        return id;
    }

    public String getProvider() {
        return provider;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}