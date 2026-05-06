package com.nexus.inventory.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.NullMarked;

import java.time.Instant;

@NullMarked
@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long productId;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantity = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockReservationStatus status = StockReservationStatus.PENDING;

    @Builder.Default
    @Column(nullable = false)
    private Instant expiresAt = Instant.now().plusSeconds(60 * 10);

}
