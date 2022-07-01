package com.sample.sample.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MessagesTest {

    @Test
    public void testDefaultMessage(){
        Message message = new Message();
        Assertions.assertEquals("Hello Vish!", message.getMessage("Vish"));
    }

    @Test
    public void testEmptyMessage(){
        Message message = new Message();
        Assertions.assertEquals("Please Provide name!", message.getMessage(""));
    }

    @Test
    public void testNullMessage(){
        Message message = new Message();
        Assertions.assertEquals("Please Provide name!", message.getMessage(null));
    }


}
