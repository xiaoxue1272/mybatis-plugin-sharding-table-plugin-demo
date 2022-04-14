package com.tiangou;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInterceptor;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.tiangou.interceptor.ShardTableHelper;
import com.tiangou.interceptor.ShardTableInterceptor;
import com.tiangou.mapper.TestMapper;
import com.tiangou.po.User;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://47.101.51.10/test");
        dataSource.setUser("root");
        dataSource.setPassword("127201");
        dataSource.setPort(3306);
        Environment environment = new Environment.Builder("development-Environment")
                .transactionFactory(new JdbcTransactionFactory())
                .dataSource(dataSource)
                .build();
        Configuration configuration = new Configuration();
        configuration.setEnvironment(environment);
        configuration.setLogImpl(StdOutImpl.class);
        configuration.addMapper(TestMapper.class);
        configuration.addInterceptor(new PageInterceptor());
        configuration.addInterceptor(new ShardTableInterceptor(true));
        SqlSessionFactory factory = new SqlSessionFactoryBuilder()
                .build(configuration);
        SqlSession sqlSession = factory.openSession();
        TestMapper mapper = sqlSession.getMapper(TestMapper.class);


        // 1
        ShardTableHelper.startShardingTable("02");
        PageHelper.startPage(1, 1);
        List<User> list = mapper.simpleQuery();
        System.out.println(list);

        // 2
        ShardTableHelper.startShardingTable("01");
        PageHelper.startPage(1, 1);
        User user = new User();
        user.setName("舔狗");
        List<User> list1 = mapper.simpleConditionQuery(user);
        System.out.println(list1);

        // 3
        ShardTableHelper.startShardingTable("02");
        User user1 = new User();
        user1.setId("1");
        user1.setName("二狗子");
        int update = mapper.simpleUpdate(user1);
        sqlSession.commit();

        // 4
        User user2 = new User();
        user2.setName("舔狗QwQ");
        user2.setAge(20);
        ShardTableHelper.startShardingTable("01");
        int insert = mapper.simpleInsert(user2);
        sqlSession.commit();

        // 5
        User user3 = new User();
        user3.setName("舔狗QwQ");
        user3.setAge(20);
        ShardTableHelper.startShardingTable("01");
        mapper.simpleDelete(user3);
        sqlSession.commit();
    }


}
