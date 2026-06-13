package com.yeonsung01.cryptopricetrackerapi.domain.coin.dao;

import com.yeonsung01.cryptopricetrackerapi.domain.coin.entity.Coin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class CoinDaoImpl implements CoinDao {

    private final JdbcTemplate jdbcTemplate;

    // Spring이 JdbcTemplate 객체를 자동 주입
    public CoinDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 코인 저장
    @Override
    public Coin save(Coin coin) {

        String sql = """
                INSERT INTO coin
                (symbol, name, api_id, created_at, updated_at)
                VALUES (?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

            ps.setString(1, coin.getSymbol());
            ps.setString(2, coin.getName());
            ps.setString(3, coin.getApiId());
            ps.setObject(4, now);
            ps.setObject(5, now);

            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();

        coin.setId(id);
        coin.setCreatedAt(now);
        coin.setUpdatedAt(now);

        return coin;
    }

    // 코인 단건 조회
    @Override
    public Optional<Coin> findById(Long id) {

        String sql = """
                SELECT *
                FROM coin
                WHERE id = ?
                """;

        List<Coin> result = jdbcTemplate.query(sql,
                (rs, rowNum) -> new Coin(
                        rs.getLong("id"),
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getString("api_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                ),
                id
        );

        return result.stream().findFirst();
    }

    // 코인 목록 조회
    @Override
    public List<Coin> findAll(int offset, int limit) {

        String sql = """
                SELECT *
                FROM coin
                ORDER BY id DESC
                LIMIT ? OFFSET ?
                """;

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new Coin(
                        rs.getLong("id"),
                        rs.getString("symbol"),
                        rs.getString("name"),
                        rs.getString("api_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                ),
                limit, offset
        );
    }

    // 전체 개수 조회
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM coin";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    // 심볼 중복 확인
    @Override
    public boolean existsBySymbol(String symbol) {
        String sql = "SELECT COUNT(*) FROM coin WHERE symbol = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, symbol);

        return count != null && count > 0;
    }

    // 코인 수정
    @Override
    public Coin update(Coin coin) {

        String sql = """
                UPDATE coin
                SET symbol = ?,
                    name = ?,
                    api_id = ?,
                    updated_at = ?
                WHERE id = ?
                """;

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(sql,
                coin.getSymbol(),
                coin.getName(),
                coin.getApiId(),
                now,
                coin.getId()
        );

        coin.setUpdatedAt(now);

        return coin;
    }

    // 코인 삭제
    @Override
    public boolean deleteById(Long id) {

        String sql = """
                DELETE FROM coin
                WHERE id = ?
                """;

        int affectedRows = jdbcTemplate.update(sql, id);

        return affectedRows > 0;
    }
}