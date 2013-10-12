package org.oneandone.idev.johanna.store;

/**
 *
 * @author Alex Kiesel <alex.kiesel@1und1.de>
 */
public class PlainValue implements Value {
    String value;
    
    public PlainValue(String value) {
        this.value= value;
    }
    
    @Override
    public String asEncoded() {
        return this.value;
    }

    @Override
    public byte[] asIntern() {
        return this.value.getBytes();
    }
}
