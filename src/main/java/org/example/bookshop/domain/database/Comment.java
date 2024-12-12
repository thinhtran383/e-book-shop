package org.example.bookshop.domain.database;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Comments", schema = "book_shop")
public class Comment {
    @Id
    @Column(name = "CommentID", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserID", nullable = false)
    private User userID;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "BookID", nullable = false)
    private Book bookID;

    @NotNull
    @Lob
    @Column(name = "Content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "CommentDate", nullable = false)
    private LocalDateTime commentDate;

    @PrePersist
    public void prePersist() {
        this.id = (int) (Math.random() * 1000000);
    }

}