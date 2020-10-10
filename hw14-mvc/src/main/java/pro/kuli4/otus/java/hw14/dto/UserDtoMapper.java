package pro.kuli4.otus.java.hw14.dto;

import org.springframework.stereotype.Component;
import pro.kuli4.otus.java.hw14.entities.User;

import java.util.stream.Collectors;

@Component
public class UserDtoMapper {
    public UserDto map(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());
        AddressDataSetDto addressDataSetDto = new AddressDataSetDto(user.getAddress().getStreet());
        userDto.setAddress(addressDataSetDto);
        userDto.setPhones(user.getPhones().stream().map(phone -> new PhoneDataSetDto(phone.getNumber())).collect(Collectors.toList()));
        return userDto;
    }
}
