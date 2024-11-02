package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookshop.dto.book.BookDto;
import org.example.bookshop.dto.book.UpdateBookDto;
import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.Category;
import org.example.bookshop.exceptions.DataNotFoundException;
import org.example.bookshop.repositories.IBookRepository;
import org.example.bookshop.repositories.ICategoryRepository;
import org.example.bookshop.responses.book.BookResponse;
import org.example.bookshop.responses.book.PublisherResponse;
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

    @Transactional(readOnly = true)
    public Page<PublisherResponse> getPublishers(Pageable pageable) {
        return bookRepository.findAllPublisher(pageable);
    }


    @Transactional
    public BookResponse createNewBook(BookDto bookDto) {
        Book book = modelMapper.map(bookDto, Book.class);

        book.setId(null);

        Category category = categoryRepository.findById(bookDto.getCategoryID())
                .orElseThrow(() -> new DataNotFoundException("Category not found"));

        String imageUrl = cloudinaryService.upload(bookDto.getImage());

        book.setCategoryID(category);
        book.setImage(imageUrl);
        book.setAverageRating(BigDecimal.ZERO);


        book = bookRepository.save(book);

        return BookResponse.builder()
                .image(imageUrl)
                .author(book.getAuthor())
                .categoryName(book.getCategoryID().getCategoryName())
                .description(book.getDescription())
                .id(book.getId())
                .price(book.getPrice())
                .publisher(book.getPublisher())
                .quantity(book.getQuantity())
                .title(book.getTitle())
                .averageRating(BigDecimal.ZERO)
                .publishedDate(book.getPublishedDate())
                .build();
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
    public Page<BookResponse> filterBooks(Integer category, BigDecimal priceMin, BigDecimal priceMax, String publisher, String title, Pageable pageable) {
        Specification<Book> spec = Specification.where(BookSpecification.hasCategory(category))
                .and(BookSpecification.hasPriceGreaterThan(priceMin))
                .and(BookSpecification.hasPriceLessThan(priceMax))
                .and(BookSpecification.hasPublisher(publisher))
                .and(BookSpecification.hasTitle(title));


        Page<Book> booksPage = bookRepository.findAll(spec, pageable);

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

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .quantity(book.getQuantity())
                .categoryName(book.getCategoryID() == null ? null : book.getCategoryID().getCategoryName())
                .description(book.getDescription())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .image(book.getImage())
                .averageRating(book.getAverageRating())
                .purchaseCount(bookRepository.getPurchaseCountByBookID(bookID))
                .build();
    }

    @Transactional
    public void descQuantity(Integer bookID, Integer quantity) {
        Book book = bookRepository.findById(bookID).orElseThrow(
                () -> new DataNotFoundException("Book not found")
        );

        book.setQuantity(book.getQuantity() - quantity);

        bookRepository.save(book);
    }

    @Transactional
    public void incQuantity(Integer bookID, Integer quantity) {
        Book book = bookRepository.findById(bookID).orElseThrow(
                () -> new DataNotFoundException("Book not found")
        );

        book.setQuantity(book.getQuantity() + quantity);

        bookRepository.save(book);
    }

    @Transactional
    public BookResponse updateBook(UpdateBookDto updateBookDto) {
        Book book = bookRepository.findById(updateBookDto.getId()).orElseThrow(
                () -> new DataNotFoundException("Book not found")
        );

        log.error("Update book: {}", updateBookDto);


        if (updateBookDto.getCategoryId() != null && updateBookDto.getCategoryId() != 0) {
            Category category = categoryRepository.findById(updateBookDto.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Category not found"));
            book.setCategoryID(category);
        }

        if (updateBookDto.getImage() != null && !updateBookDto.getImage().isEmpty()) {
            String imageUrl = cloudinaryService.upload(updateBookDto.getImage());
            book.setImage(imageUrl);
        }

        if (updateBookDto.getAuthor() != null && !updateBookDto.getAuthor().isBlank()) {
            book.setAuthor(updateBookDto.getAuthor());
        }

        if (updateBookDto.getDescription() != null && !updateBookDto.getDescription().isBlank()) {
            book.setDescription(updateBookDto.getDescription());
        }

        if (updateBookDto.getPublisher() != null && !updateBookDto.getPublisher().isBlank()) {
            book.setPublisher(updateBookDto.getPublisher());
        }


        if (updateBookDto.getQuantity() != null && updateBookDto.getQuantity() > 0) {
            book.setQuantity(updateBookDto.getQuantity());
        }


        if (updateBookDto.getTitle() != null) {
            book.setTitle(updateBookDto.getTitle());
        }

        if(updateBookDto.getPrice() != null && updateBookDto.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            book.setPrice(updateBookDto.getPrice());
        }


        book = bookRepository.save(book);

        return modelMapper.map(book, BookResponse.class);

    }

    @Transactional(readOnly = true)
    public int getBookQuantity(Integer bookID) {
        return bookRepository.getQuantityById(bookID);
    }




}
