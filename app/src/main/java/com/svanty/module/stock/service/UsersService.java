package com.svanty.module.stock.service;

import com.svanty.db.page.Paged;
import com.svanty.db.page.Paging;
import com.svanty.module.core.db.UserRepository;
import com.svanty.module.core.db.entity.User;
import com.svanty.module.core.model.UserModel;
import com.svanty.module.stock.db.ImagesRepository;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.security.UserDetailsSecurity;
import com.svanty.security.validator.AccountSettingsForm;
import com.svanty.service.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class UsersService extends PaginationService<UserRepository, User> {

    UserRepository userRepository;

    ImagesRepository imagesRepository;

    @Autowired
    UsersService(UserRepository userRepository, ImagesRepository imagesRepository) {
        super(userRepository);

        this.userRepository = userRepository;
        this.imagesRepository = imagesRepository;
    }


    public Paged<UserModel> getUsersByPage(Integer pageNumber, Integer size) {
        PageRequest request = PageRequest.of(pageNumber - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<User> users = userRepository.findAll(request);

        Locale locale = LocaleContextHolder.getLocale();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        List<UserModel> usersModel = users.getContent().stream().map(e-> new UserModel(
                e.getId(),
                e.getUsername(),
                e.getName(),
                e.getBio(),
                "",
                e.getEmail(),
                e.getDate(),
                df.format(e.getDate()),
                "https://svanty.com/public/uploads/avatar/" + e.getAvatar(),
                "https://svanty.com/public/uploads/cover/" + e.getCover(),
                e.getStatus(),
                e.getTypeAccount(),
                User.Role.normal,
                e.getWebsite(),
                "",
                e.getTwitter(),
                e.getFacebook(),
                e.getGoogle(),
                e.getPaypal_account(),
                e.getOauth_uid(),
                e.getOauth_provider(),
                "",
                e.getAuthorizedToUpload(),
                e.getInstagram(),
                new BigDecimal("0.0"),
                new BigDecimal("0.0"),
                e.getPaymentGateway(),
                e.getBank(),
                e.getIp(),
                e.getAuthorExclusive(),
                0l,
                imagesRepository.getCountWithStatusAndUserId(Images.Status.active, e.getId())
        )).collect(Collectors.toList());

        Page<UserModel> userModelPage = new PageImpl<>(usersModel);
        return new Paged<>(userModelPage, Paging.of(users.getTotalPages(), users.getTotalElements(), pageNumber, size));
    }

    public User getByUsername(String username) {
            return userRepository.getByUsername(username);
    }

    public void updateAccount(AccountSettingsForm form, String uploadRootPath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final Path rootLocation = Paths.get(uploadRootPath);

        UserDetailsSecurity userDetailsSecurity = (UserDetailsSecurity) authentication.getPrincipal();
        User user = userDetailsSecurity.getUserDetails();

        try {
            MultipartFile fileAvatar = form.getAvatar();
            if (!fileAvatar.isEmpty() && FileService.isImage(fileAvatar.getOriginalFilename())) {
                Files.copy(fileAvatar.getInputStream(), rootLocation.resolve(fileAvatar.getOriginalFilename()));

                String path = rootLocation.resolve(fileAvatar.getOriginalFilename()).toString();
                String newFileName = user.getUsername() + "-" + FileService.generateRandomFileName(fileAvatar.getOriginalFilename(), 10);
                String newFileNamePath = rootLocation + "/avatar/" + newFileName;

                FileService.resizeImage(path, newFileNamePath, 180, 180);

                user.setAvatar(newFileName);
            }


            MultipartFile fileBackground = form.getBackground();
            if (!fileBackground.isEmpty() && FileService.isImage(fileBackground.getOriginalFilename())) {
                Files.copy(fileBackground.getInputStream(), rootLocation.resolve(fileBackground.getOriginalFilename()));

                String path = rootLocation.resolve(fileBackground.getOriginalFilename()).toString();
                String newFileName = user.getUsername() + "-" + FileService.generateRandomFileName(fileBackground.getOriginalFilename(), 10);
                String newFileNamePath = rootLocation + "/cover/" + newFileName;

                FileService.resizeImage(path, newFileNamePath, 1500, 180);

                user.setCover(newFileName);
            }

        } catch (IOException e) {
        } catch (Exception e) {
        }

        user.setName(form.getName());
        user.setCity(form.getCity());
        user.setPaypal_account(form.getPaypal_account());
        user.setWebsite(form.getWebsite());
        user.setFacebook(form.getFacebook());
        user.setTwitter(form.getTwitter());
        user.setInstagram(form.getInstagram());
        user.setBio(form.getBio());

        userRepository.save(user);
    }
}
