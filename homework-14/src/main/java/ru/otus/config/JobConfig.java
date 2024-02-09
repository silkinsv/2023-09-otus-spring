package ru.otus.config;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.mappers.AuthorMapper;
import ru.otus.mappers.BookMapper;
import ru.otus.mappers.GenreMapper;
import ru.otus.models.documents.AuthorDocument;
import ru.otus.models.documents.BookDocument;
import ru.otus.models.documents.GenreDocument;
import ru.otus.models.entities.Author;
import ru.otus.models.entities.Book;
import ru.otus.models.entities.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.GenreRepository;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private final MongoTemplate mongoTemplate;

    private final EntityManagerFactory managerFactory;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    private final BookMapper bookMapper;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @Bean
    public Job migrateToMongoJob(Flow authorAndGenreMigrationFlow,
                                 Step bookMigrationStep) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(dropMongoDBStep())
                .next(authorAndGenreMigrationFlow)
                .next(bookMigrationStep)
                .next(dropMigrationIdsStep())
                .end()
                .build();
    }

    @Bean
    public TaskletStep dropMongoDBStep() {
        return new StepBuilder("dropMongoDBStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    mongoTemplate.getDb().drop();
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager).build();
    }

    @Bean
    public Flow authorAndGenreMigrationFlow(Flow authorMigrationFlow, Flow genreMigrationFlow) {
        return new FlowBuilder<SimpleFlow>("authorAndGenreMigrationFlow")
                .split(taskJobExecutor())
                .add(authorMigrationFlow, genreMigrationFlow)
                .build();
    }

    @Bean
    public TaskExecutor taskJobExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean
    public Flow authorMigrationFlow(Step authorMigrationStep) {
        return new FlowBuilder<SimpleFlow>("authorMigrationFlow")
                .start(authorMigrationStep)
                .build();
    }

    @Bean
    public Step authorMigrationStep(JpaPagingItemReader<Author> authorReader,
                                    ItemProcessor<Author, AuthorDocument> authorProcessor,
                                    MongoItemWriter<AuthorDocument> authorWriter) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<Author, AuthorDocument>chunk(3, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Author> authorReader() {
        return new JpaPagingItemReaderBuilder<Author>()
                .name("authorReader")
                .entityManagerFactory(managerFactory)
                .queryString("select a from Author a")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Author, AuthorDocument> authorProcessor() {
        return authorMapper::toDocument;
    }

    @StepScope
    @Bean
    public MongoItemWriter<AuthorDocument> authorWriter() {
        return new MongoItemWriterBuilder<AuthorDocument>()
                .collection("authors")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Flow genreMigrationFlow(Step genreMigrationStep) {
        return new FlowBuilder<SimpleFlow>("genreMigrationFlow")
                .start(genreMigrationStep)
                .build();
    }

    @Bean
    public Step genreMigrationStep(JpaPagingItemReader<Genre> genreReader,
                                    ItemProcessor<Genre, GenreDocument> genreProcessor,
                                    MongoItemWriter<GenreDocument> genreWriter) {
        return new StepBuilder("genreMigrationStep", jobRepository)
                .<Genre, GenreDocument>chunk(3, platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Genre> genreReader() {
        return new JpaPagingItemReaderBuilder<Genre>()
                .name("genreReader")
                .entityManagerFactory(managerFactory)
                .queryString("select g from Genre g")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Genre, GenreDocument> genreProcessor() {
        return genreMapper::toDocument;
    }

    @StepScope
    @Bean
    public MongoItemWriter<GenreDocument> genreWriter() {
        return new MongoItemWriterBuilder<GenreDocument>()
                .collection("genres")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step bookMigrationStep(JpaPagingItemReader<Book> bookReader,
                                  ItemProcessor<Book, BookDocument> bookProcessor,
                                  MongoItemWriter<BookDocument> bookWriter) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<Book, BookDocument>chunk(3, platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .build();
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Book> bookReader() {
        return new JpaPagingItemReaderBuilder<Book>()
                .name("bookReader")
                .entityManagerFactory(managerFactory)
                .queryString("select b from Book b")
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Book, BookDocument> bookProcessor() {
        return new ItemProcessor<Book, BookDocument>() {
            @Override
            public BookDocument process(Book book) throws Exception {
                return bookMapper.toDocument(book,
                        authorRepository.findFirstByMigrationId(book.getAuthor().getId()).get(),
                        genreRepository.findFirstByMigrationId(book.getGenre().getId()).get());
            }
        };
    }

    @StepScope
    @Bean
    public MongoItemWriter<BookDocument> bookWriter() {
        return new MongoItemWriterBuilder<BookDocument>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public TaskletStep dropMigrationIdsStep() {
        return new StepBuilder("dropMigrationIdsStep", jobRepository)
                .tasklet((stepContribution, chunkContext) -> {
                    var query = new Query();
                    var update = new Update().unset("migrationId");
                    mongoTemplate.findAndModify(query, update, GenreDocument.class);
                    mongoTemplate.findAndModify(query, update, AuthorDocument.class);
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
