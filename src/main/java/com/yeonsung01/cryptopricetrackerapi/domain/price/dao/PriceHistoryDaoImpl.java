package com.yeonsung01.cryptopricetrackerapi.domain.price.dao;

import com.yeonsung01.cryptopricetrackerapi.domain.price.entity.PriceHistory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PriceHistoryDaoImpl implements PriceHistoryDao {

    private final JdbcTemplate jdbcTemplate;

    public PriceHistoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PriceHistory save(PriceHistory priceHistory) {
        String sql = """
                INSERT INTO price_history (coin_id, price_usd, fetched_at)
                VALUES (?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, priceHistory.getCoinId());
            ps.setBigDecimal(2, priceHistory.getPriceUsd());
            ps.setObject(3, now);
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        return new PriceHistory(id, priceHistory.getCoinId(), priceHistory.getPriceUsd(), now);
    }

    @Override
    public Optional<PriceHistory> findLatestByCoinId(Long coinId) {
        String sql = """
                SELECT * FROM price_history
                WHERE coin_id = ?
                ORDER BY fetched_at DESC
                LIMIT 1
                """;

        List<PriceHistory> result = jdbcTemplate.query(sql,
                (rs, rowNum) -> new PriceHistory(
                        rs.getLong("id"),
                        rs.getLong("coin_id"),
                        rs.getBigDecimal("price_usd"),
                        rs.getTimestamp("fetched_at").toLocalDateTime()
                ),
                coinId
        );

        return result.stream().findFirst();
    }

    @Override
    public List<PriceHistory> findByCoinId(Long coinId, int offset, int limit) {
        String sql = """
                SELECT * FROM price_history
                WHERE coin_id = ?
                ORDER BY fetched_at DESC
                LIMIT ? OFFSET ?
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new PriceHistory(
                        rs.getLong("id"),
                        rs.getLong("coin_id"),
                        rs.getBigDecimal("price_usd"),
                        rs.getTimestamp("fetched_at").toLocalDateTime()
                ),
                coinId, limit, offset
        );
    }

    @Override
    public long countByCoinId(Long coinId) {
        String sql = "SELECT COUNT(*) FROM price_history WHERE coin_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, coinId);
    }
}
