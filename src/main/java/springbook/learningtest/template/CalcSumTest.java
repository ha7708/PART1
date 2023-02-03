package springbook.learningtest.template;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// 파일의 숫자 합을 계산하는 코드의 테스트
// 템플릿/콜백 활용예 테스트
public class CalcSumTest {

    Calculator calculator;
    String numFilepath = "numbers.txt";

    @Before public void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("numbers").getPath(); // 오류발생.
        //CalcSumTest.class.getResource("numbers.txt").getPath(); // 오류발생.
    }

    @Test public void initTest(){
        System.out.println("초기설정테스트");
    }

    @Test public void sumOfNumbers() throws IOException {
        assertThat(calculator.calcSum(this.numFilepath), is(10));
    }

    @Test public void multiplyOfNumbers() throws IOException {
        assertThat(calculator.calcMultiply(this.numFilepath), is(24));
    }

    @Test public void concatenateStrings() throws IOException {
        assertThat(calculator.concatenate(this.numFilepath), is("1234"));
    }

}

