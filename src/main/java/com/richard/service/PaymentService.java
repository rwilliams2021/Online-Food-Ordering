package com.richard.service;

import com.richard.model.Order;
import com.richard.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentResponse createPaymentLink(Order order) throws StripeException;
}
