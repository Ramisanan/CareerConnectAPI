package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DTO.CompanyDTO;
import org.example.grad_gateway2.Entity.Company;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CompanyService {

    Company getCompanyById(long id);

    void addCompany(CompanyDTO companyDTO);

    void updateCompany(CompanyDTO companyDTO, long id);

    void deleteCompany(long id);

    List<Company> getCompany(String name);

    List<Company> getAllCompanies();

    List<Company> getCompanyByLocation(String location);

    List<Company> getCompanyBySize(int size);

    String uploadImage(MultipartFile file, long id) throws IOException;

    byte[] getImage(long id) throws IOException;

    boolean existsByName(String name);
}
