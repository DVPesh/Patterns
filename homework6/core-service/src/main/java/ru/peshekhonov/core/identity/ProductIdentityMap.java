package ru.peshekhonov.core.identity;

import ru.peshekhonov.core.entities.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductIdentityMap {

    private static final Map<Long, Product> productMap = new ConcurrentHashMap<>();

    public static void add(Product product) {
        productMap.put(product.getId(), product);
    }

    public static Product get(Long id) {
        return productMap.get(id);
    }

    public static void delete(Long id) {
        productMap.remove(id);
    }

    public static boolean contains(Long id) {
        return productMap.containsKey(id);
    }
}
