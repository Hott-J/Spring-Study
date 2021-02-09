package com.example.SpringBootCommunityWeb.domain.enums;

public enum SocialType {

    FACEBOOK("facebook"),
    GOOGLE("google");

    private final String ROLE_PREFIX = "ROLE_";
    private String name;

    SocialType(String name) { // 생성자
        this.name = name;
    }

    public String getRoleType() {
        return ROLE_PREFIX + name.toUpperCase();
    }

    public String getValue() {
        return name;
    }

    public boolean isEquals(String autority) {
        return this.name.equals(autority);
    }
}
