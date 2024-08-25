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
public class MemberVO {
    private Long memberId;
    private String name;
    private Character gender;
    private Integer age;
    private String phoneNumber;
    private String carNumber;
    private Integer isDeleted;
    private Date createdAt;
    private Date updatedAt;
}