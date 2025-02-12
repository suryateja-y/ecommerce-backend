package com.academy.projects.ecommerce.ordermanagementservice.services;

import com.academy.projects.ecommerce.ordermanagementservice.configurations.IdGenerator;
import com.academy.projects.ecommerce.ordermanagementservice.models.Invoice;
import com.academy.projects.ecommerce.ordermanagementservice.models.Order;
import com.academy.projects.ecommerce.ordermanagementservice.repositories.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class InvoiceService implements IInvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final IdGenerator idGenerator;

    Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository, IdGenerator idGenerator) {
        this.invoiceRepository = invoiceRepository;
        this.idGenerator = idGenerator;
    }

    @Override
    public Invoice createInvoice(Order order) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setCustomerId(order.getCustomerId());
        invoice.setInvoiceNumber(idGenerator.getId("Invoice"));
        invoice.setTotalAmount(order.getTotalAmount());
        invoice.setCurrencyType(order.getPaymentDetails().getCurrencyType());
        invoice.setOrder(order);
        invoice.setPaymentDetails(order.getPaymentDetails());
        invoice = invoiceRepository.save(invoice);
        logger.info("Invoice created: '{}'!!!", invoice.getInvoiceNumber());
        return invoice;
    }
}
