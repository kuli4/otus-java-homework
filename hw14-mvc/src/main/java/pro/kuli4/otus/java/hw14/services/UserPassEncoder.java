package pro.kuli4.otus.java.hw14.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class UserPassEncoder implements PassEncoder {

    @Override
    public String encode(String pass) {
        return DigestUtils.md5Hex(pass);
    }

}
