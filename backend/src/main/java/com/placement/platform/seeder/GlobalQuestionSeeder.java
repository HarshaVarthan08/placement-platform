package com.placement.platform.seeder;

import com.placement.platform.entity.GlobalQuestion;
import com.placement.platform.entity.InterviewDifficulty;
import com.placement.platform.repository.GlobalQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GlobalQuestionSeeder implements CommandLineRunner {

    private final GlobalQuestionRepository globalQuestionRepository;

    public GlobalQuestionSeeder(GlobalQuestionRepository globalQuestionRepository) {
        this.globalQuestionRepository = globalQuestionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (globalQuestionRepository.count() == 0) {
            seedGlobalQuestions();
        }
    }

    private void seedGlobalQuestions() {
        List<GlobalQuestion> questions = new ArrayList<>();

        // ==========================================
        // 1. JAVA CORE (10 Questions)
        // ==========================================
        questions.add(createQuestion(
            "Explain the difference between final, finally, and finalize in Java.",
            "final is a keyword to declare constants, prevent method overriding, and prevent inheritance. finally is a block used in exception handling that always executes. finalize is a deprecated method in Object class invoked by the GC before destroying an object.",
            "Java Core",
            InterviewDifficulty.EASY,
            Arrays.asList("final is for constants/inheritance prevention", "finally is for resource cleanup in exception blocks", "finalize is GC hook prior to destruction"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Java", "OOP")
        ));

        questions.add(createQuestion(
            "What is the difference between abstract class and interface in Java 8+?",
            "Abstract classes can have state (instance variables) and constructors, whereas interfaces cannot. Interfaces support multiple inheritance. Java 8 added default/static methods and Java 9 added private methods to interfaces.",
            "Java Core",
            InterviewDifficulty.EASY,
            Arrays.asList("Abstract classes have state/constructors", "Interfaces support multiple inheritance", "Java 8+ interfaces have default/static methods"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Java", "OOP")
        ));

        questions.add(createQuestion(
            "How does Java's HashMap handle collisions internally?",
            "HashMap uses chaining in bucket slots. When objects hash to the same bucket, they form a linked list. In Java 8, if a bucket size exceeds 8 and overall map size is at least 64, the linked list is treeified into a Red-Black Tree.",
            "Java Core",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Bucket array chaining", "Java 8 treeification with Red-Black tree", "Threshold is 8 elements"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("Java", "Collections")
        ));

        questions.add(createQuestion(
            "What is the difference between fail-fast and fail-safe iterators in Java?",
            "Fail-fast iterators throw ConcurrentModificationException immediately if collection is modified structurally during iteration. Fail-safe iterators work on a clone or view of the collection and do not throw exceptions.",
            "Java Core",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Fail-fast throws ConcurrentModificationException", "Fail-safe operates on a clone/view", "Example: ArrayList vs CopyOnWriteArrayList"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("Java", "Collections")
        ));

        questions.add(createQuestion(
            "Explain the volatile keyword and its use case in Java concurrency.",
            "volatile ensures thread visibility of variable changes by reading/writing directly to/from main memory instead of thread-local CPU registers, preventing visibility issues. It also prevents instruction reordering.",
            "Java Core",
            InterviewDifficulty.HARD,
            Arrays.asList("Guarantees CPU cache visibility", "Prevents instruction reordering", "Does not provide atomicity"),
            Arrays.asList("Senior Software Engineer", "Backend Developer"),
            Arrays.asList("Java", "Concurrency")
        ));

        questions.add(createQuestion(
            "What is the Java Memory Model and how does it relate to garbage collection?",
            "JMM divides memory into Stack (thread local) and Heap (shared objects). Heap is split into Young Generation (Eden, survivor spaces) and Old Generation. Garbage collection frees up unreferenced Heap memory using minor/major cycles.",
            "Java Core",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Stack stores local frames, Heap stores objects", "Young vs Old Generation divisions", "GC sweeps unused references in heap"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("Java", "Memory Management")
        ));

        questions.add(createQuestion(
            "Explain ThreadLocal class and its typical use cases in backend applications.",
            "ThreadLocal provides thread-local variables. Each thread accessing the ThreadLocal has its own, independently initialized copy. It is commonly used for user session contexts, transaction IDs, or thread-safe database connections.",
            "Java Core",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Thread-isolated storage", "Avoids synchronization overhead", "Risk of memory leaks if not cleaned up in thread pools"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("Java", "Concurrency")
        ));

        questions.add(createQuestion(
            "What are Java Generics and what is type erasure?",
            "Generics enforce compile-time type safety. Type erasure is the compiler process where generic type parameters are replaced with their bounds or Object during compilation, ensuring backwards compatibility with older JVM versions.",
            "Java Core",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Compile-time type safety", "Type erasure replaces parameters with Object/bounds", "No generic runtime type information"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Java", "Generics")
        ));

        questions.add(createQuestion(
            "Explain Java Reflection API and its pros/cons.",
            "Reflection allows inspection and modification of classes, methods, fields, and constructors at runtime. Pros: flexibility, frameworks (Spring). Cons: performance overhead, breaks encapsulation, lacks compile-time safety.",
            "Java Core",
            InterviewDifficulty.HARD,
            Arrays.asList("Runtime class inspection/manipulation", "Bypasses access controls (private)", "Performance overhead and security risks"),
            Arrays.asList("Senior Software Engineer", "Backend Developer"),
            Arrays.asList("Java", "Reflection")
        ));

        questions.add(createQuestion(
            "What is the difference between String, StringBuilder, and StringBuffer in Java?",
            "String is immutable and stored in the string pool. StringBuilder is mutable but not thread-safe. StringBuffer is mutable and thread-safe due to synchronized methods, making it slower than StringBuilder.",
            "Java Core",
            InterviewDifficulty.EASY,
            Arrays.asList("String immutability and pool caching", "StringBuilder is mutable and non-synchronized", "StringBuffer is synchronized and thread-safe"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Java", "Strings")
        ));

        // ==========================================
        // 2. SPRING FRAMEWORK & SPRING BOOT (10 Questions)
        // ==========================================
        questions.add(createQuestion(
            "What is Dependency Injection and how does Spring support it?",
            "Dependency Injection is a design pattern realizing Inversion of Control. Instead of creating dependencies manually, Spring IoC container creates and injects them via constructor injection, setter injection, or field injection.",
            "Spring Framework",
            InterviewDifficulty.EASY,
            Arrays.asList("Inversion of Control concept", "Spring container manages bean lifecycle", "Constructor vs Setter vs Field injection"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Spring Boot", "Dependency Injection")
        ));

        questions.add(createQuestion(
            "Explain the difference between Spring bean scopes.",
            "Default scope is singleton (one instance per container). Prototype scope creates a new instance on every request. Web scopes include request (per HTTP request), session (per HTTP session), and application (per ServletContext).",
            "Spring Framework",
            InterviewDifficulty.EASY,
            Arrays.asList("Singleton is default scope", "Prototype creates new instances", "Web scopes: request, session, application"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Spring Boot", "Spring Core")
        ));

        questions.add(createQuestion(
            "What is Aspect-Oriented Programming (AOP) in Spring?",
            "AOP modules cross-cutting concerns like logging, security, or transactions away from business logic. It works by creating proxies around target beans and applying advice (actions) at specified join points (execution interceptors).",
            "Spring Framework",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Cross-cutting concerns separation", "Aspects, JoinPoints, Pointcuts, and Advice", "JDK dynamic proxies vs CGLIB proxying"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("Spring Core", "AOP")
        ));

        questions.add(createQuestion(
            "How does Spring Boot's auto-configuration work internally?",
            "Spring Boot scans classpath dependencies and automatically registers beans based on `@Conditional` annotations (e.g. `@ConditionalOnClass`, `@ConditionalOnMissingBean`) declared in configurations defined in `spring.factories`.",
            "Spring Boot",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("@EnableAutoConfiguration starts the mechanism", "Scans META-INF/spring.factories or imports configuration classes", "@Conditional annotations decide bean loading"),
            Arrays.asList("Backend Developer", "Spring Developer"),
            Arrays.asList("Spring Boot", "Configuration")
        ));

        questions.add(createQuestion(
            "What is Spring Boot Actuator and what is it used for?",
            "Actuator provides production-ready features for monitoring and managing applications. It exposes HTTP/JMX endpoints to check health metrics, thread dumps, environment properties, and HTTP trace details.",
            "Spring Boot",
            InterviewDifficulty.EASY,
            Arrays.asList("Application health monitoring endpoints (/actuator/health)", "Metrics and telemetry (/actuator/metrics)", "Security of actuator endpoints"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("Spring Boot", "DevOps")
        ));

        questions.add(createQuestion(
            "Explain `@Transactional` annotation in Spring and how transaction propagation works.",
            "@Transactional proxies class execution inside database transactions. Propagation defines boundary behavior (e.g. REQUIRED reuse/start, REQUIRES_NEW always start new, NESTED run in savepoint transaction).",
            "Spring Framework",
            InterviewDifficulty.HARD,
            Arrays.asList("Proxy-based transaction boundary", "Propagation levels: REQUIRED, REQUIRES_NEW", "Rollback rules (default on runtime exceptions only)"),
            Arrays.asList("Senior Software Engineer", "Backend Developer"),
            Arrays.asList("Spring Data", "Transactions")
        ));

        questions.add(createQuestion(
            "What is the difference between JPA and Hibernate?",
            "JPA (Java Persistence API) is a specification/standard interface detailing Object-Relational Mapping. Hibernate is a concrete, widely used implementation framework of the JPA specification.",
            "Spring Data",
            InterviewDifficulty.EASY,
            Arrays.asList("JPA is specification/standard", "Hibernate is vendor implementation", "EntityManager (JPA) wraps Session (Hibernate)"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("JPA", "Hibernate")
        ));

        questions.add(createQuestion(
            "Explain Lazy Loading vs Eager Loading in JPA/Hibernate and how to avoid N+1 queries.",
            "Eager loading fetches associations immediately. Lazy loading defers loading until accessed (creates proxy). N+1 occurs when fetching parent list triggers separate queries for child. Avoid via JOIN FETCH or Entity Graphs.",
            "Spring Data",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Lazy proxy instantiation", "N+1 query problem definition", "Join fetch / EntityGraph solution"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("JPA", "Hibernate")
        ));

        questions.add(createQuestion(
            "What is the purpose of Spring Security filter chain?",
            "Spring Security is a chain of Servlet Filters. HTTP requests pass through filters (e.g., JwtFilter, UsernamePasswordAuthenticationFilter) to perform authentication, authorization checks, CORS/CSRF protection, and error handling.",
            "Spring Security",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("SecurityFilterChain configuration bean", "Filters executed sequentially", "Throws AccessDeniedException or AuthenticationException"),
            Arrays.asList("Backend Developer", "Security Engineer"),
            Arrays.asList("Spring Security", "Security")
        ));

        questions.add(createQuestion(
            "How does JWT-based authentication work in Spring Boot apps?",
            "A client sends credentials, the backend validates them and generates a signed JWT token containing claims. The client stores it and attaches it to the Authorization header in subsequent requests. A custom filter intercepts and validates it.",
            "Spring Security",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Stateless session management", "Token validation using signatures", "Filter extracts token from Authorization header"),
            Arrays.asList("Backend Developer", "Software Engineer"),
            Arrays.asList("Spring Security", "JWT")
        ));

        // ==========================================
        // 3. WEB & REST APIs (8 Questions)
        // ==========================================
        questions.add(createQuestion(
            "What are key principles of REST API design?",
            "REST (Representational State Transfer) is stateless, client-server based, cacheable, structured in a uniform interface using HTTP methods (GET, POST, PUT, DELETE) representing CRUD, and resources are named using nouns.",
            "Web Development",
            InterviewDifficulty.EASY,
            Arrays.asList("Stateless communication", "Resource-based URI naming with nouns", "Use of standard HTTP status codes and verbs"),
            Arrays.asList("Software Engineer", "Frontend Developer"),
            Arrays.asList("REST API", "HTTP")
        ));

        questions.add(createQuestion(
            "What is the difference between PUT and PATCH HTTP methods?",
            "PUT is used to replace or overwrite a resource completely (idempotent). PATCH is used to apply partial modifications or updates to a resource (typically non-idempotent).",
            "Web Development",
            InterviewDifficulty.EASY,
            Arrays.asList("PUT replaces entire resource", "PATCH updates specific fields", "Idempotency differences"),
            Arrays.asList("Software Engineer", "Web Developer"),
            Arrays.asList("REST API", "HTTP")
        ));

        questions.add(createQuestion(
            "Explain CORS (Cross-Origin Resource Sharing) and how browsers enforce it.",
            "CORS is a security mechanism restricting web pages from making requests to a different domain. Browsers check access headers returned by servers (Access-Control-Allow-Origin) and send preflight OPTIONS requests for unsafe actions.",
            "Web Development",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Same-Origin Policy protection", "Preflight OPTIONS request validation", "Response headers define allowed origins/methods"),
            Arrays.asList("Web Developer", "Software Engineer"),
            Arrays.asList("CORS", "Security")
        ));

        questions.add(createQuestion(
            "What is statelessness in REST APIs and what are its benefits?",
            "Statelessness means the server does not store client session state. Each request must contain all context needed to process it. Benefits: scales horizontally, simplifies server logic, improves caching.",
            "Web Development",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("No server-side session state storage", "Each request contains credentials/context", "Scales easily horizontally"),
            Arrays.asList("Backend Developer", "System Architect"),
            Arrays.asList("REST API", "System Design")
        ));

        questions.add(createQuestion(
            "What is the purpose of HTTP Status Code classes (1xx to 5xx)?",
            "1xx are informational. 2xx represent success (e.g. 200 OK, 201 Created). 3xx are redirection codes. 4xx represent client-side errors (e.g. 400 Bad Request, 401 Unauthorized, 404 Not Found). 5xx are server-side errors.",
            "Web Development",
            InterviewDifficulty.EASY,
            Arrays.asList("Status classes represent response types", "Client error (400s) vs Server error (500s)", "Success codes (200s)"),
            Arrays.asList("Software Engineer", "Web Developer"),
            Arrays.asList("HTTP", "Web")
        ));

        questions.add(createQuestion(
            "How do you secure REST APIs from Web vulnerabilities?",
            "Apply SSL/HTTPS, enforce authentication (JWT/OAuth), authorize with roles, validate and sanitize all inputs to prevent SQL Injection and XSS, configure CORS, and apply Rate Limiting.",
            "Web Development",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Transport encryption via HTTPS", "Input validation and escaping", "Authentication & Rate Limiting protection"),
            Arrays.asList("Security Analyst", "Backend Developer"),
            Arrays.asList("Security", "OWASP")
        ));

        questions.add(createQuestion(
            "What is Content Negotiation in HTTP?",
            "Content negotiation allows clients and servers to agree on the format (media type, language, encoding) of the response data. It is controlled using headers like `Accept`, `Accept-Language`, and `Content-Type`.",
            "Web Development",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Agreement on representation media type", "Accept header defines client preference", "Content-Type defines returned representation"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("HTTP", "REST API")
        ));

        questions.add(createQuestion(
            "Explain WebSockets and how they differ from HTTP polling.",
            "WebSockets provide full-duplex, bidirectional communication over a single long-lived TCP connection. HTTP polling repeatedly requests updates, creating overhead. WebSockets enable instant real-time push messages.",
            "Web Development",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Persistent full-duplex TCP channel", "Lower overhead compared to HTTP polling", "Good for real-time applications"),
            Arrays.asList("Software Engineer", "Web Developer"),
            Arrays.asList("WebSockets", "HTTP")
        ));

        // ==========================================
        // 4. DATABASES & SQL (10 Questions)
        // ==========================================
        questions.add(createQuestion(
            "Explain database indexes and how they speed up search queries.",
            "An index is a data structure (typically a B+ Tree) that stores pointer references to table rows. It allows the database engine to find matches in logarithmic logarithmic time O(log N) instead of scanning the full table O(N).",
            "Database",
            InterviewDifficulty.EASY,
            Arrays.asList("B-Tree / B+Tree indexing structures", "Avoids Full Table Scans", "Write overhead penalty"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("SQL", "Indexing")
        ));

        questions.add(createQuestion(
            "What is the difference between Clustered and Non-Clustered Indexes?",
            "A Clustered Index dictates the physical order of data rows stored on disk (only one allowed, usually PK). A Non-Clustered Index contains pointers/logical keys pointing to the actual data pages (multiple allowed).",
            "Database",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Clustered index stores data pages directly in leaf nodes", "Non-clustered index stores references to row locators", "Count constraints (1 clustered, multiple non-clustered)"),
            Arrays.asList("Database Administrator", "Backend Developer"),
            Arrays.asList("SQL", "Indexing")
        ));

        questions.add(createQuestion(
            "What are database Transaction Isolation Levels and anomalies they prevent?",
            "Isolation levels define visibility during concurrent transactions. Levels: Read Uncommitted (dirty reads allowed), Read Committed (prevents dirty reads), Repeatable Read (prevents non-repeatable reads), Serializable (prevents phantom reads).",
            "Database",
            InterviewDifficulty.HARD,
            Arrays.asList("Concurrency anomalies: Dirty Read, Non-repeatable Read, Phantom Read", "Standard Isolation Levels in SQL databases", "Performance/Locking trade-offs"),
            Arrays.asList("Senior Software Engineer", "Backend Developer"),
            Arrays.asList("SQL", "Transactions")
        ));

        questions.add(createQuestion(
            "Explain ACID properties in relational databases.",
            "ACID stands for Atomicity (all changes succeed or roll back), Consistency (rules/constraints are always maintained), Isolation (transactions run independently without interference), Durability (successful changes persist permanently).",
            "Database",
            InterviewDifficulty.EASY,
            Arrays.asList("Atomicity: all-or-nothing execution", "Consistency: valid database state transitions", "Isolation and Durability properties"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("SQL", "ACID")
        ));

        questions.add(createQuestion(
            "What is database Normalization and what are 1NF, 2NF, and 3NF?",
            "Normalization organizes schemas to eliminate data redundancy and anomalies. 1NF: Atomic values only. 2NF: 1NF + no partial dependencies (on composite keys). 3NF: 2NF + no transitive dependencies.",
            "Database",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Minimizes data anomalies and redundancy", "1NF (Atomic), 2NF (No partial key dependencies)", "3NF (No transitive dependencies)"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("SQL", "Database Design")
        ));

        questions.add(createQuestion(
            "What is the difference between SQL and NoSQL databases?",
            "SQL databases are relational, structured (schemas), support transactions (ACID), and scale vertically. NoSQL databases are non-relational, document/key-value/graph-based, scale horizontally, and trade consistency for availability.",
            "Database",
            InterviewDifficulty.EASY,
            Arrays.asList("Structured tables vs Schema-less documents", "Scale vertically vs Scale horizontally", "ACID guarantees vs BASE characteristics"),
            Arrays.asList("Software Engineer", "Backend Developer"),
            Arrays.asList("SQL", "NoSQL")
        ));

        questions.add(createQuestion(
            "How does database connection pooling work and why is it used?",
            "A connection pool creates and maintains a cache of active database connections. Reusing existing connections avoids the high cost of TCP handshakes and database authentication on every request, improving performance.",
            "Database",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Pre-allocated pool of database sockets", "Saves connection setup/teardown overhead", "HikariCP is a popular Java connection pool"),
            Arrays.asList("Backend Developer", "Java Developer"),
            Arrays.asList("SQL", "Connection Pooling")
        ));

        questions.add(createQuestion(
            "Explain database Sharding and how it differs from Replication.",
            "Sharding splits a table horizontally and distributes rows across different database nodes. Replication duplicates the entire database on multiple nodes for read-scalability and high availability.",
            "Database",
            InterviewDifficulty.HARD,
            Arrays.asList("Horizontal partitioning across nodes", "Sharding key selection is critical", "Replication is duplication for read scaling/backups"),
            Arrays.asList("Senior Backend Developer", "System Architect"),
            Arrays.asList("System Design", "Sharding")
        ));

        questions.add(createQuestion(
            "What is the difference between INNER JOIN, LEFT JOIN, and outer joins in SQL?",
            "INNER JOIN returns records with matching keys in both tables. LEFT JOIN returns all records from left table, plus matched records from right. FULL OUTER JOIN returns all records when there is a match in either.",
            "Database",
            InterviewDifficulty.EASY,
            Arrays.asList("INNER JOIN: intersection matching only", "LEFT JOIN: keeps all left table rows", "Null padding for unmatched rows"),
            Arrays.asList("Software Engineer", "Data Analyst"),
            Arrays.asList("SQL", "Joins")
        ));

        questions.add(createQuestion(
            "What is a database deadlock and how can it be avoided?",
            "A deadlock occurs when two transactions hold locks needed by each other, causing both to wait indefinitely. Avoid by accessing tables in a consistent order, keeping transactions short, and using appropriate isolation levels.",
            "Database",
            InterviewDifficulty.HARD,
            Arrays.asList("Circular wait condition for resources", "Detection and automatic rollback by DB engine", "Acquire locks in consistent order to prevent"),
            Arrays.asList("Senior Software Engineer", "Database Administrator"),
            Arrays.asList("SQL", "Concurrency")
        ));

        // ==========================================
        // 5. SYSTEM DESIGN & MICROSERVICES (8 Questions)
        // ==========================================
        questions.add(createQuestion(
            "Explain the CAP Theorem and its implications.",
            "The CAP Theorem states a distributed system can guarantee at most two out of: Consistency (all nodes see same data), Availability (every request receives success/error response), and Partition Tolerance (system continues running despite node isolation).",
            "System Design",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Consistency vs Availability under partitions", "Partition tolerance is mandatory in networks", "Choosing CP vs AP configurations"),
            Arrays.asList("Backend Developer", "System Architect"),
            Arrays.asList("CAP Theorem", "Distributed Systems")
        ));

        questions.add(createQuestion(
            "What is a Load Balancer and what algorithms are commonly used?",
            "A Load Balancer distributes incoming network traffic across a cluster of servers to improve availability and response times. Algorithms: Round Robin, Least Connections, IP Hash, and Weighted Round Robin.",
            "System Design",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Single entry point distributing network requests", "Algorithms: Round Robin, Least Connections", "Hardware vs Software load balancing (e.g. Nginx)"),
            Arrays.asList("DevOps Engineer", "System Architect"),
            Arrays.asList("Load Balancing", "Infrastructure")
        ));

        questions.add(createQuestion(
            "Explain the Circuit Breaker pattern in microservices.",
            "Circuit Breaker prevents cascading failures in microservices. If calls to a downstream service fail repeatedly, the breaker opens, immediately failing subsequent calls with a fallback response, letting the service recover.",
            "System Design",
            InterviewDifficulty.HARD,
            Arrays.asList("States: Closed, Open, Half-Open", "Prevents cascading outages", "Resilience4j is a popular Java library"),
            Arrays.asList("Senior Software Engineer", "Microservices Developer"),
            Arrays.asList("Microservices", "Design Patterns")
        ));

        questions.add(createQuestion(
            "What is a CDN (Content Delivery Network) and how does it work?",
            "A CDN is a geographically distributed network of proxy servers that cache static assets (images, CSS, JS, videos) close to user locations, reducing latency, load times, and bandwidth usage on origin servers.",
            "System Design",
            InterviewDifficulty.EASY,
            Arrays.asList("Geographical caching of static content", "Edge servers serve content to local users", "Reduces origin server workload"),
            Arrays.asList("Software Engineer", "Frontend Developer"),
            Arrays.asList("CDN", "Caching")
        ));

        questions.add(createQuestion(
            "Explain horizontal scaling vs vertical scaling.",
            "Horizontal scaling (scaling out) adds more machines/nodes to the pool. Vertical scaling (scaling up) adds more power (CPU, RAM, Storage) to an existing machine. Horizontal scaling is more scalable and resilient.",
            "System Design",
            InterviewDifficulty.EASY,
            Arrays.asList("Scale out (more servers) vs Scale up (stronger server)", "Horizontal requires load balancing and distributed state", "Vertical has physical limits and single points of failure"),
            Arrays.asList("Software Engineer", "System Architect"),
            Arrays.asList("Scalability", "Infrastructure")
        ));

        questions.add(createQuestion(
            "What is an API Gateway in microservices architecture?",
            "An API Gateway is a reverse proxy acting as a single entry point for client requests. It handles routing, authentication, authorization, rate limiting, request composition, and SSL termination.",
            "System Design",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Single entry point routing requests to internal services", "Centralized security: SSL termination, CORS, Auth", "Cross-cutting features offloaded from business services"),
            Arrays.asList("Microservices Developer", "System Architect"),
            Arrays.asList("Microservices", "Gateway")
        ));

        questions.add(createQuestion(
            "What is the difference between synchronous and asynchronous microservices communication?",
            "Synchronous (e.g. HTTP, gRPC) blocks the caller thread waiting for a response, creating tight coupling. Asynchronous (e.g. Kafka, RabbitMQ) returns immediately and broadcasts messages, reducing coupling.",
            "System Design",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Synchronous: direct request-response blocking", "Asynchronous: event-driven, non-blocking via queues", "Tuning trade-offs: latency vs decoupling"),
            Arrays.asList("Backend Developer", "System Architect"),
            Arrays.asList("Microservices", "Messaging")
        ));

        questions.add(createQuestion(
            "Explain Database Replication (Leader-Follower) and its write/read distribution.",
            "Leader-Follower replication sends all writes to a primary leader node, which propagates logs to secondary follower nodes. Followers handle all read operations, scaling read performance.",
            "System Design",
            InterviewDifficulty.HARD,
            Arrays.asList("Writes processed by Leader only", "Followers sync writes asynchronously or synchronously", "Enables scaling reads but introduces replication lag"),
            Arrays.asList("Senior Software Engineer", "System Architect"),
            Arrays.asList("Distributed Systems", "Replication")
        ));

        // ==========================================
        // 6. DATA STRUCTURES & ALGORITHMS (6 Questions)
        // ==========================================
        questions.add(createQuestion(
            "What is Big O notation and why is it important?",
            "Big O notation mathematically describes the upper bound performance / time-space complexity of an algorithm in the worst-case scenario relative to the input size N.",
            "Data Structures & Algorithms",
            InterviewDifficulty.EASY,
            Arrays.asList("Worst-case runtime complexity indicator", "Scales with input size N", "Helps analyze performance scalability"),
            Arrays.asList("Software Engineer", "Computer Scientist"),
            Arrays.asList("Algorithms", "Complexity")
        ));

        questions.add(createQuestion(
            "Explain the difference between Array and LinkedList data structures.",
            "Arrays store elements in contiguous memory (random access is O(1), insertions/deletions are O(N)). LinkedLists store pointers to non-contiguous elements (access is O(N), insertions/deletions at pointer are O(1)).",
            "Data Structures & Algorithms",
            InterviewDifficulty.EASY,
            Arrays.asList("Array has contiguous memory allocation", "LinkedList has node-pointer references", "Time complexity: random access vs insertion/deletion"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Data Structures", "Algorithms")
        ));

        questions.add(createQuestion(
            "What is a Binary Search Tree (BST) and its search complexity?",
            "A BST is a binary tree where left children are smaller and right children are larger than the parent node. Average search time is O(log N). Worst case is O(N) if the tree is skewed (linear list).",
            "Data Structures & Algorithms",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Sorted left < root < right binary tree structure", "O(log N) average search time", "Worst-case O(N) if unbalanced"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Data Structures", "Trees")
        ));

        questions.add(createQuestion(
            "Explain the difference between BFS and DFS traversal algorithms on graphs.",
            "BFS (Breadth-First Search) explores neighbors layer-by-layer using a Queue. DFS (Depth-First Search) explores paths as deep as possible before backtracking using a Stack or recursion.",
            "Data Structures & Algorithms",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("BFS uses Queue for layer exploration", "DFS uses Stack/recursion for depth exploration", "Complexity: O(V + E) for both"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Algorithms", "Graphs")
        ));

        questions.add(createQuestion(
            "What is Dynamic Programming and how does it differ from Divide and Conquer?",
            "Dynamic Programming solves complex problems by breaking them into overlapping subproblems, solving them once, and caching results (memoization/tabulation). Divide and conquer splits problems into independent subproblems.",
            "Data Structures & Algorithms",
            InterviewDifficulty.HARD,
            Arrays.asList("Overlapping subproblems + optimal substructure properties", "Memoization (top-down) vs Tabulation (bottom-up)", "Divide & Conquer subproblems do not overlap (e.g. MergeSort)"),
            Arrays.asList("Software Engineer", "Algorithm Engineer"),
            Arrays.asList("Algorithms", "Dynamic Programming")
        ));

        questions.add(createQuestion(
            "Explain how a MergeSort algorithm works and its time/space complexity.",
            "MergeSort is a divide-and-conquer algorithm. It recursively splits the array in halves, sorts each half recursively, and merges sorted halves back. Time complexity: O(N log N) in all cases. Space complexity: O(N).",
            "Data Structures & Algorithms",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Divide-and-conquer recursively splitting array", "Merge step combines sorted sub-arrays", "Time complexity O(N log N), Space O(N)"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Algorithms", "Sorting")
        ));

        // ==========================================
        // 7. BEHAVIORAL & PROFESSIONAL (8 Questions)
        // ==========================================
        questions.add(createQuestion(
            "How do you handle conflict or disagreement within your development team?",
            "Acknowledge the dispute objectively, discuss differences politely with a focus on data/facts rather than emotions, seek compromises aligned with business goals, and escalate only if alignment cannot be achieved.",
            "Behavioral",
            InterviewDifficulty.EASY,
            Arrays.asList("Professional and open communication", "Data-driven objective decision making", "Compromise for team progress"),
            Arrays.asList("Software Engineer", "Project Manager"),
            Arrays.asList("Behavioral", "Teamwork")
        ));

        questions.add(createQuestion(
            "Describe a time when you had to work under tight deadlines with high pressure.",
            "Prioritize critical path deliverables using MVP concepts, communicate risks early to stakeholders, eliminate non-essential meetings, and coordinate tasks with team members to deliver core functionality on time.",
            "Behavioral",
            InterviewDifficulty.EASY,
            Arrays.asList("Priority management (critical path vs nice-to-have)", "Stakeholder communication and transparency", "Task coordination and focus"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Behavioral", "Professionalism")
        ));

        questions.add(createQuestion(
            "How do you handle scope creep or changing requirements mid-sprint?",
            "Evaluate impact on current sprint commitment, discuss trade-offs with Product Owner, document changes in ticket, and swap tasks out of the sprint backlog if new high-priority items must be added.",
            "Behavioral",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Impact analysis on sprint commitment", "Collaborate with Product Owner on prioritization", "Swap tasks to maintain velocity budget"),
            Arrays.asList("Scrum Master", "Software Developer"),
            Arrays.asList("Behavioral", "Agile")
        ));

        questions.add(createQuestion(
            "What do you do when you realize you won't be able to deliver a task on time?",
            "Communicate immediately to the team and lead, explain the blockers clearly, propose a revised timeline, and ask for assistance or look to descoping optional requirements to meet the milestone.",
            "Behavioral",
            InterviewDifficulty.EASY,
            Arrays.asList("Proactive communication instead of hiding delay", "Clear explanation of blocker reasons", "Propose actionable solutions or fallback plans"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Behavioral", "Communication")
        ));

        questions.add(createQuestion(
            "How do you explain complex technical details to non-technical stakeholders?",
            "Avoid technical jargon, use real-world analogies, focus on business outcomes and user impact, and use visual aid/diagrams to make concepts intuitive.",
            "Behavioral",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Translate code to business value/impact", "Use intuitive analogies", "Feedback loops to ensure understanding"),
            Arrays.asList("Software Engineer", "Tech Lead"),
            Arrays.asList("Behavioral", "Communication")
        ));

        questions.add(createQuestion(
            "What is your approach to code reviews and receiving feedback?",
            "View reviews as constructive pair programming. Focus on coding standards and correctness without making personal remarks. Welcome feedback with an open mind to learn and discuss differences objectively.",
            "Behavioral",
            InterviewDifficulty.EASY,
            Arrays.asList("Constructive, respectful review style", "Separating identity from code changes", "Opportunities for shared knowledge sharing"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Behavioral", "Teamwork")
        ));

        questions.add(createQuestion(
            "Describe how you handle legacy code that lacks documentation and tests.",
            "Read code path carefully, write unit tests to lock down current behavior (characterization tests), refactor in small increments, document discoveries, and verify changes via integration tests.",
            "Behavioral",
            InterviewDifficulty.MEDIUM,
            Arrays.asList("Write tests to discover/lock current behavior first", "Refactor incrementally to avoid breaking changes", "Document code paths and architecture for future developers"),
            Arrays.asList("Software Engineer", "Tech Lead"),
            Arrays.asList("Behavioral", "Legacy Code")
        ));

        questions.add(createQuestion(
            "How do you stay updated with the latest trends and technologies in software development?",
            "Read industry blogs (Tech blogs, Medium, InfoQ), participate in developer forums (Reddit, StackOverflow), take online courses, contribute to open-source or build side projects to practice new skills.",
            "Behavioral",
            InterviewDifficulty.EASY,
            Arrays.asList("Consistent reading habits", "Practical experimentation in side projects", "Community participation"),
            Arrays.asList("Software Engineer", "Developer"),
            Arrays.asList("Behavioral", "Continuous Learning")
        ));

        globalQuestionRepository.saveAll(questions);
    }

    private GlobalQuestion createQuestion(
            String text,
            String idealAnswer,
            String topic,
            InterviewDifficulty difficulty,
            List<String> keyPoints,
            List<String> roles,
            List<String> skills) {
        GlobalQuestion q = new GlobalQuestion();
        q.setQuestion(text);
        q.setIdealAnswer(idealAnswer);
        q.setTopic(topic);
        q.setDifficulty(difficulty);
        q.setKeyPoints(keyPoints);
        q.setApplicableRoles(roles);
        q.setSkillTags(skills);
        return q;
    }
}
