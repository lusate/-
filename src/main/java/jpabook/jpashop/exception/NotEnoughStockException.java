package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    //오버라이드 하는 이유
    //메시지(message)들 넘겨주고 예외 발생한 cause 들 넣어서 Exception Trace 가 나올 수 있도록 하기 위함.
}
