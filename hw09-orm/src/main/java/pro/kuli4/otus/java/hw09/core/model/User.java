package pro.kuli4.otus.java.hw09.core.model;

import lombok.*;
import pro.kuli4.otus.java.hw09.core.model.annotations.Id;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Setter(AccessLevel.NONE)
    private long id;
    @NonNull
    private String name;
    @NonNull
    private int age;
}
