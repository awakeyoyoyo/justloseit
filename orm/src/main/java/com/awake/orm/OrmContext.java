package com.awake.orm;

import com.awake.orm.accessor.IAccessor;
import com.awake.orm.manager.IOrmManager;
import com.awake.orm.query.IQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public class OrmContext {

    private static final Logger logger = LoggerFactory.getLogger(OrmContext.class);

    private static OrmContext instance;

    private ApplicationContext applicationContext;

    private IAccessor accessor;

    private IQuery query;

    private IOrmManager ormManager;

    private boolean stop = false;

    public static boolean isStop() {
        return instance.stop;
    }

    public static IOrmManager getOrmManager() {
        return null;
    }

    public static IAccessor getAccessor() {
        return null;
    }
}
