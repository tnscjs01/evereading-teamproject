package com.evereading.best.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@ToString
public class MemberDTO extends User  implements OAuth2User {

    private String email;

    private String pw;

    private String nickname;

    private boolean social;

    private Map<String, Object> attr;

    public MemberDTO(String username, String nickname ,String password, boolean fromSocial,
                             Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr) {
        this(username,nickname, password, fromSocial, authorities);
        this.attr = attr;
    }

    public MemberDTO(String username, String nickname ,String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = username;
        this.pw = password;
        this.social = fromSocial;
        this.nickname = nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    @Override
    public String getName() {
        return this.nickname;
    }

}