package com.opensourcewithslu.inputdevices;
import com.pi4j.crowpi.components.exceptions.RfidException;
import com.pi4j.context.Context;
import com.pi4j.io.spi.SpiConfig;
import io.micronaut.context.annotation.Prototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.pi4j.crowpi.components.RfidComponent;

import java.util.concurrent.atomic.AtomicReference;


@Prototype
public class RFidHelper {
    private static final Logger log = LoggerFactory.getLogger(RFidHelper.class);
    private RfidComponent scanner;

    public RFidHelper(SpiConfig config, int reset, Context pi4jContext){
        this.scanner = new RfidComponent(pi4jContext, reset, config.getAddress(), config.getBaud());
    }

    public RFidHelper(SpiConfig config, Context pi4jContext){
        this.scanner = new RfidComponent(pi4jContext, config.getAddress(), config.getBaud());
    }
    public void writeToCard(Object data){
        scanner.waitForAnyCard(card -> {
            try {
                card.writeObject(data);
            } catch (RfidException e){
                log.info(e.getMessage());
            }
        });
    }
    public Object readFromCard(){
        AtomicReference<Object> data = new AtomicReference<>(new Object());
        scanner.waitForAnyCard(card -> {
            try {
                data.set(card.readObject());
            } catch (RfidException e){
                log.info(e.getMessage());
            }
        });
        return data;
    }

    public void resetScanner(){
        scanner.reset();
    }
}
