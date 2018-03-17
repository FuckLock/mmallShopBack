package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;

public interface IShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse del(Integer userId, Integer shippingId);

    ServerResponse update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer id, Integer shippingId);

    ServerResponse<PageInfo> list(Integer id, int pageNum, int pageSize);
}
