package com.svanty.module.stock.controller;

import com.svanty.constans.GlobalConstants;
import com.svanty.db.page.Paged;
import com.svanty.module.core.db.entity.User;
import com.svanty.module.core.model.UserModel;
import com.svanty.module.stock.db.ImagesRepository;
import com.svanty.module.stock.db.entity.Images;
import com.svanty.module.stock.model.ImageItemModel;
import com.svanty.module.stock.service.ImagesService;
import com.svanty.module.stock.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private ImagesService imagesService;

    private UsersService usersService;

    private ImagesRepository imagesRepository;


    @Autowired
    public ApiController(ImagesService imagesService,
                         UsersService usersService,
                         ImagesRepository imagesRepository) {
        this.imagesService = imagesService;
        this.usersService = usersService;
        this.imagesRepository = imagesRepository;
    }


    @GetMapping("/photos/random")
    public ResponseEntity<List<ImageItemModel>> getRandomPhotos() {

        List<ImageItemModel> images =
                imagesService.getRandomImageItemPage(GlobalConstants.IMAGES_LIST_PAGE_MAX_LIST_COUNT);

        return new ResponseEntity<>(images,
                HttpStatus.OK);
    }

    @GetMapping("/members")
    public ResponseEntity<List<UserModel>> members(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber) {

        Paged<UserModel> members = usersService.getUsersByPage(pageNumber, GlobalConstants.MEMBERS_LIST_PAGE_MAX_LIST_COUNT);

        return new ResponseEntity<>(members.getPage().toList(),
                HttpStatus.OK);
    }


    @GetMapping("/account/{id}")
    public ResponseEntity<UserModel> account(@PathVariable Integer id) {

        User account = usersService.getById(id);

        Locale locale = LocaleContextHolder.getLocale();
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

        UserModel userModel = new UserModel(
                account.getId(),
                account.getUsername(),
                account.getName(),
                account.getBio(),
                "",
                account.getEmail(),
                account.getDate(),
                df.format(account.getDate()),
                "https://svanty.com/public/uploads/avatar/" + account.getAvatar(),
                "https://svanty.com/public/uploads/cover/" + account.getCover(),
                account.getStatus(),
                account.getTypeAccount(),
                User.Role.normal,
                account.getWebsite(),
                "",
                account.getTwitter(),
                account.getFacebook(),
                account.getGoogle(),
                account.getPaypal_account(),
                account.getOauth_uid(),
                account.getOauth_provider(),
                "",
                account.getAuthorizedToUpload(),
                account.getInstagram(),
                new BigDecimal("0.0"),
                new BigDecimal("0.0"),
                account.getPaymentGateway(),
                account.getBank(),
                account.getIp(),
                account.getAuthorExclusive(),
                0l,
                imagesRepository.getCountWithStatusAndUserId(Images.Status.active, account.getId()));

        return new ResponseEntity<>(userModel, HttpStatus.OK);
    }

    @GetMapping("/account/{id}/images")
    public ResponseEntity<List<ImageItemModel>> account(@PathVariable Integer id, @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageNumber) {

        Paged<ImageItemModel> images = imagesService.getByUserImageItemPage(id, pageNumber, 40);

        List<ImageItemModel> imageItemModels = images.getPage().toList();

        imageItemModels = imageItemModels
                .stream()
                .map(e -> new ImageItemModel(e.getId(),
                        "https://svanty.com/public/uploads/thumbnail/" +e.getThumbnail(),
                        "https://svanty.com/public/uploads/preview/" + e.getPreview(),
                        e.getTitle(),
                        e.getDescription(),
                        e.getCategories_id(),
                        null,
                        e.getDate(),
                        e.getStatus(),
                        "",
                        e.getTags(),
                        e.getExtension(),
                        e.getColors(),
                        e.getExif(),
                        e.getCamera(),
                        e.getFeatured(),
                        e.getFeatured_date(),
                        e.getHow_use_image(),
                        e.getAttribution_required(),
                        e.getOriginal_name(),
                        e.getPrice(),
                        e.getItem_for_sale(),
                        e.getVector(),
                        e.getHeight(),
                        e.getWidth(),
                        e.getResolution(),
                        e.getSlug(),
                        e.getDownloadCount(),
                        e.getLikeCount()
                )).collect(Collectors.toList());

        images.getPage().toList();
        return new ResponseEntity<>(imageItemModels,
                HttpStatus.OK);

    }
}
