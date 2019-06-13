package com.example.joe.cityumobile.Core;

import java.util.ArrayList;
import java.util.List;

public interface MyObserver {
    List<Integer> eventCodes = new ArrayList<>();

    void update(Integer... eventMessageCode);
}
