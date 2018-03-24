package com.valdemar.service;

public class PollServiceFake implements PollService {
    @Override
    public int numberOfOpenPolls(String userId) {
        return 3;
    }
}
