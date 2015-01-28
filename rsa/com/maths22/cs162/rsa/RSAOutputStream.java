package com.maths22.cs162.rsa;

import java.math.BigInteger;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.io.IOException;

public class RSAOutputStream extends FilterOutputStream {

    private final RSAKey key;
    private final byte[] output;
    private int ptr;
    private final int len;
    private final int offset;

    public RSAOutputStream(OutputStream out, RSAKey key) {
        super(out);
        this.output = new byte[257];
        this.key = key;
        this.ptr = 0;
        this.offset = 0;
        this.len = 236;
    }

    @Override
    public void write(int b) throws IOException {
        output[ptr+offset]=((byte) b);
        ptr++;
        if (ptr > len) {
            this.flush();
        }
    }

    @Override
    public void flush() throws IOException {
        byte[] bytes = new byte[ptr+1];
        System.arraycopy(output, 0, bytes, 0, ptr);
        if (ptr > 0) {
            bytes[bytes.length-1]=(byte)(bytes.length-1);
            
            BigInteger msg = new BigInteger(bytes);
            BigInteger sendMsg = msg.modPow(key.getExp(), key.getN());
            byte[] newKey = new byte[257];
            byte[] oldKey = sendMsg.toByteArray();
            System.arraycopy(oldKey, 0, newKey, newKey.length - oldKey.length, oldKey.length);
            ptr=0;
            out.write(newKey);
            out.flush();
        }
    }
}
