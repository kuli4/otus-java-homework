package pro.kuli4.otus.java.hw10.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "ADDRESSES")
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "STREET")
    private String street;

    @OneToOne(mappedBy = "address")
    @ToString.Exclude private User user;
}
