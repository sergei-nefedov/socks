package pers.nefedov.socks.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Table(name = "socks")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "color", nullable = false, length = 20)
    private String color;
    @Column(name = "cotton", nullable = false)
    private Double cottonPercentage;
    @Column(name = "quantity")
    @Min(0)
    private Integer quantity;
}
