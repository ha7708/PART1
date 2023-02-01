package springbook.user.dao;

// 제어를 관장하는 팩토리

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;


@Configuration  // 애플리케이션 또는 빈 팩토리가 사용할 설정정보라는 표시.
public class DaoFactory {

    @Bean
    public DataSource dataSource(){ // SimpleDriverDataSource 를 사용하게 함
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource ();

        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/tobi?characterEncoding=UTF-8");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }


    @Bean
    public UserDao userDao(){   // UserDao는 이제 DataSource 를 DI 받음.
        UserDao userDao = new UserDao();
        userDao.setDataSource(dataSource());
        return userDao;
    }



}

