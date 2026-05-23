package com.library.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.db.DBConnection;
import com.library.db.DBUtility;
import com.library.enums.MemberStatus;
import com.library.exceptions.DuplicatePupilException;
import com.library.exceptions.MemberNotFoundException;
import com.library.models.Member;

/**
 * Class for MemberServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class MemberServices {

    /**
     * Checks if database is empty
     * @return true if empty, else not
     */
    public boolean isEmpty() {return DBUtility.isEmpty("members");}
    /**
     * registers new member
     * @param member to register
     * @throws DuplicatePupilException if member already exists
     * @throws RuntimeException if query not executes correctly
     * @throws SQLException Error from Database
     */
    public void registerMember(Member member) throws DuplicatePupilException {
        
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT member_id FROM members WHERE member_id = ?")) {
                ps.setString(1, member.getMemberID());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) throw new DuplicatePupilException(member.getMemberID());
                }
            }
            try (PreparedStatement ps = conn.prepareCall("INSERT INTO members VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, member.getMemberID());
                ps.setString(2, member.getName());
                ps.setString(3, member.getPhone());
                ps.setString(4, member.getEmail());
                ps.setString(5, member.getAddress());
                ps.setInt(6, member.getAge());
                ps.setString(7, member.getStatus());

                int output = ps.executeUpdate();
                if (output == 0) throw new RuntimeException("An Unexpected Error Occured");
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    }
    /**
     * get member object
     * @param id of member to get
     * @return member
     * @throws MemberNotFoundException if member not found
     * @throws SQLException Error from Database
     */
    public Member getMember(String id) throws MemberNotFoundException {
    
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM members WHERE member_id = ?")) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapMemberFromDB(rs);
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        throw new MemberNotFoundException(id);
    }
    /**
     * removes member
     * @param id of member to remove
     * @throws MemberNotFoundException if member not found
     * @throws RuntimeException if query not executes correctly
     * @throws SQLException Error from Database
     */
    public void removeMember(String id) throws MemberNotFoundException {
        
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM members WHERE member_id = ?")) {
                ps.setString(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new MemberNotFoundException(id);
                }
            } try (PreparedStatement ps = conn.prepareStatement("DELETE FROM members WHERE member_id = ?")) {
                ps.setString(1, id);
                int output = ps.executeUpdate();
                if (output == 0) throw new RuntimeException("An Unexpected Error Occured");
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    }
    
    /**
     * searches for member
     * @param query string for member to search
     * @return list of results matched
     * @throws SQLException Error from Database
     */
    public List<Member> searchMembers(String query) {
        List<Member> members = new ArrayList<>();
        String sql = """
                SELECT * FROM members
                WHERE LOWER(member_id) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(member_name) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(email) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(phone) LIKE LOWER(CONCAT('%', ?, '%'))
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ps.setString(4, query);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) members.add(mapMemberFromDB(rs));
            }
            
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return members;
    }
    /**
     * returns list of all members
     * @return list of members
     */
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM members");
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) members.add(mapMemberFromDB(rs));
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return members;
    }

    
    public static Member mapMemberFromDB(ResultSet rs) throws IllegalArgumentException, SQLException{
        if (rs == null) throw new IllegalArgumentException("Invalid Response from Database");

        Member m =  new Member(rs.getString("member_id"), rs.getString("member_name"), 
                rs.getString("phone"), rs.getString("email"), rs.getString("address"), rs.getInt("age"));
        m.setStatus(MemberStatus.valueOf(rs.getString("member_status")));
        
        return m;
    }
} 
// => Prevent member from deletion if has active transaction