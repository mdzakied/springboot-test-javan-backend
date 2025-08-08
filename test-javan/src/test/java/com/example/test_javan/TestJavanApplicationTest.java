package com.example.test_javan;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestJavanApplicationTest {

    @Test
    void main_runsWithoutException() {
        TestJavanApplication.main(new String[]{});
    }
}
