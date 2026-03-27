 , with import com.easychat.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.annotation.Resource;
import java.sql.SQLException;

@Component("initRun")
public class InitRun implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);

    @Resource
    private DataSource dataSource;

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{
            dataSource.getConnection();
            redisUtils.get("test");
            logger.info("Service Start success");
        }catch(SQLException e){
            logger.error("Database configure failed ");

        }catch (RedisConnectionFailureException e){
            logger.error("Redis connection failed ");

        }catch (Exception e){
            logger.error("Service failed to start",e);
        }


    }
}
