package com.ex.hero.order.service;

import com.ex.hero.order.model.OrderItem;
import org.springframework.stereotype.Service;

import com.ex.hero.common.util.MemberUtils;
import com.ex.hero.member.model.Member;
import com.ex.hero.order.model.Order;
import com.ex.hero.order.model.dto.request.CreateOrderRequest;
import com.ex.hero.order.model.dto.response.CreateOrderResponse;
import com.ex.hero.order.model.mapper.OrderMapper;
import com.ex.hero.ticket.model.TicketItem;
import com.ex.hero.ticket.model.TicketPayType;
import com.ex.hero.ticket.service.CommonTicketItemService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase {

	private final MemberUtils memberUtils;
	private final CommonTicketItemService commonTicketItemService;
	private final CommonOrderService commonOrderService;
//	private final OrderValidator orderValidator;
	private final OrderMapper orderMapper;

	public CreateOrderResponse execute(CreateOrderRequest createOrderRequest) {
		Member member = memberUtils.getCurrentMember();
		// 주문서를 생성하는 코드
		Order order = createOrder(createOrderRequest, member.getUserId());
		return orderMapper.toCreateOrderResponse(order.getId());
	}


	// 주문서 생성 함수
	private Order createOrder(CreateOrderRequest request, Long userId){
		TicketItem ticketItem = commonTicketItemService.queryTicketItem(request.getTicketId());
		TicketPayType payType = ticketItem.getPayType();
		OrderItem orderItem = OrderItem.of(ticketItem, request.getQuantity());

		return null;
//		if(payType == TicketPayType.FREE_TICKET){
//			if (ticketItem.isFCFS()) {
//				return Order.createPaymentOrder(userId, ticketItem, orderValidator);
//			}
//			// 승인 티켓
//			return Order.createApproveOrder(userId, ticketItem, orderValidator);
//		}
//		return Order.createPaymentOrder(userId, cart, ticketItem, orderValidator);
	}

}
