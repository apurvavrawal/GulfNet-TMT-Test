package com.gulfnet.tmt.specifications;


import com.gulfnet.tmt.entity.sql.User;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;
import java.util.List;

public class UserSpecifications {
    public static Specification<User> withSearch(String search) {
        String searchString = MessageFormat.format("%{0}%", search.toLowerCase());
        return (root, query, builder) -> builder.or(
                builder.like(builder.lower(root.get("userName")), searchString),
                builder.like(builder.lower(root.get("firstName")), searchString),
                builder.like(builder.lower(root.get("lastName")), searchString),
                builder.like(builder.lower(root.get("phone")), searchString),
                builder.like(builder.lower(root.get("email")), searchString)
        );
    }

    public static Specification<User> withAppType(String search) {
        String searchString = MessageFormat.format("{0}", search.toLowerCase());
        return (root, query, builder) -> builder.like(builder.lower(root.get("appType")), searchString);
    }

    public static Specification<User> withStatus(String status) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("status")), status);
    }

    public static Specification<User> hasUserGroups(List<String> userGroups) {
        return (root, query, builder) -> root.join("userGroups").get("group").get("code").in(userGroups);
    }

    public static Specification<User> hasUserRoles(List<String> userRoles) {
        return (root, query, builder) -> root.join("userRole").get("role").get("code").in(userRoles);
    }
}