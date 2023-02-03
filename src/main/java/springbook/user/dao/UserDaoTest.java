package springbook.user.dao;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springbook.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext.xml")
public class UserDaoTest {
    /*
    @Autowired
    UserDao dao;
    @Autowired
    SimpleDriverDataSource dataSource; // XML의 datasource 로 등록한 SimpleDriverDataSource 타입의 빈을 가져옴.
    */

    @Autowired
    private ApplicationContext context;     //ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml"); 대신

    // 픽스처: 테스트를 수행하는 데에 필요한 정보 또는 오브젝트.
    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before     // 테스트 메소드 개수만큼 반복됨. (=ApplicationContext 도 같은 개수로 만들어짐)
    public void setUp(){    // 중복 추출
        //ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");
        this.dao = this.context.getBean("userDao", UserDao.class);
        this.user1 = new User("kim1234", "김말이", "1234");
        this.user2 = new User("duck1234", "떡볶이", "25234");
        this.user3 = new User("mandoo", "군만두", "32234");
        System.out.println(this.context);
        System.out.println(this);
    }

    // JUnit 프레임워크에서 동작할 수 있는 테스트 메소드로 전환.
    // 테스트 메소드의 요건 2가지     ⓐ public  ⓑ @Test  ⓒ void
    @Test       // JUnit 에게 테스트용 메소드임을 알려줌
    public void addAndGet() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);
        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userget1 = dao.get(user1.getId());
        assertThat(userget1.getName()).isEqualTo(user1.getName());
        assertThat(userget1.getPassword()).isEqualTo(user1.getPassword());

        User userget2 = dao.get(user2.getId());
        assertThat(userget2.getName()).isEqualTo(user2.getName());
        assertThat(userget2.getPassword()).isEqualTo(user2.getPassword());
    }


    @Test
    public void count() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);
        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    // 예외 테스트: 특정 예외로 던져져야 하며, 통과하는 것이 실패임.
    @Test(expected = EmptyResultDataAccessException.class) // 발생할 것으로 예상되는(발생시켜야 할) "특정" 예외클래스 명시
    public void getUserFailure() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);
        dao.get("unknown_id"); // 존재하지 않는 아이디로 검색: get메소드의 ResultSet rs.next() null시 예외 상황을 발생시킴
    }


}
