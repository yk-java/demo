package com.glens.model;

import java.io.Serializable;

/**
 * @author zjp47
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer errorCode;
    private String errorMsg;
    private boolean result;
    private Object content;

    public Message() {
    }

    public Message(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Message(String errorMsg, boolean result) {
        this.errorMsg = errorMsg;
        this.result = result;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
