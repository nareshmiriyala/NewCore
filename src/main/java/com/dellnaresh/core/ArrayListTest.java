package com.dellnaresh.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nareshm on 28/02/2015.
 */
public class ArrayListTest {
    public static void main(String[] args) {
        List<Integer> longList=new ArrayList<>();
        longList.add(10);
        longList.add(11);
        longList.add(12);
        longList.add(1,13);
        longList.add(33);

        System.out.println(longList.stream().mapToLong(s->s).sum());
    }
}
