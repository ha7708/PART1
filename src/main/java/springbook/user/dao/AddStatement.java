package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {

    User user;
    public AddStatement(User user){
        this.user = user;
    }

    // StatementStrategy 인터페이스를 구현함.
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        PreparedStatement ps = c.prepareStatement("INSERT INTO users(id, name, password) VALUES(?,?,?)");
        ps.setString(1, user.getId());  // user 는 어디서 가져올까? > add() 메소드가 가지고 있음. 위에 생성자에서 가지고 온다.
        ps.setString(2, user.getName());
        ps.setString(2, user.getPassword());
        return ps;
    }





}
