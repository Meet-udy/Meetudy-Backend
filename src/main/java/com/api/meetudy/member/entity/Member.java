package com.api.meetudy.member.entity;

import com.api.meetudy.member.dto.AdditionalInfoDto;
import com.api.meetudy.group.enums.Location;
import com.api.meetudy.interest.entity.Interest;
import com.api.meetudy.interest.entity.MemberInterest;
import com.api.meetudy.member.enums.LoginType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name  = "member")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String major;

    @Column(nullable = true)
    private String introduction;

    @Column(nullable = true)
    private Boolean isOnline;

    @Column(nullable = true)
    private String profileImage;

    @Column(nullable = false)
    @Builder.Default
    private Integer activityScore = 50;

    @Column(nullable = true)
    private Boolean notificationEnabled;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MemberInterest> memberInterests = new ArrayList<>();

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public void updateRoles(List<String> roles) {
        this.roles = roles;
    }

    public void updateActivityScore(Integer activityScore) {
        this.activityScore = activityScore;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateAdditionalInfo(AdditionalInfoDto dto) {
        this.nickname = dto.getNickname();
        this.major = dto.getMajor();
        this.introduction = dto.getIntroduction();
        this.isOnline = dto.getIsOnline();
        this.notificationEnabled = dto.getNotificationEnabled();
        this.location = dto.getLocation();
    }

    public List<Interest> getInterests() {
        return memberInterests.stream()
                .map(MemberInterest::getInterest)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}