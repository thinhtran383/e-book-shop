package org.example.bookshop.domain.elasticsearch;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "books")
public class BookDocument {
    @Field(type = FieldType.Integer)
    private Integer id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Double)
    private BigDecimal price;

    @Field(type = FieldType.Integer)
    private Integer quantity;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String publisher;

    @Field(type = FieldType.Date)
    private LocalDate publishedDate;

    @Field(type = FieldType.Text)
    private String image;

    @Field(type = FieldType.Float)
    private BigDecimal averageRating;

    @Field(type = FieldType.Nested)
    private CategoryDocument category;
}
