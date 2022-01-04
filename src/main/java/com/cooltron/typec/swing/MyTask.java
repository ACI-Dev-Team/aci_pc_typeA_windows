package com.cooltron.typec.swing;

import javafx.concurrent.Task;

public class MyTask extends Task<String> {

    private String message;

    public void setMessage(String message) throws Exception {
        this.message = message;
        call();
    }

    @Override
    protected String call() throws Exception {
        this.updateMessage(message);
        return message;
    }

    protected void updateMessage(String message)
    {
        // TODO Auto-generated method stub
        super.updateMessage(message);
    }
}
