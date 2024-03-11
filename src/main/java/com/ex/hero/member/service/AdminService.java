package com.ex.hero.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ex.hero.member.model.dto.response.MemberInfoResponse;
import com.ex.hero.member.model.MemberType;
import com.ex.hero.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getMembers() {
        return memberRepository.findAllByAccountRole(MemberType.USER).stream()
                .map(MemberInfoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getSellers() {
        return memberRepository.findAllByAccountRole(MemberType.MANAGER).stream()
            .map(MemberInfoResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<MemberInfoResponse> getAdmins() {
        return memberRepository.findAllByAccountRole(MemberType.ADMIN).stream()
                .map(MemberInfoResponse::from)
                .toList();
    }


}
