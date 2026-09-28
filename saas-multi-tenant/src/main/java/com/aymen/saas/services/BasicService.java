package com.aymen.saas.services;

import com.aymen.saas.common.PageResponse;

import java.util.List;

public interface BasicService<I,O>{

    void create(final I request);
    void update(final String id , final I request);
    O findById(final String id);
    List<O> findAll();
    PageResponse<O> findAll(final int page, final int size);
    void delete(final String id);





}
