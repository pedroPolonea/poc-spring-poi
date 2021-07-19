package com.p.sx.service;

import com.p.sx.model.OrderEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OrderService {

    OrderEntity importOrders(MultipartFile file) throws IOException;
}
