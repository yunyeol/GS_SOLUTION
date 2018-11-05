package gs.msg.target.mapper;

import gs.msg.target.dto.testDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class testmapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        testDto test = new testDto();

        test.setTest(rs.getString("test"));
        return test;
    }
}
