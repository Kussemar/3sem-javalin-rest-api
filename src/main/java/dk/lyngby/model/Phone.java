package dk.lyngby.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPhonePK", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "number", nullable = false, length = 45)
    private String number;

    @Column(name = "description", length = 45)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPersonFK", nullable = false)
    private Person idPersonFK;

    public Phone(@NotNull String number, String description) {
        this.number = number;
        this.description = description;
    }

    public void addPersonToPhone(Person p) {
        if (p != null) p.addPhoneToPerson(this);
    }
}

