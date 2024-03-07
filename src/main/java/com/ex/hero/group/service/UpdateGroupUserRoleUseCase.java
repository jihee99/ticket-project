package com.ex.hero.group.service;


import org.springframework.stereotype.Service;

import com.ex.hero.group.dto.request.UpdateGroupUserRoleRequest;
import com.ex.hero.group.dto.response.GroupDetailResponse;
import com.ex.hero.group.model.Group;
import com.ex.hero.group.model.GroupUserRole;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateGroupUserRoleUseCase {
	private final CommonGroupService commonGroupService;
	private final GroupService groupService;

	public GroupDetailResponse execute(Long groupId, UpdateGroupUserRoleRequest updateGroupUserRoleRequest) {
		final Group group = commonGroupService.findById(groupId);
		final Long updateUserId = updateGroupUserRoleRequest.getUserId();

		final GroupUserRole updateUserRole = updateGroupUserRoleRequest.getRole();
		return commonGroupService.toGroupDetailResponseExecute(groupService.updateGroupUserRole(group, updateUserId, updateUserRole));
	}

}