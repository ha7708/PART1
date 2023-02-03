package springbook.user.dao;

import springbook.user.domain.User;

import javax.sql.DataSource;
import javax.swing.plaf.nimbus.State;
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
    
    private JdbcContext jdbcContext;
    private DataSource dataSource;
    public void setDataSource(DataSource dataSource){ // 의존 obj 타입을 ConnectionMaker 에서 DataSource 로 변경함.
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
        this.dataSource = dataSource;
    }
    
    // jdbc 로직 메소드로 추출 (공통)
    // abstract protected PreparedStatement makeStatement(Connection c) throws SQLException;   // 추상화를 고려했지만 맞지 않음.
    // 이에 따로 메소드로 만들었으나 모든 DAO에서 사용하려 jdbc 로직을 jdbcContext 클래스로 독립시킴.
    /*
    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        // 컨텍스트 메소드 파라미터로 stmt 지정 > 만들어진 오브젝트를 컨텍스트 메소드로 전달.
        Connection c = null;
        PreparedStatement ps = null;
        try{
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        }catch (SQLException e){
            throw e;
        }finally {
            if(ps != null) { try { ps.close(); } catch (SQLException e) {} }
            if(c != null) { try { c.close(); } catch (SQLException e) {} }
        }

    }
    */

    // 독립시킨 jdbContext 사용
    /*
    private JdbcContext jdbcContext;
    public void setJdbcContext(JdbcContext jdbcContext){
        this.jdbcContext = jdbcContext;
    }
    */

    public void add(final User user) throws SQLException {  // 외부의 변수 user는 내부클래스에서 운용시 반드시 'final'

        // 옵션2-2 - 익명 내부 클래스 (내부 클래스에서 전환됨) AddStatement
        this.jdbcContext.workWithStatementStrategy(   // 익명(이름이 없음) 클래스이므로 타입이 없음. JdbcContext 클래스를 사용하도록 고쳐줌.
                new StatementStrategy() {   // 구현한 인터페이스: StatementStrategy
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException{    // workWithStatementStrategy 에서 Connection c 받고 실행.
                        PreparedStatement ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES(?, ?, ?)");
                        ps.setString(1, user.getId());
                        ps.setString(2, user.getName());
                        ps.setString(3, user.getPassword());
                        return ps; // 리턴한 ps를 workWithStatementStrategy 에 돌려주고, workWithStatementStrategy는 이 ps를 사용한다.
                    }
                }
        );

        // 옵션2-1 - 내부 클래스 AddStatement
        //public void add(final User user) throws SQLException {} 메소드
        // 내부클래스 == 로컬클래스. 로컬 변수 선언하듯 사용. 특정 메소드에만 사용되는 클래스의 경우 메소드 내에 클래스를 정의하여 사용할 수 있다!
        // 중첩 클래스: ⓐ스태틱 클래스-독립적으로 obj로 만듦 ⓑ내부 클래스-자신이 정의된 클래스와 obj 안에서 만듦
        // 내부클래스: ⓐ멤버 내부 클래스(멤버 필드, obj레벨)  ⓑ로컬 클래스(메소드 레벨)  ⓒ익명 내부 클래스(이름X, 범위는 선언 위치에 따라 다름)
        /*
        class AddStatement implements StatementStrategy {   // 'public' 제외
            User user;
            public AddStatement(User user) { this.user = user; }
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {  // StatementStrategy 인터페이스를 구현함.
                PreparedStatement ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES(?,?,?)");
                ps.setString(1, user.getId());
                ps.setString(2, user.getName());
                ps.setString(2, user.getPassword());
                return ps;
            }

        } // 내부클래스 종료

        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
        */

    }


    public User get(String id) throws SQLException{

        Connection c = this.dataSource.getConnection();
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

    public void deleteAll() throws SQLException{    // 클라이언트 책임 담당
        // 옵션3 - 변하지 않는 SQL 문장 사용, executeSQL은 jdbcContext 클래스 소속으로
        this.jdbcContext.executeSql("DELETE FROM users");

        // 옵션2 - 익명 내부 클래스 - add() 동일
        /*
        this.jdbcContext.workWithStatementStrategy(
                new StatementStrategy() {
                    public PreparedStatement makePreparedStatement(Connection c) throws SQLException{
                        return c.prepareStatement("DELETE FROM users");
                    }
                }
        );
        */

        // 옵션1 - 전략 패턴을 사용.
        /*
        StatementStrategy st = new DeleteAllStatement();      // 다형성 + 인터페이스
        jdbcContextWithStatementStrategy(st); // 컨텍스트 호출. 전략 오브젝트 전달
        */

    }

    public int getCount() throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement("select count(*) from users");

        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        rs.close();
        ps.close();
        c.close();

        return count;
    }



}
