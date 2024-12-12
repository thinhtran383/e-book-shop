package org.example.bookshop.repositories.elasticsearch;

import org.example.bookshop.domain.elasticsearch.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IBookDocumentRepository extends ElasticsearchRepository<BookDocument, Integer> {

    Page<BookDocument> findByTitleContainingIgnoreCase(String title, Pageable pageable);


}
