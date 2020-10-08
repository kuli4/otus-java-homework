package pro.kuli4.otus.java.hw12.service;

import org.apache.commons.codec.digest.DigestUtils;

public class UserPassEncoder implements PassEncoder {

    @Override
    public String encode(String pass) {
        return DigestUtils.md5Hex(pass);
    }

}
