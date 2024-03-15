package com.ex.hero.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

        @Schema(description = "회원 이메일", example = "user@test.com")
        private String email;

        @Schema(description = "회원 비밀번호", example = "1234")
        String password;



}
