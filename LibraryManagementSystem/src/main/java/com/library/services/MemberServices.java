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

    private static final String TABLE = "members";
    private static final String UIDCOL = "member_id";

    /**
     * Checks if database is empty
     * @return true if empty, else not
     */
    public boolean isEmpty() {return DBUtility.isEmpty(TABLE);}
    /**
     * registers new member
     * @param member to register
     * @throws DuplicatePupilException if member already exists
     * @throws RuntimeException if query not executes correctly
     * @throws SQLException Error from Database
     */
    public void registerMember(Member member) throws DuplicatePupilException {
        
        try (Connection conn = DBConnection.getConnection()) {
            if (DBUtility.doesRowExists(TABLE, UIDCOL, member.getMemberID(), conn))
                throw new DuplicatePupilException(member.getMemberID());
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
    public void updateMember(String targetId, String name, String phone, String email, String address,
        String status) throws MemberNotFoundException, IllegalArgumentException {
            Member member = getMember(targetId);

            try (Connection conn = DBConnection.getConnection()) {
                int rowsAffected = 0;
                int countChangesMade = 0;
                try {
                    conn.setAutoCommit(false);

                    if ((name != null && !name.isEmpty()) && !name.equals(member.getName())) {
                        countChangesMade++;
                        member.setName(name);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE members SET member_name = ? WHERE member_id = ?")) {
                            ps.setString(1, member.getName());
                            ps.setString(2, member.getMemberID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((phone != null && !phone.isEmpty()) && !phone.equals(member.getPhone())) {
                        countChangesMade++;
                        member.setPhone(phone);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE members SET phone = ? WHERE member_id = ?")) {
                            ps.setString(1, member.getPhone());
                            ps.setString(2, member.getMemberID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((email != null && !email.isEmpty()) && !email.equals(member.getEmail())) {
                        countChangesMade++;
                        member.setEmail(email);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE members SET email = ? WHERE member_id = ?")) {
                            ps.setString(1, member.getEmail());
                            ps.setString(2, member.getMemberID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((address != null && !address.isEmpty()) && !address.equals(member.getAddress())) {
                        countChangesMade++;
                        member.setAddress(address);

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE members SET address = ? WHERE member_id = ?")) {
                            ps.setString(1, member.getAddress());
                            ps.setString(2, member.getMemberID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                    if ((status != null && !status.isEmpty()) && !status.equals(member.getStatus())) {
                        countChangesMade++;
                        member.setStatus(MemberStatus.valueOf(status));

                        try (PreparedStatement ps = conn.prepareStatement("UPDATE members SET member_status = ? WHERE member_id = ?")) {
                            ps.setString(1, member.getStatus());
                            ps.setString(2, member.getMemberID());

                            rowsAffected += ps.executeUpdate();
                        }
                    }
                
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    if (countChangesMade == rowsAffected) conn.commit();
                    else conn.rollback();
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                DBUtility.SQLExceptionLoop(e);
            }
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
            if (!DBUtility.doesRowExists(TABLE, UIDCOL, id, conn))
                throw new MemberNotFoundException(id);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM members WHERE member_id = ?")) {
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