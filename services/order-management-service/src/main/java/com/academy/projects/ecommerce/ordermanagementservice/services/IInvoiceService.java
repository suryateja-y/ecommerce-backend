package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.models.Invoice;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;

public interface IInvoiceService {
    Invoice createInvoice(Order order);
}
