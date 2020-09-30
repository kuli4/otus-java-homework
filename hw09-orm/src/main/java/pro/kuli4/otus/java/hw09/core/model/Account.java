package pro.kuli4.otus.java.hw09.core.model;

import lombok.*;
import pro.kuli4.otus.java.hw09.core.model.annotations.Id;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @Setter(AccessLevel.NONE)
    private long no;
    @NonNull
    private String type;
    @NonNull
    private BigDecimal rest;
}
