package com.nexus.inventory.domain.model;


import com.nexus.inventory.domain.exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

@NullMarked
@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long productId;

    @Builder.Default
    @Column(nullable = false)
    private Integer availableQuantity = 0;

    @Builder.Default
    @Column(nullable = false)
    private Integer reservedQuantity = 0;

    @Version
    private Integer version;

    @Nullable
    @UpdateTimestamp
    private Instant updatedAt;

    /**
     * Business logic to reserve stock.
     * Guards against overselling at the Domain level.
     */
    public void reserve(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Reservation amount must be positive");
        }
        if (this.availableQuantity < amount) {
            throw new InsufficientStockException(productId, amount, availableQuantity);
        }

        this.availableQuantity -= amount;
        this.reservedQuantity += amount;
    }

    /**
     * Business logic to confirm a sale (finalize the reservation).
     */
    public void confirmReservation(int amount) {
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Attempting to confirm more than what is reserved");
        }
        this.reservedQuantity -= amount;
        // availableQuantity was already decremented during reservation
    }

    /**
     * Business logic to cancel a reservation and return items to available stock.
     */
    public void cancelReservation(int amount) {
        if (this.reservedQuantity < amount) {
            throw new IllegalStateException("Attempting to cancel more than what is reserved");
        }
        this.reservedQuantity -= amount;
        this.availableQuantity += amount;
    }

}
