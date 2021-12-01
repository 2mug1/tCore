package com.github.iamtakagi.tcore.util;

import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class DigitCode {

    static final Random r = new Random();

    private int size;

    public int generate(){
        int data[] = new int[size];
        for(int i = 0; i < size; i++){
            data[i] = r.nextInt(9) + 1;
        }
        StringBuilder b = new StringBuilder();
        for (int n : data) {
            b.append(n);
        }
        return Integer.valueOf(b.toString());
    }
}
