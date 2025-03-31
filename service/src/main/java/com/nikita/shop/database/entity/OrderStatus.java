package com.nikita.shop.database.entity;

public enum OrderStatus {
    BASKET, // корзина
    ASSEMBLY, // сборка заказа
    SENT, // заказ отправлен
    RECEIVED,
    CANCELLED
}
