package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DTO.CompanyDTO;
import org.example.grad_gateway2.Entity.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;


@Service
public class CompanyServiceImpl implements CompanyService{
    private final CompanyRepository companyRepository;

    //path separator is independent of the operating system
    private final String BASE_PATH = "C:" + File.separator+"Users" + File.separator + "lebak" +
            File.separator + "Full stack" + File.separator + "Projects"
            + File.separator+"images" + File.separator;




    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company getCompanyById(long id) {

        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    @Override
    public void addCompany(CompanyDTO companyDTO) {
        Company company = Company.builder()
                .id(0L)
                .logo(null)
                .description(companyDTO.getDescription())
                .url(companyDTO.getUrl())
                .name(companyDTO.getName())
                .location(companyDTO.getLocation())
                .size(companyDTO.getSize())
                .build();
        companyRepository.save(company);
    }

    @Override
    public void updateCompany(CompanyDTO companyDTO, long id) {

        Optional<Company> company = companyRepository.findById(id);
        if(company.isPresent()) {
            company.get().setName(companyDTO.getName());
            company.get().setLocation(companyDTO.getLocation());
            company.get().setUrl(companyDTO.getUrl());
            company.get().setDescription(companyDTO.getDescription());
            company.get().setSize(companyDTO.getSize());
            companyRepository.save(company.get());
        }
    }

    @Override
    public void deleteCompany(long id) {
        companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        companyRepository.deleteById(id);
    }

    @Override
    public List<Company> getCompany(String name) {
        return companyRepository.findAllByNameLikeIgnoreCase("%" + name + "%" );
    }

    @Override
    public List<Company> getAllCompanies() {
        Optional<List<Company>> companies = Optional.of(companyRepository.findAll());
        return companies.orElse(null);
    }

    @Override
    public List<Company> getCompanyByLocation(String location) {
        Optional<List<Company>> companies = Optional.of(companyRepository.findAllByLocationLikeIgnoreCase(location));
        return companies.orElse(null);
    }

    @Override
    public List<Company> getCompanyBySize(int size) {
        Optional<List<Company>> companies = Optional.of(companyRepository.findAllBySizeLessThanEqual(size));
        return companies.orElse(null);
    }

    @Override
    public String uploadImage(MultipartFile file, long id) throws IOException{
        String fileName = file.getOriginalFilename();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String  path = BASE_PATH +timeStamp+"_"+ fileName;

        file.transferTo(new File(path));

        Optional<Company> company = companyRepository.findById(id);
        if (company.isPresent()) {
            company.get().setLogo(path);
            companyRepository.save(company.get());
        }
        return path;
    }

    @Override
    public byte[] getImage(long id) throws IOException {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
        String path = company.getLogo();
        return Files.readAllBytes(new File(path).toPath());
    }

    @Override
    public boolean existsByName(String name) {
        return companyRepository.existsByName(name);
    }
}
