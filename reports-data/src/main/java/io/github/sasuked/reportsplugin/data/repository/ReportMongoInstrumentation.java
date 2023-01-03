package io.github.sasuked.reportsplugin.data.repository;

import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;


class ReportMongoInstrumentation {

    public static Bson uniqueIdFilter(UUID reportId) {
        return eq("uniqueId", reportId);
    }

    public static Bson reportedPlayerIdFilter(UUID reportedPlayerrId) {
        return eq("reportedPlayerId", reportedPlayerrId);
    }

    public static Bson authorIdFilter(UUID authorId) {
        return eq("authorId", authorId);
    }

    public static Document documentSet(Document doc) {
        return new Document("$set", doc);
    }
}
