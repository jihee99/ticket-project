package com.ex.hero.events.model.dto.request;

import com.ex.hero.common.annotation.Enum;

import com.ex.hero.events.model.EventStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateEventStatusRequest {
	@Schema(defaultValue = "OPEN", description = "오픈 상태")
	@Enum(message = "올바른 상태값을 입력해주세요.")
	private EventStatus status;
}
