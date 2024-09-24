package org.example.bookshop.repositories;

import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.Category;
import org.example.bookshop.responses.book.BookResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


@Repository
public interface IBookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book>  {

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

    Page<Book> findBooksByCategoryID(Category categoryID, Pageable pageable);
}