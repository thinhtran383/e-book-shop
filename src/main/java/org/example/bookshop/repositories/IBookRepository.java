package org.example.bookshop.repositories;

import org.example.bookshop.entities.Book;
import org.example.bookshop.responses.book.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {

    @Query("""
                Select 
                new org.example.bookshop.responses.book.BookResponse(
                b.id,
                 b.title, 
                 b.author, 
                 b.price, 
                 b.quantity, 
                 b.categoryID.categoryName, 
                 b.description, 
                 b.publisher, 
                 b.publishedDate, 
                 b.image, 
                 b.averageRating
                 )
                From Book b
            """)
    Page<BookResponse> findAllBook(Pageable pageable);
}