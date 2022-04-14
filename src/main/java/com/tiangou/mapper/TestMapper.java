package com.tiangou.mapper;

import com.tiangou.po.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMapper {

    List<User> simpleQuery();

    List<User> simpleConditionQuery(User user);

    int simpleUpdate(User user);

    int simpleInsert(User user);

    int simpleDelete(User user);

}
