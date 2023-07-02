package lk.ijse.dep10.pos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Configuration
public class JdbcConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource) {
            @Override
            public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
                try {
                    return super.queryForObject(sql, rowMapper, args);
                } catch (EmptyResultDataAccessException e) {
                    return null;
                }
            }
        };
    }
}
