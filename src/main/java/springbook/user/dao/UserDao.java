package springbook.user.dao;

import springbook.user.domain.User;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.*;
/*
문제
N 사와 D 사가 각각 자사의 로직을 사용하고 싶어함.
1. UserDao 를 추상화: 서브클래스로 만들어 상속받아야 한다는 단점.
2. 서브클래스가 아닌 아예 별도의 클래스로 만들어 사용하게 함. > p72 : N, D사에서 각각 DB 커넥션 제공 클래스가 다르다면 UserDao의 가져오는 이름을 N, D 사의 메소드 이름을 일일이 바꿔줘야 됨.
3. 인터페이스 도입:
*/

public class UserDao {

    private DataSource dataSource;
    public void setDataSource(DataSource dataSource){ // 의존 obj 타입을 ConnectionMaker 에서 DataSource 로 변경함.
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        // Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users(id, name, password) VALUES(?,?,?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());


        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public User get(String id) throws SQLException{
        // Connection c = connectionMaker.makeConnection();
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM users WHERE id = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();
        return user;
    }

    public void deleteAll() throws SQLException{
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM users");
        ps.executeUpdate();
        ps.close();
        c.close();
    }

    public int getCount() throws SQLException{
        Connection c = dataSource.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }



}
