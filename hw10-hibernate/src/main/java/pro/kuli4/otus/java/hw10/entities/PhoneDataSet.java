package pro.kuli4.otus.java.hw10.entities;

import lombok.*;

import javax.persistence.*;


@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "PHONES")
public class PhoneDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NUMBER")
    private String number;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude private User user;
}
