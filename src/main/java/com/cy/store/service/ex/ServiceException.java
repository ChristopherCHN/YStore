package com.cy.store.service.ex;

/* 作为业务层异常的基类：throws new ServiceException(根据需要修改参数或保持无参)
 * 由于业务层的所有异常都在运行时产生，所以都继承RuntimeException。
*/
public class ServiceException extends RuntimeException{
    //借用右键快捷方式【生成-重写】，选中父类RuntimeException的全部5个，定义构造方法

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
