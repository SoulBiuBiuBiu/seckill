package com.example.miaosha.exception;

import com.example.miaosha.result.CodeMsg;

public class GlobalException extends RuntimeException {

    public CodeMsg getCm() {
        return cm;
    }

    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm=cm;
    }
}
