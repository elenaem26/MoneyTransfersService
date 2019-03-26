package revolut.bank.h2;

import com.google.inject.Inject;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class DSLContextFactory {

    @Inject
    private DataSource dataSource;

    public DSLContext getContext() {
        return DSL.using(dataSource.getDataSource(), SQLDialect.H2);
    }
}
