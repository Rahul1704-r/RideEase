package com.Uber.Project.UberApp.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name="idx_rider", columnList = "rider_id")
})
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    /* means table rider will have a field called user_id and
     user_id will be the foreign key of user
     */
    private User user;

    private Double rating;
}
