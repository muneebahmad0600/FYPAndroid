package com.example.muneebahmad.edwbqfgb;

import java.util.HashMap;

public class Order {
    private static Order instance;
    private HashMap<Number, Number> pqMap;
    private Order(){
        pqMap = new HashMap<>();
    }

    public static Order getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new Order();
        return instance;
    }

    public void addOrIncrement(int productId) {
        if (pqMap.keySet().contains(productId)) {
            Number quantity = pqMap.get(productId);
            pqMap.put(productId, (int)quantity + 1);
        }else {
            pqMap.put(productId, 1);
        }
    }

    public void decrementQuantity(int productId) {
        if (pqMap.keySet().contains(productId)) {
            Number quantity = pqMap.get(productId);
            if ((int)quantity == 1) {
                pqMap.remove(productId);
            }else {
                pqMap.put(productId, (int)quantity - 1);
            }
        }
    }

    public int getQuantity(int productId) {
        if (pqMap.keySet().contains(productId)) {
            return (int)pqMap.get(productId);
        }
        return 0;
    }

    public  void clear (){
        pqMap.clear();
    }
    public HashMap<Number,Number> getPqMap(){
        return pqMap;
    }
}
