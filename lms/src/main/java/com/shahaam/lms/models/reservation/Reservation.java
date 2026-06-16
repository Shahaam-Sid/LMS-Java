package com.shahaam.lms.models.reservation;

import java.time.LocalDateTime;

import com.shahaam.lms.enums.ReservationStatus;
import com.shahaam.lms.models.Pupil.Member;
import com.shahaam.lms.models.book.AbstractBook;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reservation_queue")
@NoArgsConstructor
public class Reservation {

    @EmbeddedId
    private ReservationId id;

    @Column(name = "queue_position", nullable = false)
    private Integer queuePosition;

    @Column(name = "reserved_at", nullable = false, updatable = false)
    private LocalDateTime reservedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("isbn")
    @JoinColumn(name = "isbn", referencedColumnName = "isbn")
    private AbstractBook book;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    public Reservation(AbstractBook book, Member member, Integer queuePosition) {
        this.id = new ReservationId(book.getISBN(), member.getMemberID());
        this.book = book;
        this.member = member;
        this.queuePosition = queuePosition;
        this.reservedAt = LocalDateTime.now();
        this.status = ReservationStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        if (reservedAt == null) reservedAt = LocalDateTime.now();
        if (status == null) status = ReservationStatus.PENDING;
    }

    public ReservationId getId() {return id;}
    public AbstractBook getBook() {return book;}
    public Member getMember() {return member;}
    public Integer getQueuePosition() {return queuePosition;}
    public LocalDateTime getReservedAt() { return reservedAt; }
    public String getStatus() { return status.toString(); }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public void setQueuePosition(Integer queuePosition) {
        if (queuePosition < 0) throw new IllegalArgumentException("Queue Position cannot be nigative");
        this.queuePosition = queuePosition;
    }
}