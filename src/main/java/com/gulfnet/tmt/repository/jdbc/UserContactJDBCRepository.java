package com.gulfnet.tmt.repository.jdbc;

import com.gulfnet.tmt.model.response.UserBasicInfoResponse;
import com.gulfnet.tmt.model.response.UserContactsResponse;
import com.gulfnet.tmt.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserContactJDBCRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private List<UserContactsResponse> userContactsResponses;
    private final String userContactQuery =
            "SELECT uc.id as contactId, uc.user_id as userId ,gtu.id as contactUserId, gtu.user_name as contactUserName,gtu.first_name as contactFirstName," +
            "gtu.last_name as contactLastName, gtu.email as contactUserEmail, gtu.phone as contactUserPhone, gtu.status as contactUserStatus," +
            "gtu.profile_photo as contactUserPhoto " +
            "FROM USER_CONTACT uc " +
            "JOIN GULFNET_TMT_USER gtu ON uc.user_contact_id = gtu.id " +
            "WHERE user_id = :userId " +
            "order by gtu.first_name , gtu.last_name " +
            "OFFSET :offset  LIMIT :limit ";

    public UserContactsResponse findUserContacts(UUID userId, Pageable pageable) {
        userContactsResponses = new ArrayList<>();
        SqlParameterSource parameters = new MapSqlParameterSource(Map.of(
                "userId", userId,
                "offset", pageable.getPageNumber(),
                "limit", pageable.getPageSize()
        ));
        return namedParameterJdbcTemplate.query(userContactQuery, parameters, (rs, rowNum) -> setUserContactsResponse(rs)).get(0);
    }

    private UserContactsResponse setUserContactsResponse(ResultSet rs) {
        try {
            if (userContactsResponses.isEmpty()) {
                userContactsResponses.add(new UserContactsResponse());
                UserContactsResponse userContactsResponse = userContactsResponses.get(0);
                userContactsResponse.setId(rs.getObject("contactId", UUID.class));
                userContactsResponse.setUserId(rs.getObject("userId", UUID.class));
                userContactsResponse.setUserContacts(new ArrayList<>());
            }
            List<UserBasicInfoResponse> userContacts = userContactsResponses.get(0).getUserContacts();
            UserBasicInfoResponse userBasicInfoResponse = new UserBasicInfoResponse();
            userBasicInfoResponse.setId(rs.getObject("contactUserId", UUID.class));
            userBasicInfoResponse.setUserName(rs.getString("contactUserName"));
            userBasicInfoResponse.setFirstName(rs.getString("contactFirstName"));
            userBasicInfoResponse.setLastName(rs.getString("contactLastName"));
            userBasicInfoResponse.setPhone(rs.getString("contactUserPhone"));
            userBasicInfoResponse.setEmail(rs.getString("contactUserEmail"));
            userBasicInfoResponse.setStatus(rs.getString("contactUserStatus"));
            userBasicInfoResponse.setProfilePhoto(ImageUtil.getB64EncodedStringFromImagePathOrURL(rs.getString("contactUserPhoto")));
            userContacts.add(userBasicInfoResponse);
        } catch (SQLException e) {
            log.error("Error in setUserContactsResponse ", e);
        }
        return userContactsResponses.get(0);
    }

}
