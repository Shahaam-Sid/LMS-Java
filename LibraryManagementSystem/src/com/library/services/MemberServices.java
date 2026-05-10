package com.library.services;

import com.library.exceptions.DuplicatePupilException;
import com.library.exceptions.MemberNotFoundException;
import com.library.models.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for MemberServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class MemberServices {

    private Map<String, Member> members = new HashMap<>();

    /**
     * registers new member
     * @param member to register
     * @throws DuplicatePupilException if member already exists
     */
    public void registerMember(Member member) throws DuplicatePupilException {
        if (members.containsKey(member.getMemberID()))
            throw new DuplicatePupilException(member.getMemberID());

        members.put(member.getMemberID(), member);
    }
    /**
     * get member object
     * @param id of member to get
     * @return member
     * @throws MemberNotFoundException if member not found
     */
    public Member getMember(String id) throws MemberNotFoundException {
        Member member = members.get(id);
        if (member ==  null) throw new MemberNotFoundException(id);

        return member;
    }
    /**
     * removes member
     * @param id of member to remove
     * @throws MemberNotFoundException if member not found
     */
    public void removeMember(String id) throws MemberNotFoundException {
        if (!members.containsKey(id)) throw new MemberNotFoundException(id);

        members.remove(id);
    }
    
    /**
     * searches for member
     * @param query string for member to search
     * @return list of results matched
     */
    public List<Member> searchMembers(String query) {
        List<Member> result = new ArrayList<>();
        for (Member member : members.values()) {
            if (member.matchesQuery(query)) result.add(member);
        }

        return result;
    }
    /**
     * returns list of all members
     * @return list of members
     */
    public List<Member> getAllMembers() {
        return new ArrayList<>(members.values());
    }
}