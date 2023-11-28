package com.awake.orm.accessor;

import com.awake.orm.OrmContext;
import com.awake.orm.autoconfigure.OrmAutoTestConfiguration;
import com.awake.orm.entity.UserEntity;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.TransactionBody;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version : 1.0
 * @ClassName: TransactionTest
 * @Description: 必须要用复制集群才能使用事务
 * @Auther: awake
 * @Date: 2023/11/28 16:38
 **/
@SpringBootTest(classes = {OrmAutoTestConfiguration.class})
public class TransactionTest {

    @Test
    public void transactionTest() {

        /* Step 1: Start a client session. */
        var clientSession = OrmContext.getOrmManager().mongoClient().startSession();

        /* Step 2: Optional. Define options to use for the transaction. */

        var txnOptions = TransactionOptions.builder()
                .readPreference(ReadPreference.primary())
                .readConcern(ReadConcern.LOCAL)
                .writeConcern(WriteConcern.MAJORITY)
                .build();

        /* Step 3: Define the sequence of operations to perform inside the transactions. */


        var txnBody = new TransactionBody<String>() {
            public String execute() {
                var collection = OrmContext.getOrmManager().getCollection(UserEntity.class);
                var userEntity1 = new UserEntity(1, (byte) 2, (short) 3, 5, true, "helloORM", "helloOrm");
                var userEntity2 = new UserEntity(1, (byte) 2, (short) 3, 5, true, "helloORM", "helloOrm");

                /* Important:: You must pass the session to the operations.. */

                collection.insertOne(clientSession, userEntity1);
                collection.insertOne(clientSession, userEntity2);

                return "Inserted into collections in different databases";
            }
        };

        /* Step 4: Use .withTransaction() to start a transaction, execute the callback, and commit (or abort on error). */
        try {
            clientSession.withTransaction(txnBody, txnOptions);
        } catch (RuntimeException e) {
            // some error handling
            e.printStackTrace();
        } finally {
            clientSession.close();
        }
    }
}
