package io.github.sasuked.reportsplugin.data.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.ReturnDocument;
import io.github.sasuked.reportsplugin.data.ReportData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.github.sasuked.reportsplugin.data.repository.ReportMongoInstrumentation.uniqueIdFilter;

public class ReportMongoRepository {

    private static final String MONGODB_URI_FORMAT = "mongodb://<user>:<password>@<host>:<port>";
    private static final String COLLECTION_NAME = "redetela-reports";

    private static final FindOneAndReplaceOptions REPLACE_OPTIONS = new FindOneAndReplaceOptions()
      .upsert(true)
      .returnDocument(ReturnDocument.AFTER);


    private final MongoClient client;
    private final MongoCollection<ReportData> collection;

    public ReportMongoRepository(MongoClient client, MongoCollection<ReportData> collection) {
        this.client = client;
        this.collection = collection;
    }

    public static @NotNull ReportMongoRepository fromCredentials(
      @NotNull Map<String, Object> credentials
    ) {
        String databaseName = (String) credentials.getOrDefault("mongo.database", "admin");
        String user = (String) credentials.get("mongo.user");
        String password = (String) credentials.get("mongo.password");
        String host = (String) credentials.get("mongo.host");
        int port = (int) credentials.getOrDefault("mongo.port", 27017);

        MongoClient client = MongoClients.create(MONGODB_URI_FORMAT
          .replace("<user>", user)
          .replace("<password>", password)
          .replace("<host>", host)
          .replace("<port>", String.valueOf(port)));

        return new ReportMongoRepository(
          client,
          client.getDatabase(databaseName).getCollection(COLLECTION_NAME, ReportData.class)
        );
    }


    @Nullable
    public ReportData findOne(@NotNull UUID reportUniqueId) {
        return collection.find(Filters.eq("uniqueId", reportUniqueId)).first();
    }

    @NotNull
    public List<ReportData> selectAll() {
        return collection.find().into(new ArrayList<>());
    }

    @Nullable
    public ReportData updateReport(@NotNull ReportData reportData) {
        return collection.findOneAndReplace(
          uniqueIdFilter(reportData.getUniqueId()),
          reportData,
          REPLACE_OPTIONS
        );
    }

    @Nullable
    public ReportData deleteReport(@NotNull UUID reportUniqueId) {
        return collection.findOneAndDelete(uniqueIdFilter(reportUniqueId));
    }

    @Override
    protected void finalize() throws Throwable {
        client.close();

        super.finalize();
    }
}
