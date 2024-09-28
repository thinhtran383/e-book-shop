package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.book.BookDto;
import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.Category;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.repositories.IBookRepository;
import org.example.bookshop.repositories.ICategoryRepository;
import org.example.bookshop.responses.book.BookResponse;
import org.example.bookshop.specifications.BookSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookService {
    private final IBookRepository bookRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ICategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAllBook(pageable);
    }

//    @Transactional
//    public BookResponse createNewBook(BookDto bookDto) {
//        categoryRepository.findById(bookDto.getCategoryID()).orElseThrow(
//                () -> new DataNotFoundException("Category not found")
//        );
//
//        Book newBook = modelMapper.map(bookDto, Book.class);
//        newBook.setAverageRating(BigDecimal.ZERO);
//
//        cloudinaryService.uploadFile(bookDto.getImage())
//                .subscribe(uploadResult -> {
//                    String imageUrl = (String) uploadResult.get("url");
//                    newBook.setImage(imageUrl);
//
//                    newBook.setCategoryID(new Category(bookDto.getCategoryID(), bookDto.getCategoryName()));
//
//                    bookRepository.save(newBook);
//
//                });
//
//        return modelMapper.map(newBook, BookResponse.class);
//    }

    @Transactional
    public void createNewBook(BookDto bookDto) {
        categoryRepository.findById(bookDto.getCategoryID()).orElseThrow(
                () -> new DataNotFoundException("Category not found")
        );


        Book newBook = modelMapper.map(bookDto, Book.class);

        newBook.setAverageRating(BigDecimal.ZERO);

        cloudinaryService.uploadFile(bookDto.getImage())
                .doOnNext(uploadResult -> {
                    String imageUrl = (String) uploadResult.get("url");
                    newBook.setImage(imageUrl);
                    newBook.setCategoryID(new Category(bookDto.getCategoryID(), bookDto.getCategoryName()));
                }).subscribe(
                        uploadResult -> bookRepository.save(newBook)
                );

    }


    @Transactional
    public Page<BookResponse> getBooksByCategory(Integer categoryID, Pageable pageable) {
        Category category = new Category();
        category.setId(categoryID);

        Page<Book> books = bookRepository.findBooksByCategoryID(category, pageable);

        return books.map(book -> modelMapper.map(book, BookResponse.class));
    }

    @Transactional
    public BookResponse deleteBook(Integer bookID) {
        Book book = bookRepository.findById(bookID).orElseThrow();

        bookRepository.delete(book);

        return modelMapper.map(book, BookResponse.class);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> filterBooks(Integer category, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable) {
        Specification<Book> spec = Specification.where(BookSpecification.hasCategory(category))
                .and(BookSpecification.hasPriceGreaterThan(priceMin))
                .and(BookSpecification.hasPriceLessThan(priceMax));

        Page<Book> booksPage = bookRepository.findAllWithCategory(spec, pageable);

        List<Integer> bookIds = booksPage.getContent().stream()
                .map(Book::getId)
                .toList();

        List<Object[]> purchaseCounts = bookRepository.getPurchaseCountsByBookIds(bookIds);



        Map<Integer, Long> purchaseCountMap = purchaseCounts.stream()
                .collect(Collectors.toMap(
                        result -> (Integer) result[0],
                        result -> (Long) result[1]
                ));

        List<BookResponse> bookResponses = booksPage.getContent().stream()
                .map(book -> {
                    BookResponse response = modelMapper.map(book, BookResponse.class);
                    response.setPurchaseCount(purchaseCountMap.getOrDefault(book.getId(), 0L));
                    return response;
                })
                .toList();

        return new PageImpl<>(bookResponses, pageable, booksPage.getTotalElements());
    }


    @Transactional(readOnly = true)
    public BigDecimal getAverageRatingById(Integer bookID) {
        return bookRepository.getAverageRatingById(bookID);
    }

    @Transactional(readOnly = true)
    public BookResponse getBookById(Integer bookID) {
        Book book = bookRepository.findById(bookID).orElseThrow(
                () -> new DataNotFoundException("Book not found")
        );

        return modelMapper.map(book, BookResponse.class);
    }

    @Transactional
    public void descQuantity(Integer bookID, Integer quantity) {
        Book book = bookRepository.findById(bookID).orElseThrow(
                () -> new DataNotFoundException("Book not found")
        );

        book.setQuantity(book.getQuantity() - quantity);

        bookRepository.save(book);
    }
}
