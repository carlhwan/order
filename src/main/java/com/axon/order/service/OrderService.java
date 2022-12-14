package com.axon.order.service;

import com.axon.order.command.CreateOrderCommand;
import com.axon.order.entity.OrderEntity;
import com.axon.order.event.OrderCreatedEvent;
import com.axon.order.query.GetOrdersQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public void createOrder(String productId, int quantity) {
        // product과 관련 있는 부분
        // command를 발행
        commandGateway.sendAndWait(new CreateOrderCommand(UUID.randomUUID().toString(), productId, quantity));
    }

    public List<OrderEntity> getOrders() throws ExecutionException, InterruptedException {
        List<OrderEntity> oel = queryGateway.query(new GetOrdersQuery(),
                ResponseTypes.multipleInstancesOf(OrderEntity.class)).get();

        return oel;
    }

}
