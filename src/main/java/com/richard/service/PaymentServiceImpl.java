package com.richard.service;

import com.richard.model.Order;
import com.richard.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    
    @Override
    public PaymentResponse createPaymentLink(Order order) throws StripeException {
        Stripe.apiKey = stripeApiKey;
        
        SessionCreateParams params = SessionCreateParams.builder()
                                                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                                                        .setMode(SessionCreateParams.Mode.PAYMENT)
                                                        .setSuccessUrl("http://localhost:3000/payment/success" + order.getId())
                                                        .setCancelUrl("http://localhost:3000/payment/cancel")
                                                        .addLineItem(SessionCreateParams.LineItem.builder()
                                                                                                 .setQuantity(1L)
                                                                                                 .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                                                                                                                                     .setCurrency(
                                                                                                                                                             "usd")
                                                                                                                                                     .setUnitAmount(
                                                                                                                                                             order.getTotalAmount() *
                                                                                                                                                             100)
                                                                                                                                                     .setProductData(
                                                                                                                                                             SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                                                                                                                                                               .setName(
                                                                                                                                                                                                                       "Zosh food")
                                                                                                                                                                                                               .build())
                                                                                                                                                     .build())
                                                                                                 .build())
                                                        .build();
        Session session = Session.create(params);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentUrl(session.getUrl());
        
        return null;
    }
}
