package com.ll.sbbdoc20250109.domain.user;

import com.ll.sbbdoc20250109.domain.base.BaseTime;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "SiteUser")
@NoArgsConstructor
@AllArgsConstructor
public class SiteUser extends BaseTime {

    @Column(unique = true, length = 20)
    private String username;

    @Column(length = 50)
    private String password;

    @Column(length = 20)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(unique = true, length = 50)
    private String apiKey;

    public String getNickname() {
        return nickname;
    }

    public boolean isAdmin() {
        return "admin".equals(username);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public SiteUser(long id, String username) {
        this.setId(id);
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesAsStringList()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public List<String> getAuthoritiesAsStringList() {
        List<String> authorities = new ArrayList<>();

        if (isAdmin()) authorities.add("ROLE_ADMIN");

        return authorities;
    }

}
