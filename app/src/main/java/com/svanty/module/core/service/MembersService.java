package com.svanty.module.core.service;

import com.svanty.module.core.db.UserRepository;
import com.svanty.module.core.db.entity.User;
import com.svanty.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MembersService extends PaginationService<UserRepository, User> {

    @Autowired
    MembersService(UserRepository repository) {
        super(repository);
    }
}
