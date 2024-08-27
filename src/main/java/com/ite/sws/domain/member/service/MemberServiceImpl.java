package com.ite.sws.domain.member.service;

import com.ite.sws.domain.member.dto.GetMemberRes;
import com.ite.sws.domain.member.dto.JwtToken;
import com.ite.sws.domain.member.dto.PostLoginReq;
import com.ite.sws.domain.member.dto.PostMemberReq;
import com.ite.sws.domain.member.mapper.MemberMapper;
import com.ite.sws.domain.member.vo.AuthVO;
import com.ite.sws.domain.member.vo.MemberVO;
import com.ite.sws.exception.CustomException;
import com.ite.sws.exception.ErrorCode;
import com.ite.sws.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 회원 서비스 구현체
 * @author 정은지
 * @since 2024.08.24
 * @version 1.0
 *
 * <pre>
 * 수정일        	수정자        수정내용
 * ----------  --------    ---------------------------
 * 2024.08.24  	정은지        최초 생성
 * 2024.08.24   정은지        중복 아이디 체크 및 회원가입 기능 추가
 * 2024.08.25   정은지        로그인 기능 추가
 * </pre>
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 아이디 중복 체크
     * @param loginId 아이디
     * @return 아이디 중복 여부
     */
    @Override
    public boolean isLoginIdAvailable(String loginId) {
        return memberMapper.selectCountByLoginId(loginId) == 0;
    }

    /**
     * 회원가입
     * @param postMemberReq 회원 정보
     */
    @Transactional
    @Override
    public void addMember(PostMemberReq postMemberReq) {
        MemberVO member = MemberVO.builder()
                .name(postMemberReq.getName())
                .gender(postMemberReq.getGender())
                .age(postMemberReq.getAge())
                .phoneNumber(postMemberReq.getPhoneNumber())
                .carNumber(postMemberReq.getCarNumber())
                .build();

        memberMapper.insertMember(member);

        AuthVO auth = AuthVO.builder()
                .memberId(member.getMemberId())
                .loginId(postMemberReq.getLoginId())
                .password(passwordEncoder.encode(postMemberReq.getPassword()))
                .build();

        memberMapper.insertAuth(auth);
    }

    /**
     * 로그인
     * @param postLoginReq 아이디, 비밀번호
     * @return JwtToken 객체
     */
    @Transactional
    @Override
    public JwtToken findMemberByLoginId(PostLoginReq postLoginReq) {

        log.info("로그인 요청 정보", postLoginReq);

        // 로그인 아이디로 사용자 조회
        Optional<AuthVO> authOptional = memberMapper.selectMemberByLoginId(postLoginReq.getLoginId());

        // 사용자가 존재하지 않으면 예외 발생
        if(!authOptional.isPresent()) {
            throw new CustomException(ErrorCode.FIND_FAIL_MEMBER_ID);
        }

        // 비밀번호가 일치하지 않으면 예외 발생
        AuthVO auth = authOptional.get();

        if(!passwordEncoder.matches(postLoginReq.getPassword(), auth.getPassword())) {
            throw new CustomException(ErrorCode.FIND_FAIL_MEMBER_ID);
        }

        // 아이디와 비밀번호를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(postLoginReq.getLoginId(), postLoginReq.getPassword());

        // 실제 검증
        // authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication, auth.getMemberId());

        return jwtToken;
    }

    /**
     * 멤버 아이디로 회원 정보 조회
     * @param memberId 멤버 아이디
     * @return MemberVO 객체
     */
    @Override
    public GetMemberRes findMemberByMemberId(Long memberId) {

        return memberMapper.selectMemberByMemberId(memberId);
    }
}
