package com.example.hospital.api.service;

import com.example.hospital.api.common.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface DoctorService {
    public PageUtils searchByPage(Map param);
    public HashMap searchContent(int id);
    public void updatePhoto(MultipartFile file, Integer doctorId);
    public void insert(Map param);
    public HashMap searchById(int id);
    public void deleteByIds(Integer[] ids);
    public void update(Map param);

    public ArrayList<HashMap> searchByDeptSubId(int deptSubId);}
