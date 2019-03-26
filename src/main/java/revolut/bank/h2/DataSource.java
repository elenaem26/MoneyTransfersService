package revolut.bank.h2;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

    @Inject
    @Named("db.url")
    private String DB_URL;

    @Inject
    @Named("db.user")
    private String DB_USER;

    @Inject
    @Named("db.password")
    private String DB_PASSWORD;

    @Inject(optional = true)
    @Named("hikari.poolsize")
    private int MAX_POOL_SIZE = 3;

    private HikariDataSource ds;

    @Inject
    private void init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        ds = new HikariDataSource(config);
    }

    public javax.sql.DataSource getDataSource() {
        return ds;
    }
}
