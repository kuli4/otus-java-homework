package pro.kuli4.otus.java.hw14.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class UserDto {
    private String name;
    private String login;
    private AddressDataSetDto address;
    private List<PhoneDataSetDto> phones = new ArrayList<>();
}
