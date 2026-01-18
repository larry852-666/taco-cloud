package org.example.tacocloud.data.repository;

import org.example.tacocloud.data.TacoOrder;

public interface OrderRepository {
    TacoOrder save(TacoOrder tacoOrder);
}
