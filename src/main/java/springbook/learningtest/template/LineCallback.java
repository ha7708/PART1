package springbook.learningtest.template;

public interface LineCallback<T> {  // 제네릭 타입 파라미터 <T>: 결과 타입 여러가지로 하기 위해 추가함
    T doSomethingWithLine(String line, T value);
}

