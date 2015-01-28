package com.maths22.cs162.rsa;

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.math.BigInteger;

public class RSAInputStream extends FilterInputStream {
    private final RSAKey key;
    private byte[] input;
    private int ptr;
    private int len;
    private final int offset;

    public RSAInputStream(InputStream in, RSAKey key) {
        super(in);
        this.input = new byte[257];
        this.key = key;
        this.ptr = 0;
        this.offset = 0;
    }

    @Override
    public int read() throws IOException {
        if(len-ptr>0){
        } else {
            byte[] bytes = new byte[257];
            int ret = in.read(bytes,0,257);
            if (ret == -1){
                return -1;
            }
            BigInteger msg = new BigInteger(bytes);

            BigInteger recvMsg = msg.modPow(key.getExp(),key.getN());
            input = recvMsg.toByteArray();
            len = (int)input[input.length-1] & 0xFF;
            ptr = 0;
        }
        byte ret = input[ptr+offset];
        ptr++;
        return ret;
    }

    
}
