package com.ite.sws.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthVO {
    private Long memberId;
    private String role;
    private String loginId;
    private String password;
    private Date createdAt;
    private Date updatedAt;
}
