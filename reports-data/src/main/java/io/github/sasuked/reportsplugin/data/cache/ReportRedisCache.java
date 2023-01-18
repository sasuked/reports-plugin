package io.github.sasuked.reportsplugin.data.cache;

import io.github.sasuked.reportsplugin.data.ReportData;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

import static io.github.sasuked.reportsplugin.data.cache.ReportRedisInstrumentation.deserializeReport;


// TODO find a way to self-expire cached reports in the redis
@Log
public class ReportRedisCache {

    private static final String REPORTS_CACHE_KEY = "reports";

    private final JedisPool pool;

    public ReportRedisCache(JedisPool pool) {
        this.pool = pool;
    }

    public static ReportRedisCache fromCredentials(@NotNull Map<String, Object> credentials) {
        String host = (String) credentials.getOrDefault("redis.host", "localhost");
        int port = (int) credentials.getOrDefault("redis.port", 6379);

        JedisPool jedisPool = new JedisPool(host, port);
        return new ReportRedisCache(jedisPool);
    }


    public long put(@NotNull ReportData reportData) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hset(REPORTS_CACHE_KEY, reportData.getUniqueId().toString(), ReportRedisInstrumentation.toJson(reportData));
        } catch (Exception e) {
            log.severe("Failed to put a report in the jedis cache: " + reportData.getUniqueId());
            return -1L;
        }
    }

    public long invalidateReport(@NotNull ReportData reportData) {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hdel(REPORTS_CACHE_KEY, reportData.getUniqueId().toString());
        } catch (Exception e) {
            log.severe("Failed to invalidate a report from the jedis cache: " + reportData.getUniqueId());
            return -1L;
        }
    }

    @Nullable
    public ReportData getReportData(@NotNull UUID reportId) {
        try (Jedis jedis = pool.getResource()) {
            String jsonValue = jedis.hget(REPORTS_CACHE_KEY, reportId.toString());
            if (jsonValue == null) {
                return null;
            }

            ReportData reportData = deserializeReport(jsonValue);
            if (System.currentTimeMillis() >= reportData.getExpirationTime()) {
                invalidateReport(reportData);
                return null;
            }

            return reportData;
        }
    }

    @NotNull
    public List<ReportData> getCachedReports() {
        Set<String> keys = this.getReportCachedKeys();

        try (Jedis jedis = pool.getResource()) {
            List<ReportData> data = new ArrayList<>();
            for (String key : keys) {
                if (!jedis.hexists(REPORTS_CACHE_KEY, key)) {
                    continue;
                }

                String value = jedis.hget(REPORTS_CACHE_KEY, key);
                if (value == null) {
                    continue;
                }

                ReportData report = deserializeReport(value);
                if (System.currentTimeMillis() >= report.getExpirationTime()) {
                    invalidateReport(report);
                } else {
                    data.add(report);
                }
            }

            return data;
        } catch (Exception e) {
            log.severe("Failed to obtain the cached reports from jedis: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private Set<String> getReportCachedKeys() {
        try (Jedis jedis = pool.getResource()) {
            return jedis.hkeys(REPORTS_CACHE_KEY);
        } catch (Exception e) {
            log.severe("Failed to obtain the cached reports KEYS from jedis: " + e.getMessage());
            return Collections.emptySet();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        if (!pool.isClosed()) {
            pool.close();
        }
        super.finalize();
    }
}
