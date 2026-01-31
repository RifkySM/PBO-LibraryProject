package com.example.pbolibraryproject.service;

import com.example.pbolibraryproject.models.Member;
import com.example.pbolibraryproject.util.CSVUtil;
import java.util.ArrayList;
import java.util.List;

public class MemberService {
    private static final String CSV_FILE = "members.csv";
    private List<Member> members;

    public MemberService() {
        this.members = new ArrayList<>();
        loadFromCSV();
    }

    private void loadFromCSV() {
        if (!CSVUtil.fileExists(CSV_FILE)) {
            loadSampleData();
            saveToCSV();
        } else {
            List<String[]> data = CSVUtil.readCSV(CSV_FILE);
            for (String[] row : data) {
                if (row.length >= 6) {
                    Member member = new Member(row[0], row[1], row[2], row[3],
                        java.time.LocalDate.parse(row[4]));
                    member.setActive(Boolean.parseBoolean(row[5]));
                    members.add(member);
                }
            }
        }
    }

    private void saveToCSV() {
        List<String[]> data = new ArrayList<>();
        for (Member member : members) {
            String[] row = {
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone(),
                member.getJoinDate().toString(),
                String.valueOf(member.isActive())
            };
            data.add(row);
        }
        CSVUtil.writeCSV(CSV_FILE, data);
    }

    private void loadSampleData() {
        members.add(new Member("M001", "John Doe", "john.doe@email.com", "+1234567890",
            java.time.LocalDate.now().minusMonths(6)));
        members.add(new Member("M002", "Jane Smith", "jane.smith@email.com", "+1234567891",
            java.time.LocalDate.now().minusMonths(3)));
        members.add(new Member("M003", "Bob Johnson", "bob.johnson@email.com", "+1234567892",
            java.time.LocalDate.now().minusMonths(1)));
    }

    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    public Member getMemberById(String memberId) {
        return members.stream()
                .filter(m -> m.getId().equals(memberId))
                .findFirst()
                .orElse(null);
    }

    public void addMember(Member member) {
        members.add(member);
        saveToCSV();
    }

    public void updateMember(Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId().equals(updatedMember.getId())) {
                members.set(i, updatedMember);
                break;
            }
        }
        saveToCSV();
    }

    public void deleteMember(Member member) {
        members.removeIf(m -> m.getId().equals(member.getId()));
        saveToCSV();
    }

    public List<Member> searchMembers(String query) {
        List<Member> results = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Member member : members) {
            if (member.getName().toLowerCase().contains(lowerQuery) ||
                member.getId().toLowerCase().contains(lowerQuery) ||
                member.getEmail().toLowerCase().contains(lowerQuery)) {
                results.add(member);
            }
        }

        return results;
    }

    public String generateMemberId() {
        int maxId = 0;
        for (Member member : members) {
            String id = member.getId().substring(1); // Remove 'M' prefix
            int numId = Integer.parseInt(id);
            if (numId > maxId) {
                maxId = numId;
            }
        }
        return String.format("M%03d", maxId + 1);
    }

    public boolean isEmailExists(String email, String excludeMemberId) {
        for (Member member : members) {
            // Skip checking the current member being edited
            if (excludeMemberId != null && member.getId().equals(excludeMemberId)) {
                continue;
            }
            if (member.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }
}
